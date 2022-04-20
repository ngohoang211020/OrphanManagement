package com.orphan.api.controller.admin;

import com.google.gson.JsonObject;
import com.orphan.api.controller.admin.dto.UserDetailDto;
import com.orphan.api.controller.admin.dto.UserDto;
import com.orphan.api.controller.common.dto.RegisterRequestDto;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.UserService;
import com.orphan.common.vo.PageInfo;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.PageableConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)

public class AdminController {

    private final UserService userService;

    @ApiOperation("View All Users")
    @GetMapping("/all")
    public APIResponse<?> viewAllUsers() throws NotFoundException, IOException {
        return APIResponse.okStatus(userService.viewAllUsers());
    }

    @ApiOperation("Get Users By Page")
    @GetMapping
    public APIResponse<?> viewUsersByPage(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
    ) throws NotFoundException {
        PageInfo<UserDto> userDtoPageInfo;
        if (page != null) {
            userDtoPageInfo = userService.viewUsersByPage(page, PageableConstants.limit);
        } else {
            userDtoPageInfo = userService.viewUsersByPage(1, PageableConstants.limit);

        }
        return APIResponse.okStatus(userDtoPageInfo);
    }

    @ApiOperation("View User Detail")
    @GetMapping("/{id}")
    public APIResponse<?> viewUserDetail(@PathVariable("id") Integer id) throws NotFoundException, IOException {
        UserDetailDto userDetailDto = userService.viewUserDetail(id);
        return APIResponse.okStatus(userDetailDto);
    }

    @ApiOperation("Create User")
    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody @Valid RegisterRequestDto newUserRegisterInfo
            , Errors errors) throws BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        RegisterRequestDto newRegisterRequest = userService.createUser(newUserRegisterInfo);

        return APIResponse.okStatus(newRegisterRequest);
    }

    @ApiOperation("Update User Info")
    @PutMapping(value = "/{userId}", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<?> updateUserDetail(@PathVariable("userId") Integer userId, @RequestBody @Valid RegisterRequestDto registerRequestDto, Errors errors) throws BadRequestException, NotFoundException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        RegisterRequestDto newUserDetailDto = userService.updateUser(registerRequestDto, userId);
        return APIResponse.okStatus(newUserDetailDto);
    }

    @ApiOperation("Delete User")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Integer userId) throws NotFoundException, BadRequestException {
        userService.deleteUserById(userId);
        return APIResponse.okStatus();
    }


}