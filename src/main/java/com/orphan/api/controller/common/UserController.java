package com.orphan.api.controller.common;

import com.google.gson.JsonObject;
import com.orphan.api.controller.common.dto.LoginRequest;
import com.orphan.api.controller.common.dto.PasswordDto;
import com.orphan.api.controller.common.dto.RegisterRequestDto;
import com.orphan.api.controller.common.dto.ResetPasswordDto;
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
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final AuthService authService;

    private final JwtUtils jwtUtils;

    private static final String LOGIN_ENDPOINT = "/login";
    private static final String REGISTER_USER_INDIVIDUAL_ENDPOINT = "/register";
    private static final String LOGOUT_ENDPOINT = "/logout";
    private static final String RESET_PASSWORD_ENDPOINT = "/reset-password";
    private static final String CHANGE_PASSWORD_ENDPOINT = "/change-password";

    /**
     * Login
     *
     * @param loginRequest {@link LoginRequest}
     * @return AccessToken
     * @throws NotFoundException
     */
    @PostMapping(LOGIN_ENDPOINT)
    @ApiOperation("Login")
    public APIResponse<AccessToken> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws NotFoundException {
        return APIResponse.okStatus(authService.login(loginRequest));
    }

//        /**
//     * Logout
//     *
//     * @param request
//     * @param response
//     * @return AccessToken
//     * @throws NotFoundException
//     */
//    @GetMapping(LOGOUT_ENDPOINT)
//    @ApiOperation("Logout")
//    public APIResponse<?> logoutPage(HttpServletRequest request, HttpServletResponse response) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null){
//            new SecurityContextLogoutHandler().logout(request, response, auth);
//        }
//        SecurityContextHolder.getContext().setAuthentication(null);
//        String authHeader = request.getHeader(Constants.AUTHORIZATION_HEADER);
//        if (authHeader != null && authHeader.startsWith(Constants.JWT_TOKEN_TYPE)) {
//            return authHeader.replace(Constants.JWT_TOKEN_TYPE, "");
//        }
//        return null;
//        return APIResponse.okStatus();
//    }

    /**
     * Register
     *
     * @param registerRequestDto
     * @return String
     * @throws NotFoundException
     */
    @ApiOperation("Register")
    @PostMapping(REGISTER_USER_INDIVIDUAL_ENDPOINT)
    public APIResponse<?> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto, Errors errors) throws Exception {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        RegisterRequestDto user = userService.createUser(registerRequestDto);
        return APIResponse.okStatus(user);
    }

    /**
     * Reset password
     *
     * @param email request
     * @return String
     * @throws NotFoundException
     */
    @ApiOperation("Reset password for account")
    @PostMapping(RESET_PASSWORD_ENDPOINT)
    public APIResponse<?> processResetPassword( @Valid  @RequestBody ResetPasswordDto resetPasswordDto, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_RESET_PASSWORD_BAD_REQUEST, messages.toString(), true);
        }
        userService.resetPassword(resetPasswordDto);
        return APIResponse.okStatus();
    }

    /**
     * Change password
     *
     * @param passwordDto
     * @param email       string
     * @return APIResponse
     * @throws NotFoundException
     */
    @ApiOperation("Change password for account")
    @PostMapping(CHANGE_PASSWORD_ENDPOINT)
    public APIResponse<PasswordDto> changePassword(@Valid @RequestBody PasswordDto passwordDto, @RequestParam("email") String email, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        userService.changePassWord(passwordDto, email);
        return APIResponse.okStatus(passwordDto);
    }
}
