package com.orphan.api.controller.common;

import com.google.gson.JsonObject;
import com.orphan.api.controller.common.dto.LoginRequest;
import com.orphan.api.controller.common.dto.RegisterRequestDto;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.AuthService;
import com.orphan.common.service.UserService;
import com.orphan.config.jwt.AccessToken;
import com.orphan.config.jwt.JwtUtils;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final AuthService authService;

    private final JwtUtils jwtUtils;

    private static final String LOGIN_ENDPOINT = "/login";
    private static final String REGISTER_USER_INDIVIDUAL_ENDPOINT = "/register";

    /**
     * Login
     *
     * @param loginRequest {@link LoginRequest}
     * @param request
     * @return AccessToken
     * @throws NotFoundException
     */
    @PostMapping(LOGIN_ENDPOINT)
    @ApiOperation("Login")
    public APIResponse<AccessToken> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws NotFoundException {
        return APIResponse.okStatus(authService.login(loginRequest));
    }

    /**
     * Register
     *
     * @param registerRequestDto
     * @return String
     * @throws NotFoundException
     */
    @ApiOperation("Register")
    @PostMapping(REGISTER_USER_INDIVIDUAL_ENDPOINT)
    public APIResponse<?> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto, BindingResult errors) throws Exception {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        RegisterRequestDto user = userService.createUser(registerRequestDto);
        return APIResponse.okStatus(user);
    }
}
