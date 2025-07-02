package es.readtoowell.api_biblioteca.unit.service;

import es.readtoowell.api_biblioteca.mapper.GoalMapper;
import es.readtoowell.api_biblioteca.model.DTO.GoalDTO;
import es.readtoowell.api_biblioteca.model.entity.*;
import es.readtoowell.api_biblioteca.repository.goal.GoalDurationRepository;
import es.readtoowell.api_biblioteca.repository.goal.GoalRepository;
import es.readtoowell.api_biblioteca.repository.goal.GoalTypeRepository;
import es.readtoowell.api_biblioteca.repository.library.UserLibraryBookRepository;
import es.readtoowell.api_biblioteca.repository.user.UserRepository;
import es.readtoowell.api_biblioteca.service.goal.GoalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Clase de pruebas para el servicio de objetivos de lectura.
 */
@ExtendWith(MockitoExtension.class)
public class GoalServiceTests {
    @Mock
    private GoalRepository goalRepository;
    @Mock
    private GoalMapper goalMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GoalTypeRepository typeRepository;
    @Mock
    private GoalDurationRepository durationRepository;
    @Mock
    private UserLibraryBookRepository libraryRepository;

    @InjectMocks
    private GoalService goalService;

    private Goal goalInProgress;
    private Goal finishedGoal;
    private GoalType type;
    private GoalDuration duration;

    @BeforeEach
    public void init() {
        goalInProgress = new Goal();
        goalInProgress.setAmount(5);
        goalInProgress.setCurrentAmount(2);
        goalInProgress.setDateFinish(Date.from(LocalDate.now().plusDays(5)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));

        finishedGoal = new Goal();
        finishedGoal.setAmount(3);
        finishedGoal.setCurrentAmount(3);
        finishedGoal.setDateFinish(Date.from(LocalDate.now().minusDays(1)
                .atStartOfDay(ZoneId.systemDefault()).toInstant()));

        type = new GoalType();
        type.setId(1L);
        type.setName("Libros");

        duration = new GoalDuration();
        duration.setId(1L);
        duration.setName("Anual");

        goalInProgress.setType(type);
        goalInProgress.setDuration(duration);
        finishedGoal.setType(type);
        finishedGoal.setDuration(duration);
    }

    @Test
    public void GoalService_GetGoalsInProgress_ReturnGoals() {
        Long userId = 1L;

        when(goalRepository.findByUserId(userId)).thenReturn(List.of(goalInProgress, finishedGoal));
        when(goalMapper.toDTO(any())).thenReturn(new GoalDTO());

        List<GoalDTO> result = goalService.getGoalsInProgress(userId);

        assertEquals(1, result.size());
    }

    @Test
    public void GoalService_GetFinishedGoals_ReturnGoals() {
        Long userId = 1L;

        when(goalRepository.findByUserId(userId)).thenReturn(List.of(finishedGoal));
        when(goalMapper.toDTO(any())).thenReturn(new GoalDTO());

        List<GoalDTO> result = goalService.getFinishedGoals(userId);

        assertEquals(1, result.size());
    }

    @Test
    public void GoalService_GetFinishedGoalsActualYear_ReturnGoals() {
        Long userId = 1L;

        when(goalRepository.findByUserId(userId)).thenReturn(Collections.emptyList());

        List<GoalDTO> result = goalService.getFinishedGoalsActualYear(userId);

        assertTrue(result.isEmpty());
    }

    @Test
    public void GoalService_CreateGoal_ReturnCreated() {
        Long userId = 1L;
        GoalDTO dto = new GoalDTO();
        dto.setType("Libros");
        dto.setDuration("Anual");
        dto.setAmount(5);

        User user = new User();
        user.setId(userId);

        Book book1 = new Book();
        Book book2 = new Book();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(typeRepository.findByName("Libros")).thenReturn(Optional.of(type));
        when(durationRepository.findByName("Anual")).thenReturn(Optional.of(duration));
        when(goalRepository.findByUserId(userId)).thenReturn(Collections.emptyList());
        when(libraryRepository.findBooksReadActualYear(userId)).thenReturn(List.of(book1, book2));
        when(goalMapper.toEntity(any())).thenReturn(new Goal());
        when(goalRepository.save(any())).thenReturn(new Goal());
        when(goalMapper.toDTO(any())).thenReturn(dto);

        GoalDTO result = goalService.createGoal(userId, dto);

        assertEquals("Libros", result.getType());
        assertEquals("Anual", result.getDuration());
        assertEquals(5, result.getAmount());
        assertEquals(1L, result.getUser().getId());
    }

    @Test
    public void GoalService_CreateGoal_RepeatedGoal() {
        Long userId = 1L;
        GoalDTO dto = new GoalDTO();
        dto.setType("Libros");
        dto.setDuration("Anual");

        User user = new User();

        Goal goal = new Goal();
        goal.setType(type);
        goal.setDuration(duration);
        goal.setCurrentAmount(0);
        goal.setAmount(10);
        goal.setDateFinish(Date.from(LocalDate.now().plusDays(5).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(typeRepository.findByName("Libros")).thenReturn(Optional.of(type));
        when(durationRepository.findByName("Anual")).thenReturn(Optional.of(duration));
        when(goalRepository.findByUserId(userId)).thenReturn(List.of(goal));

        assertThrows(IllegalArgumentException.class, () -> goalService.createGoal(userId, dto));
    }

    @Test
    public void GoalService_DeleteGoal_ReturnDeleted() {
        Long userId = 1L;
        Long goalId = 2L;
        Goal goal = new Goal();
        goal.setId(goalId);
        goal.setUser(new User());
        goal.getUser().setId(userId);

        when(goalRepository.findById(goalId)).thenReturn(Optional.of(goal));
        when(goalMapper.toDTO(goal)).thenReturn(new GoalDTO());

        GoalDTO result = goalService.deleteGoal(userId, goalId);

        assertNotNull(result);
        verify(goalRepository).delete(goal);
    }

    @Test
    public void GoalService_DeleteGoal_UserIsNotOwner() {
        Long userId = 1L;
        Long goalId = 2L;
        Goal goal = new Goal();
        goal.setId(goalId);
        goal.setUser(new User());
        goal.getUser().setId(999L);

        when(goalRepository.findById(goalId)).thenReturn(Optional.of(goal));

        assertThrows(AccessDeniedException.class, () -> goalService.deleteGoal(userId, goalId));
    }

    @Test
    public void GoalService_UpdateGoals_UpdatesGoals() {
        Long userId = 1L;
        Goal goal = new Goal();
        goal.setType(type);
        goal.setDuration(duration);
        goal.setAmount(10);
        goal.setCurrentAmount(0);
        goal.setDateFinish(Date.from(LocalDate.now().plusDays(10).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Book book1 = new Book();
        Book book2 = new Book();

        when(goalRepository.findByUserId(userId)).thenReturn(List.of(goal));
        when(libraryRepository.findBooksReadActualYear(userId)).thenReturn(List.of(book1, book2));
        when(libraryRepository.findBooksReadActualMonth(userId)).thenReturn(Collections.emptyList());

        goalService.updateGoals(userId, 0);

        verify(goalRepository).save(goal);
        assertEquals(2, goal.getCurrentAmount());
    }
}
