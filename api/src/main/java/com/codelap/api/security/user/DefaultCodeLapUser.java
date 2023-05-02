package com.codelap.api.security.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

import static lombok.AccessLevel.PROTECTED;

@Slf4j
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
public class DefaultCodeLapUser implements CodeLapUser, UserDetails {

    private long id;

    public DefaultCodeLapUser(long id) {
        log.info("USER : " + id);

        this.id = id;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList();
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
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
