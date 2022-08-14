package com.Lycle.Server.config.auth;

import com.Lycle.Server.domain.User.Role;
import com.Lycle.Server.domain.User.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class UserPrincipal implements UserDetails {
    private final User user;

    private Long id;
    private String email;
    private String password;
    private Role role;
    private Long sharedId;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRoleKey().equals("ROLE_ADMIN")) {
            authorities.add(new SimpleGrantedAuthority(Role.USER.getKey()));
        }
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRoleKey());
        authorities.add(authority);
        return authorities;
    }

    public Long getSharedId(){return this.user.getSharedId();}

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    public Long getId() { return this.user.getId();}

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
