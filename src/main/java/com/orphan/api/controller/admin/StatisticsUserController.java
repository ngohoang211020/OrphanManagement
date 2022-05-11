package com.orphan.api.controller.admin;

import com.orphan.common.request.GenderRequest;
import com.orphan.common.request.RoleRequest;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.UserService;
import com.orphan.enums.UserStatus;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/user")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class StatisticsUserController {
    private final UserService userService;

    @ApiOperation("Count User By Role")
    @GetMapping("/role")
    public APIResponse<?> countUserByRole() {
        return APIResponse.okStatus(userService.countUsersByRole());
    }

    @ApiOperation("Count User By Gender")
    @GetMapping("/gender")
    public APIResponse<?> countUserByGender() {
        return APIResponse.okStatus(userService.countUsersByGender());
    }

    @ApiOperation("Count User Onboard By Month")
    @GetMapping("/onboard/month")
    public APIResponse<?> countUserOnboardByMonth()  {
        return APIResponse.okStatus(userService.countUserOnBoardByMonth());
    }

    @ApiOperation("Count User Onboard By Year")
    @GetMapping("/onboard/year")
    public APIResponse<?> countUserOnboardByYear()  {
        return APIResponse.okStatus(userService.countUserOnBoardByYear());
    }

}
