package com.orphan.common.service;

import com.orphan.api.controller.common.dto.LoginRequest;
import com.orphan.common.entity.User;
import com.orphan.config.jwt.AccessToken;
import com.orphan.config.jwt.JwtUtils;
import com.orphan.enums.UserStatus;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtProvider;

    private final MessageService messageService;

    /**
     * login
     *
     * @return Identity
     * @throws NotFoundException
     */
    public AccessToken login(LoginRequest loginRequest) throws NotFoundException, BadRequestException {
        Optional<User> user = this.getUserByEmail(loginRequest.getEmail());
        if(!user.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_USER_NOT_FOUND, this.messageService.buildMessages("error.msg.login-invalid"));
        }
        if(user.get().getUserStatus().equals(UserStatus.DELETED.getCode())){
            throw new BadRequestException(BadRequestException.ERROR_ACCOUNT_WAS_DELETED, this.messageService.buildMessages("error.msg.account-deleted"));
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.get().getLoginId(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AccessToken jwt = jwtProvider.createAccessToken(authentication, true);

        return jwt;
    }
}
