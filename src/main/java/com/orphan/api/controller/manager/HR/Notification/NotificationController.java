package com.orphan.api.controller.manager.HR.Notification;


import com.google.gson.JsonObject;
import com.orphan.api.controller.admin.dto.UserDetailDto;
import com.orphan.common.request.SendMailDto;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.NotificationService;
import com.orphan.common.service.UserService;
import com.orphan.common.vo.PageInfo;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manager/notification")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER_HR')")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    @ApiOperation("Get Notifications By Pages")
    @GetMapping
    public APIResponse<?> viewNotificationsByPages(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
            , @ApiParam(value = "Limit", required = false) @RequestParam(value = "limit", required = false) Integer limit) throws NotFoundException {
        PageInfo<SendMailDto> sendMailDtoPageInfo;
        if (page != null) {
            sendMailDtoPageInfo = notificationService.viewMailNotifyByPage(page, limit);
        } else {
            sendMailDtoPageInfo = notificationService.viewMailNotifyByPage(1, limit);

        }
        return APIResponse.okStatus(sendMailDtoPageInfo);
    }

    @ApiOperation("View Notification Detail")
    @GetMapping("/{notificationId}")
    public APIResponse<?> viewNotificationDetail(
            @PathVariable("notificationId") Integer notificationId) throws NotFoundException {
        SendMailDto sendMailDto = notificationService.viewMailTrackingDetail(notificationId);
        return APIResponse.okStatus(sendMailDto);
    }

    @ApiOperation("View User Detail")
    @GetMapping("/user-info/{id}")
    public APIResponse<?> viewUserDetail(@PathVariable("id") Integer id)
            throws NotFoundException, IOException {
        UserDetailDto userDetailDto = userService.viewUserDetail(id);
        return APIResponse.okStatus(userDetailDto);
    }

    @ApiOperation("Create new Notification")
    @PostMapping
    public APIResponse<?> createNotification(@Valid @RequestBody SendMailDto sendMailDto,
            Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID,
                    messages.toString(), true);
        }
        notificationService.createMailTracking(sendMailDto);
        return APIResponse.okStatus();
    }
//    @ApiOperation("Delete children")
//    @DeleteMapping("/{childrenId}")
//    public APIResponse<?> deleteChildren(@PathVariable("childrenId") Integer childrenId) throws NotFoundException {
//        childrenService.deleteById(childrenId);
//        return APIResponse.okStatus();
//    }

}
