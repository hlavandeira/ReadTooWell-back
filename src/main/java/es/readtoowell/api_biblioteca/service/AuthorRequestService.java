package es.readtoowell.api_biblioteca.service;

import es.readtoowell.api_biblioteca.mapper.AuthorRequestMapper;
import es.readtoowell.api_biblioteca.model.AuthorRequest;
import es.readtoowell.api_biblioteca.model.DTO.AuthorRequestDTO;
import es.readtoowell.api_biblioteca.model.RequestBook;
import es.readtoowell.api_biblioteca.model.User;
import es.readtoowell.api_biblioteca.model.enums.RequestStatus;
import es.readtoowell.api_biblioteca.repository.AuthorRequestRepository;
import es.readtoowell.api_biblioteca.repository.RequestBookRepository;
import es.readtoowell.api_biblioteca.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthorRequestService {
    @Autowired
    private AuthorRequestRepository requestRepository;
    @Autowired
    private AuthorRequestMapper requestMapper;
    @Autowired
    private RequestBookRepository requestBookRepository;

    public AuthorRequestDTO sendAuthorRequest(User user, AuthorRequestDTO dto) {

        if (requestRepository.existsByUsuarioIdAndEstadoIn(user.getId(),
                List.of(RequestStatus.PENDIENTE.getValue(), RequestStatus.ACEPTADA.getValue()))) {
            throw new IllegalStateException("El usuario ya tiene una solicitud en proceso o aceptada.");
        }

        AuthorRequest request = new AuthorRequest();
        request.setNombre(dto.getNombre());
        request.setBiografia(dto.getBiografia());
        request.setFechaEnviada(Date.valueOf(LocalDate.now()));
        request.setActivo(true);
        request.setEstado(RequestStatus.PENDIENTE.getValue());
        request.setUsuario(user);

        final AuthorRequest savedRequest = requestRepository.save(request);

        Set<RequestBook> books = dto.getLibros().stream().map(req -> {
            RequestBook book = new RequestBook();
            book.setTitulo(req.getTitulo());
            book.setAñoPublicacion(req.getAñoPublicacion());
            book.setSolicitud(savedRequest);
            return book;
        }).collect(Collectors.toSet());

        savedRequest.setLibros(books);

        requestBookRepository.saveAll(books);

        AuthorRequest savedRequest2 = requestRepository.save(savedRequest);

        AuthorRequest requestWithBooks = requestRepository.findByIdWithBooks(savedRequest2.getId())
                .orElseThrow(() ->
                        new EntityNotFoundException("No se encontró la solicitud con ID " + savedRequest2.getId()));

        return requestMapper.toDTO(requestWithBooks);
    }

    public AuthorRequestDTO updateStatusRequest(Long idRequest, int newStatus) {
        if (newStatus < 0 || newStatus > 2) {
            throw new ValidationException("El nuevo estado de la solicitud es inválido.");
        }

        AuthorRequest request = requestRepository.findByIdWithBooks(idRequest)
                .orElseThrow(() -> new EntityNotFoundException("La solicitud con ID " + idRequest + " no existe."));

        request.setEstado(newStatus);

        request = requestRepository.save(request);

        return requestMapper.toDTO(request);
    }

    public Page<AuthorRequestDTO> getAllRequests(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaEnviada"));

        return requestRepository.findAll(pageable).map(requestMapper::toDTO);
    }

    public Page<AuthorRequestDTO> getRequestsWithStatus(int page, int size, int status) {
        if (status < 0 || status > 2) {
            throw new ValidationException("El estado es inválido.");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaEnviada"));

        return requestRepository.findByEstado(status, pageable).map(requestMapper::toDTO);
    }

    public AuthorRequestDTO getRequest(Long idRequest) {
        AuthorRequest request = requestRepository.findByIdWithBooks(idRequest)
                .orElseThrow(() -> new EntityNotFoundException("La solicitud con ID " + idRequest + " no existe."));

        return requestMapper.toDTO(request);
    }
}
