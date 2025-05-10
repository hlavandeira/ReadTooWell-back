package es.readtoowell.api_biblioteca.config.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utilidad para la generación y validación de tokens JWT.
 */
@Component
public class JwtUtil {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 7200000; // 2 horas

    /**
     * Genera un token JWT basado en el correo electrónico del usuario.
     *
     * @param email Correo electrónico del usuario.
     * @return Token JWT generado.
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Extrae el correo electrónico de un token JWT.
     *
     * @param token Token JWT.
     * @return Correo electrónico extraído del token.
     */
    public String extractEmail(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            System.out.println("Token expirado");
        } catch (SignatureException e) {
            System.out.println("Token inválido");
        }
        return null;
    }

    /**
     * Valida si un token JWT es válido.
     *
     * @param token Token JWT a validar.
     * @return 'true' si el token es válido, 'false' en caso contrario.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
