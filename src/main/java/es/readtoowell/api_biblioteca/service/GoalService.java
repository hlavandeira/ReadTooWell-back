package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.model.*;
import es.readtoowell.api_biblioteca.model.DTO.GoalDTO;
import es.readtoowell.api_biblioteca.mapper.GoalMapper;
import es.readtoowell.api_biblioteca.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
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

    public Set<GoalDTO> obtenerObjetivosEnCurso(Long idUser) {
        Set<Goal> objetivos = goalRepository.findByUsuarioId(idUser);

        return objetivos.stream()
                .filter(goal -> !objetivoCompletado(goal))
                .map(goalMapper::toDTO)
                .collect(Collectors.toSet());
    }

    public Set<GoalDTO> obtenerObjetivosTerminados(Long idUser) {
        Set<Goal> objetivos = goalRepository.findByUsuarioId(idUser);

        return objetivos.stream()
                .filter(this::objetivoCompletado)
                .map(goalMapper::toDTO)
                .collect(Collectors.toSet());
    }

    private boolean objetivoCompletado(Goal goal) {
        LocalDate fechaFin = goal.getFechaFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return goal.getCantidadActual() >= goal.getCantidad() || LocalDate.now().isAfter(fechaFin);
    }

    public GoalDTO crearObjetivo(Long idUser, GoalDTO goal) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        GoalType type = typeRepository.findByNombre(goal.getTipo())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de objetivo inválido"));
        GoalDuration duration = durationRepository.findByNombre(goal.getDuracion())
                .orElseThrow(() -> new EntityNotFoundException("Duración de objetivo inválida"));

        Set<Goal> objetivos = goalRepository.findByUsuarioId(idUser).stream()
                .filter(g -> !objetivoCompletado(g))
                .collect(Collectors.toSet());

        if (objetivos.stream().anyMatch(g ->
                g.getTipo().getNombre().equals(goal.getTipo()) &&
                        g.getDuracion().getNombre().equals(goal.getDuracion()))) {

            throw new IllegalArgumentException("Ya existe un objetivo en curso con ese tipo y duración.");
        }

        LocalDate fechaInicio;
        LocalDate fechaFin;

        if (duration.getId() == 1) {
            fechaInicio = LocalDate.of(LocalDate.now().getYear(), 1, 1);
            fechaFin = LocalDate.of(LocalDate.now().getYear(), 12, 31);
        } else if (duration.getId() == 2) {
            YearMonth mesActual = YearMonth.now();
            fechaInicio = mesActual.atDay(1);
            fechaFin = mesActual.atEndOfMonth();
        } else {
            throw new IllegalArgumentException("Duración de objetivo inválida");
        }

        goal.setFechaInicio(Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        goal.setFechaFin(Date.from(fechaFin.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        goal.setUsuario(user);

        Goal objetivo = goalMapper.toEntity(goal);
        objetivo.setCantidadActual(0);
        objetivo.setDuracion(duration);
        objetivo.setTipo(type);

        objetivo = goalRepository.save(objetivo);

        actualizarObjetivos(idUser, 0);

        return goalMapper.toDTO(objetivo);
    }

    public GoalDTO eliminarObjetivo(Long idUser, Long idGoal) {
        Goal goal = goalRepository.findById(idGoal)
                .orElseThrow(() -> new EntityNotFoundException("Objetivo con ID " + idGoal + " no encontrado."));

        if (!goal.getUsuario().getId().equals(idUser)) {
            throw new AccessDeniedException("No tienes permiso para borrar este objetivo");
        }

        goalRepository.delete(goal);

        return goalMapper.toDTO(goal);
    }

    public void actualizarObjetivos(Long idUser, int paginas) {
        Set<Goal> goals = goalRepository.findByUsuarioId(idUser).stream()
                .filter(goal -> !objetivoCompletado(goal)).collect(Collectors.toSet());

        Set<Book> booksReadYear = libraryRepository.findBooksReadActualYear(idUser);
        Set<Book> booksReadMonth = libraryRepository.findBooksReadActualMonth(idUser);

        for (Goal goal : goals) {
            Set<Book> books = goal.getDuracion().getNombre().equals("Anual") ? booksReadYear : booksReadMonth;

            if (goal.getTipo().getNombre().equals("Libros")) {
                int librosTotal = books.size();
                goal.setCantidadActual(librosTotal);

            } else if (goal.getTipo().getNombre().equals("Páginas")) {
                int paginasTotal = books.stream().mapToInt(Book::getNumeroPaginas).sum() + paginas;
                goal.setCantidadActual(paginasTotal);

            } else {
                throw new IllegalArgumentException("Tipo de objetivo inválido.");
            }

            goalRepository.save(goal);
        }
    }
}
