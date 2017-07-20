package com.aniov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by aniov on 7/13/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SiteUserDetails implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getAccount().getAuthorities();
    }

    @Override
    public String getPassword() {
        return user.getHashedPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return user.getAccount().isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getAccount().isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return user.getAccount().isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return user.getAccount().isEnabled();
    }
}
