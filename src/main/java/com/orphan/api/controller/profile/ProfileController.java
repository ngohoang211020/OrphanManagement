package com.orphan.api.controller.profile;

import com.google.gson.JsonObject;
import com.orphan.api.controller.admin.dto.UserDetailDto;
import com.orphan.api.controller.common.dto.PasswordDto;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.UserNotifyService;
import com.orphan.common.service.UserService;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/profile")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER_LOGISTIC','EMPLOYEE','MANAGER_CHILDREN','MANAGER_HR')")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    private final UserNotifyService userNotifyService;

    /**
     * View User login
     *
     * @return APIResponse
     * @throws NotFoundException
     */

    @ApiOperation("View detail info of the currently logged user")
    @GetMapping("/account")
    public APIResponse<?> viewLoggedUserDetail() throws NotFoundException, IOException {
        UserDetailDto userDetailDto = userService.viewUserDetail(userService.getCurrentUserId());
        return APIResponse.okStatus(userDetailDto);
    }

    /**
     * Update User login
     *
     * @return APIResponse
     * @throws NotFoundException
     * @Param registerRequestDto RegisterRequestDto
     */


    @ApiOperation("Update detail info of the currently logged user")
    @PostMapping("/account")
    public APIResponse<?> updateLoggedUserDetail(@Valid @RequestBody UserDetailDto userDetailDto, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        userDetailDto = userService.updateUserDetail(userDetailDto, userService.getCurrentUserId());
        return APIResponse.okStatus(userDetailDto);
    }

    @ApiOperation("Change password User")
    @PostMapping("/account/changepassword")
    public ResponseEntity<?> changePasswordUser(@Valid @RequestBody PasswordDto passwordDto, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        userService.changePassWord(passwordDto, userService.getCurrentUserId());
        return APIResponse.okStatus();
    }

    /**
     * Delete User Logged
     *
     * @return APIResponse
     * @throws NotFoundException
     */
    @ApiOperation("Delete User Logged")
    @DeleteMapping("/account")
    public ResponseEntity<?> deleteAccount() throws NotFoundException, BadRequestException {
        userService.deleteUserById(userService.getCurrentUserId());
        return APIResponse.okStatus();
    }

    @ApiOperation("View User Detail By id")
    @GetMapping("/account/{id}")
    public APIResponse<?> viewUserDetail(@PathVariable("id") Integer id) throws NotFoundException, IOException {
        UserDetailDto userDetailDto = userService.viewUserDetail(id);
        return APIResponse.okStatus(userDetailDto);
    }

    @ApiOperation("View list notify of user logged")
    @GetMapping("/notify")
    public APIResponse<?> viewListNotify() {
        return APIResponse.okStatus(userNotifyService.findListNotifyByUserId());
    }

    @ApiOperation("View specify notify of user logged")
    @GetMapping("/notify/{id}")
    public APIResponse<?> viewListNotify(@PathVariable("id") Integer id) {
        return APIResponse.okStatus(userNotifyService.findNotifyById(id));
    }
}
