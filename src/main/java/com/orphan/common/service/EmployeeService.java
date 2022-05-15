package com.orphan.common.service;

import com.orphan.api.controller.admin.dto.UserDto;
import com.orphan.api.controller.common.dto.EmailNotifyDto;
import com.orphan.api.controller.manager.HR.Employee.dto.AccountRequest;
import com.orphan.common.entity.Role;
import com.orphan.common.entity.User;
import com.orphan.common.repository.RoleRepository;
import com.orphan.common.repository.UserRepository;
import com.orphan.common.vo.PageInfo;
import com.orphan.enums.ERole;
import com.orphan.enums.UserStatus;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.APIConstants;
import com.orphan.utils.constants.Constants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService extends BaseService {
    private final MessageService messageService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public List<UserDto> viewAllEmployeeByStatusDELETED(String role,String status) {
        List<User> employeeList = userRepository.findByRoleAndStatusDELETED(role,status);
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
    public PageInfo<UserDto> viewUsersByPageByStatusDELETED(Integer page, Integer limit,String role,String status) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit);
        Page<User> userPage = userRepository.findByRoleAndStatusDELETED(role,status, pageRequest);
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

    public List<UserDto> viewAllEmployeeByStatusACTIVED(String role,String status) {
        List<User> employeeList = userRepository.findByRoleAndStatus(role,status);
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
    public PageInfo<UserDto> viewUsersByPageByStatusACTIVED(Integer page, Integer limit,String role,String status) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit);
        Page<User> userPage = userRepository.findByRoleAndStatus(role,status, pageRequest);
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
//    public List<UserDto> viewAllEmployee() {
//        List<User> employeeList = userRepository.findByRoles_Name(ERole.ROLE_EMPLOYEE.getCode());
//        if (employeeList.isEmpty()) {
//            return null;
//        }
//        List<UserDto> employeeDtoList = employeeList.stream().map(employee -> {
//            try {
//                return userService.UserToUserDto(employee);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }).collect(Collectors.toList());
//        return employeeDtoList;
//    }

    //View By Page
//    public PageInfo<UserDto> viewUsersByPage(Integer page, Integer limit) throws NotFoundException {
//        PageRequest pageRequest = buildPageRequest(page, limit);
//        Page<User> userPage = userRepository.findByRoles_NameOrderByCreatedAtAsc(ERole.ROLE_EMPLOYEE.getCode(), pageRequest);
//        if (userPage.getContent().isEmpty()) {
//            throw new NotFoundException(NotFoundException.ERROR_USER_NOT_FOUND,
//                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.USER));
//        }
//        List<UserDto> userDtoList = userPage.getContent().stream().map(user -> {
//            try {
//                return userService.UserToUserDto(user);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }).collect(Collectors.toList());
//        PageInfo<UserDto> userDtoPageInfo = new PageInfo<>();
//        userDtoPageInfo.setPage(page);
//        userDtoPageInfo.setLimit(limit);
//        userDtoPageInfo.setResult(userDtoList);
//        userDtoPageInfo.setTotal(userPage.getTotalElements());
//        userDtoPageInfo.setPages(userPage.getTotalPages());
//        return userDtoPageInfo;
//    }

    //Create Employee
    public AccountRequest createEmployee(AccountRequest accountRequest) throws BadRequestException, NotFoundException {
        User user = new User();

        if (userRepository.existsByEmail(accountRequest.getEmail())) {
            throw new BadRequestException(BadRequestException.ERROR_EMAIL_ALREADY_EXIST,
                    this.messageService.buildMessages("error.msg.email-existed"));
        }

        if (userRepository.existsByIdentification(accountRequest.getIdentification())) {
            throw new BadRequestException(BadRequestException.ERROR_IDENTIFICATION_ALREADY_EXIST,
                    this.messageService.buildMessages("error.msg.identification-existed"));
        }

        List<Role> roleList = new ArrayList<>();

        Role employee = roleRepository.findByName(ERole.ROLE_EMPLOYEE.getCode()).get();
        roleList.add(employee);


        user.setFullName(accountRequest.getFullName());

        user.setEmail(accountRequest.getEmail());

        user.setRoles(roleList);

        user.setPhone(accountRequest.getPhone());

        user.setGender(accountRequest.getGender());

        user.setIdentification(accountRequest.getIdentification());

        user.setPassword(passwordEncoder.encode("12345678@X"));

        if (accountRequest.getImage() != null && accountRequest.getImage() != "") {
            user.setImage(accountRequest.getImage());
        }
        user.setDateOfBirth(OrphanUtils.StringToDate(accountRequest.getDateOfBirth()));

        user.setAddress(accountRequest.getAddress());

        user.setCreatedId(String.valueOf(getCurrentUserId()));

        user.setUserStatus(UserStatus.ACTIVED.getCode());

        this.userRepository.save(user);

        accountRequest.setId(user.getLoginId());

        EmailNotifyDto emailNotifyDto = new EmailNotifyDto();
        emailNotifyDto.setFullName(user.getFullName());
        emailNotifyDto.setEmail(user.getEmail());
        emailNotifyDto.setPassword(Constants.passwordDefault);
        userService.createAccountMail(emailNotifyDto);
        return accountRequest;
    }

    //Update employee
    public AccountRequest updateEmployee(AccountRequest accountRequest, Integer userId) throws BadRequestException, NotFoundException {
        User user = userService.findById(userId);

        if (!user.getEmail().equals(accountRequest.getEmail())) {
            if (userRepository.existsByEmail(accountRequest.getEmail())) {
                throw new BadRequestException(BadRequestException.ERROR_EMAIL_ALREADY_EXIST,
                        this.messageService.buildMessages("error.msg.email-existed"));
            }
            user.setEmail(accountRequest.getEmail());
        }

        if (!user.getIdentification().equals(accountRequest.getIdentification())) {
            if (userRepository.existsByIdentification(accountRequest.getIdentification())) {
                throw new BadRequestException(BadRequestException.ERROR_IDENTIFICATION_ALREADY_EXIST,
                        this.messageService.buildMessages("error.msg.identification-existed"));
            }
            user.setIdentification(accountRequest.getIdentification());
        }

        if (accountRequest.getImage() != ""&& accountRequest.getImage()!=null) {
            user.setImage(accountRequest.getImage());
        }


        List<Role> roleList = new ArrayList<>();

        Role employee = roleRepository.findByName(ERole.ROLE_EMPLOYEE.getCode()).get();
        roleList.add(employee);


        user.setFullName(accountRequest.getFullName());

        user.setEmail(accountRequest.getEmail());

        user.setRoles(roleList);

        user.setPhone(accountRequest.getPhone());

        user.setGender(accountRequest.getGender());

        user.setIdentification(accountRequest.getIdentification());

        user.setImage(accountRequest.getImage());

        user.setDateOfBirth(OrphanUtils.StringToDate(accountRequest.getDateOfBirth()));

        user.setAddress(accountRequest.getAddress());

        user.setModifiedId(String.valueOf(getCurrentUserId()));

        this.userRepository.save(user);

        accountRequest.setId(userId);

        return accountRequest;
    }




}
