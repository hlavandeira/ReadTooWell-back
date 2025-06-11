package es.readtoowell.api_biblioteca.controller.book;

import es.readtoowell.api_biblioteca.model.DTO.book.RatedBookDTO;
import es.readtoowell.api_biblioteca.model.entity.User;
import es.readtoowell.api_biblioteca.service.book.RecommendationService;
import es.readtoowell.api_biblioteca.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controlador que gestiona las peticiones HTTP relativas a las recomendaciones.
 */
@RestController
@RequestMapping("/recomendaciones")
public class RecommendationController {
    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private UserService userService;

    /**
     * Busca posibles recomendaciones a partir de los libros favoritos de un usuario.
     *
     * @return Lista con libros recomendados
     * @throws AccessDeniedException El usuario no está autenticado
     */
    @GetMapping("/libros-favoritos")
    public ResponseEntity<List<RatedBookDTO>> getRecommendationsByFavoriteBooks() {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        List<RatedBookDTO> recoms = recommendationService.getRecommendationsByFavoriteBooks(user.getId());

        return ResponseEntity.ok(recoms);
    }

    /**
     * Busca posibles recomendaciones a partir de los géneros favoritos de un usuario.
     *
     * @return Lista con libros recomendados
     * @throws AccessDeniedException El usuario no está autenticado
     */
    @GetMapping("/generos-favoritos")
    public ResponseEntity<List<RatedBookDTO>> getRecommendationsByFavoriteGenres() {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        List<RatedBookDTO> recoms = recommendationService.getRecommendationsByFavoriteGenres(user.getId());

        return ResponseEntity.ok(recoms);
    }

    /**
     * Busca posibles recomendaciones a partir de los libros leídos de un usuario que tengan
     * una calificación superior o igual a 3 sobre 5.
     *
     * @return Lista con libros recomendados
     * @throws AccessDeniedException El usuario no está autenticado
     */
    @GetMapping("/libros-leidos")
    public ResponseEntity<List<RatedBookDTO>> getRecommendationsByReadBooks() {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        List<RatedBookDTO> recoms = recommendationService.getRecommendationsByReadBooks(user.getId());

        return ResponseEntity.ok(recoms);
    }

    /**
     * Busca posibles recomendaciones a partir de una lista de un usuario, utilizando los libros de la lista
     * y los géneros asociados a esta.
     *
     * @param idList ID de la lista
     * @return Lista con libros recomendados
     * @throws AccessDeniedException El usuario no está autenticado
     */
    @GetMapping("/lista/{idList}")
    public ResponseEntity<List<RatedBookDTO>> getRecommendationsByList(@PathVariable Long idList) {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        List<RatedBookDTO> recoms = recommendationService.getRecommendationsByList(user.getId(), idList);

        return ResponseEntity.ok(recoms);
    }

    /**
     * Busca recomendaciones generales para cualquier usuario, en función de los libros más populares
     * publicados en los últimos años.
     *
     * @return Lista con libros recomendados
     * @throws AccessDeniedException El usuario no está autenticado
     */
    @GetMapping("/catalogo")
    public ResponseEntity<List<RatedBookDTO>> getGeneralRecommendations() {
        User user = userService.getAuthenticatedUser();
        if (user == null) {
            throw new AccessDeniedException("Usuario no autenticado.");
        }

        List<RatedBookDTO> recoms = recommendationService.getGeneralRecommendations(user.getId());

        return ResponseEntity.ok(recoms);
    }
}
