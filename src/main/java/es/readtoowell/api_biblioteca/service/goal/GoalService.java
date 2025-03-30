package es.readtoowell.api_biblioteca.service.goal;

import es.readtoowell.api_biblioteca.model.DTO.GoalDTO;
import es.readtoowell.api_biblioteca.mapper.GoalMapper;
import es.readtoowell.api_biblioteca.model.entity.*;
import es.readtoowell.api_biblioteca.repository.goal.GoalDurationRepository;
import es.readtoowell.api_biblioteca.repository.goal.GoalRepository;
import es.readtoowell.api_biblioteca.repository.goal.GoalTypeRepository;
import es.readtoowell.api_biblioteca.repository.library.UserLibraryBookRepository;
import es.readtoowell.api_biblioteca.repository.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GoalService {
    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    private GoalMapper goalMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GoalTypeRepository typeRepository;
    @Autowired
    private GoalDurationRepository durationRepository;
    @Autowired
    private UserLibraryBookRepository libraryRepository;

    /**
     * Devuelve los objetivos en curso de un usuario.
     *
     * @param idUser ID del usuario
     * @return Lista con los objetivos como DTOs
     */
    public Set<GoalDTO> getGoalsInProgress(Long idUser) {
        Set<Goal> objetivos = goalRepository.findByUserId(idUser);

        objetivos.forEach(g -> updateGoals(idUser, 0));

        return objetivos.stream()
                .filter(goal -> !isGoalCompleted(goal))
                .map(goalMapper::toDTO)
                .collect(Collectors.toSet());
    }

    /**
     * Devuelve los objetivos terminados de un usuario.
     *
     * @param idUser ID del usuario
     * @return Lista con los objetivos como DTOs
     */
    public Set<GoalDTO> getCompletedGoals(Long idUser) {
        Set<Goal> objetivos = goalRepository.findByUserId(idUser);

        return objetivos.stream()
                .filter(this::isGoalCompleted)
                .map(goalMapper::toDTO)
                .collect(Collectors.toSet());
    }

    /**
     * Comprueba si un objetivo ha sido completado/terminado.
     *
     * @param goal Objetivo que se va a comprobar
     * @return 'true' si el objetivo está completado/terminado, 'false' en caso contrario
     */
    private boolean isGoalCompleted(Goal goal) {
        LocalDate fechaFin = goal.getDateFinish().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return goal.getCurrentAmount() >= goal.getAmount() || LocalDate.now().isAfter(fechaFin);
    }

    /**
     * Crea un nuevo objetivo para un usuario.
     *
     * @param idUser ID del usuario que crea el objetivo
     * @param goal DTO con los datos del objetivo a crear
     * @return DTO con los datos del objetivo creado
     * @throws EntityNotFoundException El usuario, tipo o duración de objetivo no existen
     * @throws IllegalArgumentException Objetivo en curso repetido o duración inválida
     */
    public GoalDTO createGoal(Long idUser, GoalDTO goal) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        GoalType type = typeRepository.findByName(goal.getType())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de objetivo inválido"));
        GoalDuration duration = durationRepository.findByName(goal.getDuration())
                .orElseThrow(() -> new EntityNotFoundException("Duración de objetivo inválida"));

        Set<Goal> objetivos = goalRepository.findByUserId(idUser).stream()
                .filter(g -> !isGoalCompleted(g))
                .collect(Collectors.toSet());

        if (objetivos.stream().anyMatch(g ->
                g.getType().getName().equals(goal.getType()) &&
                        g.getDuration().getName().equals(goal.getDuration()))) {

            throw new IllegalArgumentException("Ya existe un objetivo en curso con ese tipo y duración.");
        }

        LocalDate fechaInicio;
        LocalDate fechaFin;
        Set<Book> booksReadYet;

        if (duration.getId() == 1) {
            fechaInicio = LocalDate.of(LocalDate.now().getYear(), 1, 1);
            fechaFin = LocalDate.of(LocalDate.now().getYear(), 12, 31);
            booksReadYet = libraryRepository.findBooksReadActualYear(idUser);
        } else if (duration.getId() == 2) {
            YearMonth mesActual = YearMonth.now();
            fechaInicio = mesActual.atDay(1);
            fechaFin = mesActual.atEndOfMonth();
            booksReadYet = libraryRepository.findBooksReadActualMonth(idUser);
        } else {
            throw new IllegalArgumentException("Duración de objetivo inválida");
        }

        goal.setDateStart(Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        goal.setDateFinish(Date.from(fechaFin.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        goal.setUser(user);

        Goal objetivo = goalMapper.toEntity(goal);
        objetivo.setCurrentAmount(0);
        objetivo.setDuration(duration);
        objetivo.setType(type);

        // Establecer la cantidad actual por si ya se ha hecho progreso
        if (type.getName().equals("Libros")) {
            int librosTotal = booksReadYet.size();
            objetivo.setCurrentAmount(librosTotal);
        } else if (type.getName().equals("Páginas")) {
            int paginasTotal = booksReadYet.stream().mapToInt(Book::getPageNumber).sum();
            objetivo.setCurrentAmount(paginasTotal);
        }

        objetivo = goalRepository.save(objetivo);

        return goalMapper.toDTO(objetivo);
    }

    /**
     * Elimina un objetivo de un usuario.
     *
     * @param idUser ID del usuario
     * @param idGoal ID del objetivo a borrar
     * @return DTO con los datos del objetivo borrado
     * @throws EntityNotFoundException El objetivo no existe
     * @throws AccessDeniedException El usuario no es propietario del objetivo
     */
    public GoalDTO deleteGoal(Long idUser, Long idGoal) {
        Goal goal = goalRepository.findById(idGoal)
                .orElseThrow(() -> new EntityNotFoundException("Objetivo con ID " + idGoal + " no encontrado."));

        if (!goal.getUser().getId().equals(idUser)) {
            throw new AccessDeniedException("No tienes permiso para borrar este objetivo");
        }

        goalRepository.delete(goal);

        return goalMapper.toDTO(goal);
    }

    /**
     * Actualiza el progreso de los objetivos de un usuario.
     *
     * @param idUser ID del usuario
     * @param paginasProgreso Número de páginas del último progreso actualizado
     * @throws IllegalArgumentException Tipo de objetivo inválido
     */
    public void updateGoals(Long idUser, int paginasProgreso) {
        Set<Goal> goals = goalRepository.findByUserId(idUser).stream()
                .filter(goal -> !isGoalCompleted(goal)).collect(Collectors.toSet());

        Set<Book> booksReadYear = libraryRepository.findBooksReadActualYear(idUser);
        Set<Book> booksReadMonth = libraryRepository.findBooksReadActualMonth(idUser);

        for (Goal goal : goals) {
            Set<Book> booksRead = goal.getDuration().getName().equals("Anual")
                    ? booksReadYear : booksReadMonth;

            if (goal.getType().getName().equals("Libros")) {
                int librosTotal = booksRead.size();
                goal.setCurrentAmount(librosTotal);

            } else if (goal.getType().getName().equals("Páginas")) {
                int progresoTotal = goal.getCurrentAmount() + paginasProgreso;
                goal.setCurrentAmount(progresoTotal);

            } else {
                throw new IllegalArgumentException("Tipo de objetivo inválido.");
            }

            goalRepository.save(goal);
        }
    }
}
