package com.orphan.common.service;

import com.orphan.api.controller.common.dto.LoginRequest;
import com.orphan.common.entity.User;
import com.orphan.config.jwt.AccessToken;
import com.orphan.config.jwt.JwtUtils;
import com.orphan.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService extends BaseService{

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtProvider;

    private final MessageService messageService;

    /**
     * login
     *
     * @return Identity
     * @throws NotFoundException
     */
    public AccessToken login(LoginRequest loginRequest) throws NotFoundException {
        Optional<User> user = this.getUserByEmail(loginRequest.getEmail());
        if(!user.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_USER_NOT_FOUND, this.messageService.buildMessages("error.msg.login-invalid"));
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.get().getLoginId(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AccessToken jwt = jwtProvider.createAccessToken(authentication, true);

        return jwt;
    }
}
