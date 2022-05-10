package com.orphan.api.controller.manager.HR.Employee;

import com.google.gson.JsonObject;
import com.orphan.api.controller.admin.dto.UserDetailDto;
import com.orphan.api.controller.admin.dto.UserDto;
import com.orphan.api.controller.manager.HR.Employee.dto.EmployeeRequest;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.EmployeeService;
import com.orphan.common.service.UserService;
import com.orphan.common.vo.PageInfo;
import com.orphan.enums.ERole;
import com.orphan.enums.UserStatus;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.PageableConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/manager/employee")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER_HR')")
@RequiredArgsConstructor
public class EmployeeController {
    private final UserService userService;

    private final EmployeeService employeeService;

//    @ApiOperation("Get All Employees")
//    @GetMapping("/all")
//    public APIResponse<?> viewAllEmployees() throws NotFoundException {
//        return APIResponse.okStatus(employeeService.viewAllEmployee());
//    }
//
//    @ApiOperation("Get Employee By Pages")
//    @GetMapping
//    public APIResponse<?> viewStaffsByPage(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
//    ) throws NotFoundException {
//        PageInfo<UserDto> employeePageInfo;
//        if (page != null) {
//            employeePageInfo = employeeService.viewUsersByPage(page, PageableConstants.limit);
//        } else {
//            employeePageInfo = employeeService.viewUsersByPage(1, PageableConstants.limit);
//
//        }
//        return APIResponse.okStatus(employeePageInfo);
//    }

    @ApiOperation("View Employee Detail")
    @GetMapping("/{emplyeeId}")
    public APIResponse<?> viewEmployeeDetail(@PathVariable("emplyeeId") Integer emplyeeId) throws NotFoundException, IOException {
        UserDetailDto userDetailDto = userService.viewUserDetail(emplyeeId);
        return APIResponse.okStatus(userDetailDto);
    }

    @ApiOperation("Create new Employee")
    @PostMapping
    public APIResponse<?> createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest, Errors errors) throws BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        employeeRequest = employeeService.createEmployee(employeeRequest);
        return APIResponse.okStatus(employeeRequest);
    }

    @ApiOperation("Update employee detail")
    @PutMapping("/{employeeId}")
    public APIResponse<?> updateEmployee(@PathVariable("employeeId") Integer employeeId, @Valid @RequestBody EmployeeRequest employeeRequest, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        employeeRequest = employeeService.updateEmployee(employeeRequest, employeeId);
        return APIResponse.okStatus(employeeRequest);
    }

    @ApiOperation("Delete employee")
    @DeleteMapping("/{employeeId}")
    public APIResponse<?> deleteEmployee(@PathVariable("employeeId") Integer employeeId) throws NotFoundException {
        userService.deleteUserById(employeeId);
        return APIResponse.okStatus();
    }

    @ApiOperation("Get All Employees Actived")
    @GetMapping("/all")
    public APIResponse<?> viewAllEmployeesActived() throws NotFoundException {
        return APIResponse.okStatus(employeeService.viewAllEmployeeByStatusACTIVED(ERole.ROLE_EMPLOYEE.getCode(), UserStatus.ACTIVED.getCode()));
    }
    @ApiOperation("Get All Employees Deleted")
    @GetMapping("/all/deleted")
    public APIResponse<?> viewAllEmployeesDeleted() throws NotFoundException {
        return APIResponse.okStatus(employeeService.viewAllEmployeeByStatusDELETED(ERole.ROLE_EMPLOYEE.getCode(), UserStatus.DELETED.getCode()));
    }
    @ApiOperation("Get Employee ACTIVED By Pages")
    @GetMapping()
    public APIResponse<?> viewEmployeesActivedByPage(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
    ) throws NotFoundException {
        PageInfo<UserDto> employeePageInfo;
        if (page != null) {
            employeePageInfo = employeeService.viewUsersByPageByStatusACTIVED(page, PageableConstants.limit,ERole.ROLE_EMPLOYEE.getCode(), UserStatus.ACTIVED.getCode());
        } else {
            employeePageInfo = employeeService.viewUsersByPageByStatusACTIVED(1, PageableConstants.limit,ERole.ROLE_EMPLOYEE.getCode(), UserStatus.ACTIVED.getCode());

        }
        return APIResponse.okStatus(employeePageInfo);
    }

    @ApiOperation("Get Employee DELETED By Pages")
    @GetMapping("/deleted")
    public APIResponse<?> viewEmployeeDeletedsByPage(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
    ) throws NotFoundException {
        PageInfo<UserDto> employeePageInfo;
        if (page != null) {
            employeePageInfo = employeeService.viewUsersByPageByStatusDELETED(page, PageableConstants.limit,ERole.ROLE_EMPLOYEE.getCode(), UserStatus.DELETED.getCode());
        } else {
            employeePageInfo = employeeService.viewUsersByPageByStatusDELETED(1, PageableConstants.limit,ERole.ROLE_EMPLOYEE.getCode(), UserStatus.DELETED.getCode());

        }
        return APIResponse.okStatus(employeePageInfo);
    }
}
