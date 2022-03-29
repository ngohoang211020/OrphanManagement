package com.orphan.config.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.orphan.common.entity.Role;
import com.orphan.common.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * User details.
 */
@Getter
@NoArgsConstructor
public class UserPrincipal implements UserDetails {

    private Integer userId;

    private String email;
    @JsonIgnore
    private String password;
    //	private String langKey;

    private List<Role> roles;
    //   private UserEnum.Status status;
    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Integer id, String password, List<Role> roles, Collection<? extends GrantedAuthority> authorities) {
        this.userId = id;
//        this.name = name;
//		this.langKey = langKey;
        this.password = password;
        //       this.status = status;
        this.roles = roles;
        this.authorities = authorities;
    }

    public UserPrincipal(Integer id, String password, List<Role> roles) {
        this.userId = id;
//        this.name = name;
//		this.langKey = langKey;
        this.password = password;
        //       this.status = status;
        this.roles = roles;

    }

    public static UserPrincipal create(User user, Collection<? extends GrantedAuthority> authorities) throws UsernameNotFoundException, Exception {
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("The user is empty");
        }

        return new UserPrincipal(user.getLoginId(), user.getPassword(), user.getRoles(), authorities);
    }

    public static UserPrincipal create(User user) throws UsernameNotFoundException, Exception {
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("The user is empty");
        }

        return new UserPrincipal(user.getLoginId(), user.getPassword(), user.getRoles());
    }


    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
