package com.orphan.api.controller.admin;

import com.google.gson.JsonObject;
import com.orphan.api.controller.admin.dto.UserDetailDto;
import com.orphan.api.controller.admin.dto.UserDto;
import com.orphan.api.controller.common.dto.RegisterRequestDto;
import com.orphan.common.request.SearchRequest;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.UserService;
import com.orphan.common.vo.PageInfo;
import com.orphan.enums.UserStatus;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @ApiOperation("View All Users ACTIVED")
    @GetMapping("/all")
    public APIResponse<?> viewAllActivedUsers() throws NotFoundException, IOException {
        return APIResponse.okStatus(userService.viewAllUsersActived(UserStatus.ACTIVED.getCode()));
    }

    @ApiOperation("Get Users ACTIVED By Page")
    @GetMapping
    public APIResponse<?> viewUsersByPage(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
    ,@ApiParam(value = "Limit", required = false) @RequestParam(value = "limit", required = false) Integer limit) throws NotFoundException {
        PageInfo<UserDto> userDtoPageInfo;
        if (page != null) {
            userDtoPageInfo = userService.viewUsersActivedByPage(page, limit,UserStatus.ACTIVED.getCode());
        } else {
            userDtoPageInfo = userService.viewUsersActivedByPage(1, limit,UserStatus.ACTIVED.getCode());

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
            , Errors errors) throws BadRequestException, NotFoundException, MessagingException {
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

    @ApiOperation("Delete User Forever")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Integer userId)
            throws NotFoundException {
        userService.deleteUserById(userId);
        return APIResponse.okStatus();
    }

    @ApiOperation("View All Users Deleted")
    @GetMapping("/all/deleted")
    public APIResponse<?> viewAllUsersDeleted() throws NotFoundException {
        return APIResponse.okStatus(userService.viewAllUsersDeleted(UserStatus.DELETED.getCode()));
    }

    @ApiOperation("Update Status deleted/active")
    @PutMapping("/{userId}/updateStatus")
    public APIResponse<?> updateStatus(@PathVariable("userId") Integer userId) throws NotFoundException, IOException {
        userService.updateStatusUser(userId);
        return APIResponse.okStatus();
    }


    @ApiOperation("Get Users Deleted By Page")
    @GetMapping("/deleted")
    public APIResponse<?> viewUsersDeletedByPage(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
            ,@ApiParam(value = "Limit", required = false) @RequestParam(value = "limit", required = false) Integer limit) throws NotFoundException {
        PageInfo<UserDto> userDtoPageInfo;
        if (page != null) {
            userDtoPageInfo = userService.viewUsersDeletedByPage(page, limit,UserStatus.DELETED.getCode());
        } else {
            userDtoPageInfo = userService.viewUsersDeletedByPage(1, limit,UserStatus.DELETED.getCode());

        }
        return APIResponse.okStatus(userDtoPageInfo);
    }

    @ApiOperation("Search Users ACTIVED")
    @PostMapping("/search")
    public APIResponse<?> searchUsersActived(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
            ,@ApiParam(value = "Limit", required = false) @RequestParam(value = "limit", required = false) Integer limit, @RequestBody SearchRequest searchRequest) throws NotFoundException {
        PageInfo<UserDto> userDtoPageInfo;
        if (page != null) {
            userDtoPageInfo = userService.searchUser(searchRequest.getKeyword(),UserStatus.ACTIVED.getCode(), page, limit);
        } else {
            userDtoPageInfo = userService.searchUser(searchRequest.getKeyword(),UserStatus.ACTIVED.getCode(),1, limit);

        }
        return APIResponse.okStatus(userDtoPageInfo);
    }

    @ApiOperation("Search Users Deleted")
    @PostMapping("/search/deleted")
    public APIResponse<?> searchUsersDeleted(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
            ,@ApiParam(value = "Limit", required = false) @RequestParam(value = "limit", required = false) Integer limit , @RequestBody SearchRequest searchRequest) throws NotFoundException {
        PageInfo<UserDto> userDtoPageInfo;
        if (page != null) {
            userDtoPageInfo = userService.searchUser(searchRequest.getKeyword(),UserStatus.DELETED.getCode(), page, limit);
        } else {
            userDtoPageInfo = userService.searchUser(searchRequest.getKeyword(),UserStatus.DELETED.getCode(),1, limit);

        }
        return APIResponse.okStatus(userDtoPageInfo);
    }



}