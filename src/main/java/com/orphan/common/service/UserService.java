package com.orphan.common.service;

import com.orphan.api.controller.admin.dto.RoleDto;
import com.orphan.api.controller.admin.dto.UserDetailDto;
import com.orphan.api.controller.admin.dto.UserDto;
import com.orphan.api.controller.common.dto.PasswordDto;
import com.orphan.api.controller.common.dto.RegisterRequestDto;
import com.orphan.api.controller.common.dto.ResetPasswordDto;
import com.orphan.common.entity.Role;
import com.orphan.common.entity.User;
import com.orphan.common.repository.RoleRepository;
import com.orphan.common.repository.UserRepository;
import com.orphan.common.vo.PageInfo;
import com.orphan.config.EmailSenderService;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService extends BaseService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailSenderService service;

    private final MessageService messageService;

    private static final String URL_CHANGE_PASSWORD_OPEN_WEB = "https://orphanmanagement.herokuapp.com/api/v1/auth/change-password";

    //findUserById
    public User findById(Integer userId) throws NotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_USER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.USER));
        }
        return user.get();
    }

    //Create Account
    public RegisterRequestDto createUser(RegisterRequestDto registerRequestDto) throws BadRequestException {
        User user = new User();

        validatePassword(registerRequestDto.getPassword(), registerRequestDto.getConfirmPassword());

        if (userRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new BadRequestException(BadRequestException.ERROR_EMAIL_ALREADY_EXIST,
                    this.messageService.buildMessages("error.msg.email-existed"));
        }

        if (userRepository.existsByIdentification(registerRequestDto.getIdentification())) {
            throw new BadRequestException(BadRequestException.ERROR_IDENTIFICATION_ALREADY_EXIST,
                    this.messageService.buildMessages("error.msg.identification-existed"));
        }

        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));

        List<Role> roleList = new ArrayList<>();
        if (registerRequestDto.getRoles().size() != 0) {
            registerRequestDto.getRoles().forEach(role -> {
                switch (role) {
                    case "ROLE_ADMIN":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN.getCode()).get();
                        roleList.add(adminRole);
                        break;
                    case "ROLE_MANAGER_CHILDREN":
                        Role childrenManager = roleRepository.findByName(ERole.ROLE_MANAGER_CHILDREN.getCode()).get();
                        roleList.add(childrenManager);
                        break;
                    case "ROLE_MANAGER_LOGISTIC":
                        Role logisticManager = roleRepository.findByName(ERole.ROLE_MANAGER_LOGISTIC.getCode()).get();
                        roleList.add(logisticManager);
                        break;
                    case "ROLE_MANAGER_HR":
                        Role hrManager = roleRepository.findByName(ERole.ROLE_MANAGER_HR.getCode()).get();
                        roleList.add(hrManager);
                        break;
                    case "ROLE_EMPLOYEE":
                        Role employee = roleRepository.findByName(ERole.ROLE_EMPLOYEE.getCode()).get();
                        roleList.add(employee);
                        break;
                }
            });
        } else {
            Role userRole = roleRepository.findByName(ERole.ROLE_EMPLOYEE.getCode()).get();
            roleList.add(userRole);
        }

        user.setFullName(registerRequestDto.getFullName());

        user.setEmail(registerRequestDto.getEmail());

        user.setRoles(roleList);

        user.setPhone(registerRequestDto.getPhone());

        user.setGender(registerRequestDto.getGender());

        user.setIdentification(registerRequestDto.getIdentification());

        user.setImage(registerRequestDto.getImage());

        user.setDateOfBirth(OrphanUtils.StringToDate(registerRequestDto.getDate_of_birth()));

        user.setAddress(registerRequestDto.getAddress());

        user.setCreatedId(String.valueOf(getCurrentUserId()));

        this.userRepository.save(user);

        registerRequestDto.setLoginId(user.getLoginId());

        return registerRequestDto;
    }

    //Update Account
    public RegisterRequestDto updateUser(RegisterRequestDto registerRequestDto, Integer userId) throws BadRequestException, NotFoundException {
        User user = findById(userId);

        if (!user.getEmail().equals(registerRequestDto.getEmail())) {
            if (userRepository.existsByEmail(registerRequestDto.getEmail())) {
                throw new BadRequestException(BadRequestException.ERROR_EMAIL_ALREADY_EXIST,
                        this.messageService.buildMessages("error.msg.email-existed"));
            }
            user.setEmail(registerRequestDto.getEmail());
        }

        if (!user.getIdentification().equals(registerRequestDto.getIdentification())) {
            if (userRepository.existsByIdentification(registerRequestDto.getIdentification())) {
                throw new BadRequestException(BadRequestException.ERROR_IDENTIFICATION_ALREADY_EXIST,
                        this.messageService.buildMessages("error.msg.identification-existed"));
            }
            user.setIdentification(registerRequestDto.getIdentification());
        }


        if (registerRequestDto.getPassword() != "") {
            validatePassword(registerRequestDto.getPassword(), registerRequestDto.getConfirmPassword());
            user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
        }

        if (registerRequestDto.getImage() != "") {
            user.setImage(registerRequestDto.getImage());
        }

        List<Role> roleList = new ArrayList<>();
        if (registerRequestDto.getRoles().size() != 0) {
            registerRequestDto.getRoles().forEach(role -> {
                switch (role) {
                    case "ROLE_ADMIN":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN.getCode()).get();
                        roleList.add(adminRole);
                        break;
                    case "ROLE_MANAGER_CHILDREN":
                        Role childrenManager = roleRepository.findByName(ERole.ROLE_MANAGER_CHILDREN.getCode()).get();
                        roleList.add(childrenManager);
                        break;
                    case "ROLE_MANAGER_LOGISTIC":
                        Role logisticManager = roleRepository.findByName(ERole.ROLE_MANAGER_LOGISTIC.getCode()).get();
                        roleList.add(logisticManager);
                        break;
                    case "ROLE_MANAGER_HR":
                        Role hrManager = roleRepository.findByName(ERole.ROLE_MANAGER_HR.getCode()).get();
                        roleList.add(hrManager);
                        break;
                    case "ROLE_EMPLOYEE":
                        Role employee = roleRepository.findByName(ERole.ROLE_EMPLOYEE.getCode()).get();
                        roleList.add(employee);
                        break;
                }
            });
        } else {
            Role userRole = roleRepository.findByName(ERole.ROLE_EMPLOYEE.getCode()).get();
            roleList.add(userRole);
        }

        user.setFullName(registerRequestDto.getFullName());

        user.setRoles(roleList);

        user.setPhone(registerRequestDto.getPhone());

        user.setGender(registerRequestDto.getGender());

        user.setDateOfBirth(OrphanUtils.StringToDate(registerRequestDto.getDate_of_birth()));

        user.setAddress(registerRequestDto.getAddress());

        user.setModifiedId(String.valueOf(getCurrentUserId()));

        this.userRepository.save(user);

        registerRequestDto.setLoginId(user.getLoginId());

        return registerRequestDto;
    }

    //View By Page
    public PageInfo<UserDto> viewUsersByPage(Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit);
        Page<User> userPage = userRepository.findByOrderByFullNameAsc(pageRequest);
        if (userPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_USER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.USER));
        }
        List<UserDto> userDtoList = userPage.getContent().stream().map(user -> {
            try {
                return UserToUserDto(user);
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

    //View All
    public List<UserDto> viewAllUsers() throws NotFoundException {
        List<User> userList = userRepository.findAll();
        if (userList.isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_USER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.USER));
        }
        List<UserDto> userDtoList = userList.stream().map(user -> {
            try {
                return UserToUserDto(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        return userDtoList;
    }

    //View detail by id
    public UserDetailDto viewUserDetail(Integer userId) throws NotFoundException, IOException {
        UserDetailDto userDetailDto = UserToUserDetailDto(findById(userId));
        return userDetailDto;
    }

    //delete by id
    public void deleteUserById(Integer userId) throws NotFoundException {
        if (userRepository.existsByLoginId(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new NotFoundException(NotFoundException.ERROR_USER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.USER));
        }
    }

    //send api to email need change password
    public User resetPassword(ResetPasswordDto resetPasswordDto) throws NotFoundException {
        Optional<User> user = this.userRepository.findByEmail(resetPasswordDto.getEmail());
        if (!user.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_USER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.USER));
        }


        try {
            String resetPasswordLink = URL_CHANGE_PASSWORD_OPEN_WEB + "?email=" + resetPasswordDto.getEmail();
            String subject = "Orphan Management – Reset your password";
            Context context = new Context();
            Map<String, Object> model = new HashMap<>();
            model.put("link", resetPasswordLink);
            context.setVariables(model);
            String content = "<p>Hello,</p>"
                    + "<p>You have requested to reset your password.</p>"
                    + "<p>Click the link below to change your password:</p>"
                    + "<p><a href=\"" + resetPasswordLink + "\">Change my password</a></p>"
                    + "<br>"
                    + "<p>Ignore this email if you do remember your password, "
                    + "or you have not made the request.</p>";
            service.sendEmailWithAttachment(resetPasswordDto.getEmail(), content, subject);
        } catch (Exception ex) {
            log.info("sendEmail error, error msg: {}", ex.getMessage());
        }
        return user.get();
    }

    //call send email change password api
    public void changePassWord(PasswordDto passwordDto, String email) throws
            NotFoundException, BadRequestException {
        User user = this.getUserByEmail(email).get();

        validatePassword(passwordDto.getNewPassWord(), passwordDto.getConfirmPassWord());

        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassWord()));
        userRepository.save(user);
    }

    //change password
    public void changePassWord(PasswordDto passwordDto) throws
            NotFoundException, BadRequestException {
        Optional<User> user = this.getUserByLoginId(String.valueOf(getCurrentUserId()));
        if (!user.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_USER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.USER));
        }

        validatePassword(passwordDto.getNewPassWord(), passwordDto.getConfirmPassWord());

        user.get().setPassword(passwordEncoder.encode(passwordDto.getNewPassWord()));
        userRepository.save(user.get());
    }

    //-------------------------Employee Service---------------------

    //mapper

    public Role StringToRole(String role) {
        Optional<Role> oRole;
        if (role.equals(ERole.ROLE_ADMIN.getCode())) {
            oRole = roleRepository.findByName(ERole.ROLE_ADMIN.getCode());
        } else if (role.equals(ERole.ROLE_MANAGER_HR.getCode())) {
            oRole = roleRepository.findByName(ERole.ROLE_MANAGER_HR.getCode());
        } else if (role.equals(ERole.ROLE_MANAGER_LOGISTIC.getCode())) {
            oRole = roleRepository.findByName(ERole.ROLE_MANAGER_LOGISTIC.getCode());

        } else if (role.equals(ERole.ROLE_MANAGER_CHILDREN.getCode())) {
            oRole = roleRepository.findByName(ERole.ROLE_MANAGER_CHILDREN.getCode());
        } else {
            oRole = roleRepository.findByName(ERole.ROLE_EMPLOYEE.getCode());
        }
        return oRole.get();
    }

    public RoleDto RoleToRoleDto(Role role){
        RoleDto roleDto=new RoleDto();
        roleDto.setRoleName(role.getName());
        roleDto.setRoleId(role.getId());
        roleDto.setDescription(role.getDescription());
        return roleDto;
    }
    public UserDto UserToUserDto(User user) throws IOException {

        UserDto userDto = new UserDto();
        if (user.getImage() != "") {
            userDto.setImage(user.getImage());
        }
        userDto.setId(user.getLoginId());
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setRoles(user.getRoles().stream()
                .map(role -> RoleToRoleDto(role))
                .collect(Collectors.toList()));
        return userDto;
    }

    public UserDetailDto UserToUserDetailDto(User user) throws IOException {

        UserDetailDto userDetailDto = new UserDetailDto();
        userDetailDto.setId(user.getLoginId());
        userDetailDto.setEmail(user.getEmail());
        userDetailDto.setFullName(user.getFullName());
        userDetailDto.setRoles(user.getRoles().stream()
                .map(role -> RoleToRoleDto(role))
                .collect(Collectors.toList()));
        userDetailDto.setImage(user.getImage());
        userDetailDto.setAddress(user.getAddress());
        userDetailDto.setGender(user.getGender());
        userDetailDto.setDate_of_birth(OrphanUtils.DateToString(user.getDateOfBirth()));
        userDetailDto.setIdentification(user.getIdentification());
        userDetailDto.setPhone(user.getPhone());
        return userDetailDto;
    }


    public void validatePassword(String password, String confirmPassword) throws BadRequestException {
        if (!OrphanUtils.isPassword(password)) {
            throw new BadRequestException(BadRequestException.PASSWORD_IS_INVALID,
                    this.messageService.buildMessages("error.msg.password-invalid"));
        }

        if (!password.equals(confirmPassword)) {
            throw new BadRequestException(BadRequestException.ERROR_PASSWORD_NOT_MATCH_CONFIRM_PASSWORD,
                    APIConstants.ERROR_NEW_PASSWORD_NOT_MATCH_CONFIRM_PASSWORD, false);
        }

    }
}
