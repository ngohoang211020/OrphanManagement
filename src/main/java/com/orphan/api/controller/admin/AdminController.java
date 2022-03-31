package com.orphan.api.controller.admin;

import com.orphan.api.controller.UpdateImageResponse;
import com.orphan.api.controller.admin.dto.UserDetailDto;
import com.orphan.api.controller.admin.dto.UserDto;
import com.orphan.api.controller.common.dto.RegisterRequestDto;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.UserService;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

  //  private final String photoImagePaths="src/main/resources/user-photos/";

    @ApiOperation("Get All Users")
    @GetMapping
    public APIResponse<?> viewAllAccount() throws NotFoundException {
        List<UserDto> userDtoList = userService.viewAllUsers();
        return APIResponse.okStatus(userDtoList);
    }

    @ApiOperation("View User Detail")
    @GetMapping("/{id}")
    public APIResponse<?> viewUserDetail(@PathVariable("id") Integer id) throws NotFoundException, IOException {
        UserDetailDto userDetailDto = userService.viewUserDetail(id);
        return APIResponse.okStatus(userDetailDto);
    }

    @ApiOperation("Create User")
    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody @Valid  RegisterRequestDto newUserRegisterInfo
            ) throws BadRequestException {
        RegisterRequestDto newRegisterRequest=userService.createUser(newUserRegisterInfo);

        return APIResponse.okStatus(newRegisterRequest);
    }

    @ApiOperation("Update User Image")
    @PutMapping(value = "/updateImage/{userId}", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<?> updateUserImage(@PathVariable("userId") Integer userId, @RequestParam(name = "image") MultipartFile multipartFile ) throws BadRequestException, NotFoundException, IOException {

        UpdateImageResponse updateImageResponse = null;

        if (!multipartFile.isEmpty()) {
            String image = StringUtils.cleanPath(multipartFile.getOriginalFilename());

//            String uploadDir = photoImagePaths + userId;
//
//            FileUploadUtil.cleanDir(uploadDir);
//
//            FileUploadUtil.saveFile(uploadDir, image, multipartFile);

            updateImageResponse = userService.updateUserImage(image, multipartFile.getBytes(), userId);


        }
        return APIResponse.okStatus(updateImageResponse);
    }

    @ApiOperation("Update User Info")
    @PutMapping(value = "/{userId}", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<?> updateUserDetail(@PathVariable("userId") Integer userId, @RequestBody @Valid RegisterRequestDto registerRequestDto) throws BadRequestException, NotFoundException {
        RegisterRequestDto newUserDetailDto=userService.updateUser(registerRequestDto,userId);
        return APIResponse.okStatus(newUserDetailDto);
    }

    @ApiOperation("Delete User")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Integer userId) throws NotFoundException, BadRequestException {
        userService.deleteUserById(userId);
        return APIResponse.okStatus();
    }


}
