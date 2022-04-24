package com.orphan.common.service;

import com.orphan.api.controller.admin.dto.UserDto;
import com.orphan.api.controller.manager.HR.Employee.dto.EmployeeRequest;
import com.orphan.common.entity.Role;
import com.orphan.common.entity.User;
import com.orphan.common.repository.RoleRepository;
import com.orphan.common.repository.UserRepository;
import com.orphan.common.vo.PageInfo;
import com.orphan.enums.ERole;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.APIConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService extends BaseService{
    private final MessageService messageService;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public List<UserDto> viewAllEmployee(){
        List<User> employeeList = userRepository.findByRoles_Name(ERole.ROLE_EMPLOYEE.getCode());
        if (employeeList.isEmpty()) {
            return null;
        }
        List<UserDto> employeeDtoList = employeeList.stream().map(employee -> {
            try {
                return userService.UserToUserDto(employee);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        return employeeDtoList;
    }

    //View By Page
    public PageInfo<UserDto> viewUsersByPage(Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit);
        Page<User> userPage = userRepository.findByRoles_NameOrderByFullNameAsc(ERole.ROLE_EMPLOYEE.getCode(),pageRequest);
        if (userPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_USER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.USER));
        }
        List<UserDto> userDtoList = userPage.getContent().stream().map(user -> {
            try {
                return userService.UserToUserDto(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        PageInfo<UserDto> userDtoPageInfo = new PageInfo<>();
        userDtoPageInfo.setPage(page);
        userDtoPageInfo.setLimit(limit);
        userDtoPageInfo.setResult(userDtoList);
        userDtoPageInfo.setTotal(userPage.getTotalElements());
        userDtoPageInfo.setPages(userPage.getTotalPages());
        return userDtoPageInfo;
    }

    //Create Employee
    public EmployeeRequest createEmployee(EmployeeRequest employeeRequest) throws BadRequestException {
        User user = new User();

        if (userRepository.existsByEmail(employeeRequest.getEmail())) {
            throw new BadRequestException(BadRequestException.ERROR_EMAIL_ALREADY_EXIST,
                    this.messageService.buildMessages("error.msg.email-existed"));
        }

        if (userRepository.existsByIdentification(employeeRequest.getIdentification())) {
            throw new BadRequestException(BadRequestException.ERROR_IDENTIFICATION_ALREADY_EXIST,
                    this.messageService.buildMessages("error.msg.identification-existed"));
        }

        List<Role> roleList = new ArrayList<>();

        Role employee = roleRepository.findByName(ERole.ROLE_EMPLOYEE.getCode()).get();
        roleList.add(employee);


        user.setFullName(employeeRequest.getFullName());

        user.setEmail(employeeRequest.getEmail());

        user.setRoles(roleList);

        user.setPhone(employeeRequest.getPhone());

        user.setGender(employeeRequest.getGender());

        user.setIdentification(employeeRequest.getIdentification());

        user.setImage(employeeRequest.getImage());

        user.setDateOfBirth(OrphanUtils.StringToDate(employeeRequest.getDate_of_birth()));

        user.setAddress(employeeRequest.getAddress());

        user.setCreatedId(String.valueOf(getCurrentUserId()));

        this.userRepository.save(user);

        employeeRequest.setEmployeeId(user.getLoginId());

        return employeeRequest;
    }

    //Update employee
    public EmployeeRequest updateEmployee(EmployeeRequest employeeRequest, Integer userId) throws BadRequestException, NotFoundException {
        User user = userService.findById(userId);

        if (!user.getEmail().equals(employeeRequest.getEmail())) {
            if (userRepository.existsByEmail(employeeRequest.getEmail())) {
                throw new BadRequestException(BadRequestException.ERROR_EMAIL_ALREADY_EXIST,
                        this.messageService.buildMessages("error.msg.email-existed"));
            }
            user.setEmail(employeeRequest.getEmail());
        }

        if (!user.getIdentification().equals(employeeRequest.getIdentification())) {
            if (userRepository.existsByIdentification(employeeRequest.getIdentification())) {
                throw new BadRequestException(BadRequestException.ERROR_IDENTIFICATION_ALREADY_EXIST,
                        this.messageService.buildMessages("error.msg.identification-existed"));
            }
            user.setIdentification(employeeRequest.getIdentification());
        }

        if (employeeRequest.getImage() != "") {
            user.setImage(employeeRequest.getImage());
        }


            List<Role> roleList = new ArrayList<>();

            Role employee = roleRepository.findByName(ERole.ROLE_EMPLOYEE.getCode()).get();
            roleList.add(employee);


            user.setFullName(employeeRequest.getFullName());

            user.setEmail(employeeRequest.getEmail());

            user.setRoles(roleList);

            user.setPhone(employeeRequest.getPhone());

            user.setGender(employeeRequest.getGender());

            user.setIdentification(employeeRequest.getIdentification());

            user.setImage(employeeRequest.getImage());

            user.setDateOfBirth(OrphanUtils.StringToDate(employeeRequest.getDate_of_birth()));

            user.setAddress(employeeRequest.getAddress());

            user.setModifiedId(String.valueOf(getCurrentUserId()));

            this.userRepository.save(user);

            employeeRequest.setEmployeeId(userId);

            return employeeRequest;
    }

}
