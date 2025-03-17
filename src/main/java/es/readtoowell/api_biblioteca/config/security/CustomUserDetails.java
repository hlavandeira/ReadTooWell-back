package es.readtoowell.api_biblioteca.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {
    private final Long id;
    private final String correo;
    private final String contraseña;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Long id, String correo, String contraseña, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.correo = correo;
        this.contraseña = contraseña;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public String getPassword() {
        return contraseña;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}