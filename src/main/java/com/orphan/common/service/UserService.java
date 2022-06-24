package com.orphan.common.service;

import com.orphan.api.controller.admin.dto.RoleDto;
import com.orphan.api.controller.admin.dto.UserDetailDto;
import com.orphan.api.controller.admin.dto.UserDto;
import com.orphan.api.controller.common.dto.EmailNotifyDto;
import com.orphan.api.controller.common.dto.PasswordDto;
import com.orphan.api.controller.common.dto.RegisterRequestDto;
import com.orphan.api.controller.common.dto.ResetPasswordDto;
import com.orphan.common.entity.Role;
import com.orphan.common.entity.User;
import com.orphan.common.repository.RoleRepository;
import com.orphan.common.repository.UserRepository;
import com.orphan.common.request.MailTemplate;
import com.orphan.common.response.StatisticsByDateResponse;
import com.orphan.common.response.StatisticsResponse;
import com.orphan.common.vo.PageInfo;
import com.orphan.config.EmailSenderService;
import com.orphan.enums.ERole;
import com.orphan.enums.UserStatus;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.APIConstants;
import com.orphan.utils.constants.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

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

    private static final String URL_CHANGE_PASSWORD_OPEN_WEB = "https://cyfcenter.herokuapp.com/changepassword";

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
    public RegisterRequestDto createUser(RegisterRequestDto registerRequestDto) throws BadRequestException, NotFoundException, MessagingException {
        User user = new User();

        //   validatePassword(registerRequestDto.getPassword(), registerRequestDto.getConfirmPassword());

        if (userRepository.existsByEmail(registerRequestDto.getEmail())) {
            throw new BadRequestException(BadRequestException.ERROR_EMAIL_ALREADY_EXIST,
                    this.messageService.buildMessages("error.msg.email-existed"));
        }

        if (userRepository.existsByIdentification(registerRequestDto.getIdentification())) {
            throw new BadRequestException(BadRequestException.ERROR_IDENTIFICATION_ALREADY_EXIST,
                    this.messageService.buildMessages("error.msg.identification-existed"));
        }

        user.setPassword(passwordEncoder.encode(Constants.passwordDefault));

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

        user.setUserStatus(UserStatus.ACTIVED.getCode());

        this.userRepository.save(user);

        registerRequestDto.setLoginId(user.getLoginId());


        EmailNotifyDto emailNotifyDto = new EmailNotifyDto();
        emailNotifyDto.setFullName(user.getFullName());
        emailNotifyDto.setEmail(user.getEmail());
        emailNotifyDto.setPassword(Constants.passwordDefault);
        createAccountMail(emailNotifyDto);

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

//
//        if (registerRequestDto.getPassword() != "") {
//            validatePassword(registerRequestDto.getPassword(), registerRequestDto.getConfirmPassword());
//            user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
//        }

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

    public UserDetailDto updateUserDetail(UserDetailDto userDetailDto, Integer userId) throws BadRequestException, NotFoundException {
        User user = findById(userId);

        if (!user.getEmail().equals(userDetailDto.getEmail())) {
            if (userRepository.existsByEmail(userDetailDto.getEmail())) {
                throw new BadRequestException(BadRequestException.ERROR_EMAIL_ALREADY_EXIST,
                        this.messageService.buildMessages("error.msg.email-existed"));
            }
            user.setEmail(userDetailDto.getEmail());
        }

        if (!user.getIdentification().equals(userDetailDto.getIdentification())) {
            if (userRepository.existsByIdentification(userDetailDto.getIdentification())) {
                throw new BadRequestException(BadRequestException.ERROR_IDENTIFICATION_ALREADY_EXIST,
                        this.messageService.buildMessages("error.msg.identification-existed"));
            }
            user.setIdentification(userDetailDto.getIdentification());
        }

//
//        if (registerRequestDto.getPassword() != "") {
//            validatePassword(registerRequestDto.getPassword(), registerRequestDto.getConfirmPassword());
//            user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
//        }

        if (userDetailDto.getImage() != "") {
            user.setImage(userDetailDto.getImage());
        }

        List<Role> roleList = new ArrayList<>();
        if (userDetailDto.getRoles().size() != 0) {
            userDetailDto.getRoles().forEach(role -> {
                switch (role.getRoleName()) {
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

        user.setFullName(userDetailDto.getFullName());

        user.setRoles(roleList);

        user.setPhone(userDetailDto.getPhone());

        user.setGender(userDetailDto.getGender());

        user.setDateOfBirth(OrphanUtils.StringToDate(userDetailDto.getDate_of_birth()));

        user.setAddress(userDetailDto.getAddress());

        user.setModifiedId(String.valueOf(getCurrentUserId()));

        this.userRepository.save(user);

        userDetailDto.setId(user.getLoginId());

        return userDetailDto;
    }

    //View By Page
    public PageInfo<UserDto> viewUsersDeletedByPage(Integer page, Integer limit, String status) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit, Sort.by("recoveryExpirationDate").ascending());
        Page<User> userPage = userRepository.findByUserDeleted(status, pageRequest);
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
    public List<UserDto> viewAllUsersDeleted(String status) throws NotFoundException {
        List<User> userList = userRepository.findByUserDeleted(status);
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

    public PageInfo<UserDto> viewUsersActivedByPage(Integer page, Integer limit, String status) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit, Sort.by("loginId").ascending());
        Page<User> userPage = userRepository.findByUserActived(status, pageRequest);
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
    public List<UserDto> viewAllUsersActived(String status) throws NotFoundException {
        List<User> userList = userRepository.findByUserActived(status);
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
        if (userRepository.existsByLoginIdAndUserStatusAllIgnoreCase(userId,
                UserStatus.DELETED.getCode())) {
            userRepository.deleteById(userId);
        } else {
            throw new NotFoundException(NotFoundException.ERROR_USER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR,
                            APIConstants.USER));
        }
    }

    public List<StatisticsResponse> countUsersByRole() {
        List<StatisticsResponse> statisticsResponses = userRepository.countUserByRole(UserStatus.ACTIVED.getCode());
        return statisticsResponses;
    }

    public List<StatisticsResponse> countUsersByGender() {
        List<StatisticsResponse> statisticsResponses = userRepository.countUserByGender(UserStatus.ACTIVED.getCode());
        return statisticsResponses;
    }

    public List<StatisticsByDateResponse> countUserOnBoardByMonth() {
        List<StatisticsByDateResponse> statisticsByDateRespons = userRepository.countUserOnBoardByMonth(UserStatus.ACTIVED.getCode());
        return statisticsByDateRespons;
    }

    public List<StatisticsByDateResponse> countUserOnBoardByYear() {
        List<StatisticsByDateResponse> statisticsByDateRespons = userRepository.countUserOnBoardByYear(UserStatus.ACTIVED.getCode());
        return statisticsByDateRespons;
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
            MailTemplate mailTemplate= new MailTemplate();
            mailTemplate.setSubject(subject);
            mailTemplate.setRecipients(Collections.singletonList(resetPasswordDto.getEmail()));
            mailTemplate.setBody(content);
            service.sendEmailWithAttachment(mailTemplate);
        } catch (Exception ex) {
            log.info("sendEmail error, error msg: {}", ex.getMessage());
        }
        return user.get();
    }

    public User createAccountMail(EmailNotifyDto emailNotifyDto) throws NotFoundException {

        Optional<User> user = this.userRepository.findByEmail(emailNotifyDto.getEmail());
        if (!user.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_USER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.USER));
        }
        try {
            String subject = "Orphan Management – Hi " + emailNotifyDto.getFullName();
            String content = "<p>Hello,</p>"
                    + "<p>Here is the password for your account at CYF Center. You can change it after logging in.</p>"
                    + "<p>To login please use the account information below</p>"
                    + "<br>"
                    + "<h2>Username: " + emailNotifyDto.getEmail()
                    + "<br>"
                    + "Password: " + emailNotifyDto.getPassword()
                    + "</h2>"
                    + "<br>"
                    + "<p>Thank you and regards," +
                    "<br>" +
                    "CYF team</p>";
            MailTemplate mailTemplate= new MailTemplate();
            mailTemplate.setSubject(subject);
            mailTemplate.setRecipients(Collections.singletonList(emailNotifyDto.getEmail()));
            mailTemplate.setBody(content);
            service.sendEmailWithAttachment(mailTemplate);
        } catch (Exception ex) {
            log.info("sendEmail error, error msg: {}", ex.getMessage());
        }
        return user.get();
    }

    //call send email change password api
    public void changePassWord(PasswordDto passwordDto, String email) throws
            NotFoundException, BadRequestException {
        User user = this.getUserByEmail(email).get();

        validatePassword(passwordDto.getNewPassword(), passwordDto.getConfirmPassword());

        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
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

        validatePassword(passwordDto.getNewPassword(), passwordDto.getConfirmPassword());

        user.get().setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user.get());
    }

    public void changePassWord(PasswordDto passwordDto, Integer userId) throws
            NotFoundException, BadRequestException {
        User user = this.getUserByLoginId(String.valueOf(userId)).get();

        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), user.getPassword())) {
            throw new BadRequestException(BadRequestException.ERROR_CHANGE_PASSWORD_BAD_REQUEST,
                    "Current Password does not match");
        }

        validatePassword(passwordDto.getNewPassword(), passwordDto.getConfirmPassword());

        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userRepository.save(user);
    }

    //delete auto
    public void updateStatusUser(Integer userId) throws NotFoundException {
        if (userRepository.existsByLoginId(userId)) {
            if (userRepository.findById(userId).get().getUserStatus().equals(UserStatus.ACTIVED.getCode())) {
                long now = (new Date()).getTime();
                long dateToMilliseconds = 60 * 60 * 1000;
                userRepository.updateUserStatusAndRecoveryExpirationDateByLoginId(UserStatus.DELETED.getCode(), new Date(now + 7 * 24 * dateToMilliseconds), userId);

            } else {
                userRepository.updateUserStatusAndRecoveryExpirationDateByLoginId(UserStatus.ACTIVED.getCode(), null, userId);
            }
        } else {
            throw new NotFoundException(NotFoundException.ERROR_USER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.USER));
        }
    }

    public void deleteAtExpirateDate(Date date, UserStatus userStatus) {
        userRepository.deleteByRecoveryExpirationDateAndUserStatus(new Date(new Date().getTime()), UserStatus.DELETED.getCode());
    }

    public PageInfo<UserDto> searchUser(String keyword, String status, Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit);
        Page<User> userPage = null;
        if (status.equals(UserStatus.ACTIVED.getCode())) {
            userPage = userRepository.searchUser(keyword, UserStatus.ACTIVED.getCode(), pageRequest);
        } else {
            userPage = userRepository.searchUser(keyword, UserStatus.DELETED.getCode(), pageRequest);
        }
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

    public PageInfo<UserDto> searchEmployee(String keyword, String status, Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit);
        Page<User> userPage = null;
        if (status.equals(UserStatus.ACTIVED.getCode())) {
            userPage = userRepository.searchEmployee(keyword, UserStatus.ACTIVED.getCode(), pageRequest);
        } else {
            userPage = userRepository.searchEmployee(keyword, UserStatus.DELETED.getCode(), pageRequest);
        }
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

    public List<StatisticsByDateResponse> countUserArchivedByMonth() {
        List<StatisticsByDateResponse> statisticsByDateRespons = userRepository.countUserArchivedByMonth();
        return statisticsByDateRespons;
    }

    public List<StatisticsByDateResponse> countUserArchivedByYear() {
        List<StatisticsByDateResponse> statisticsByDateRespons = userRepository.countUserArchivedByYear();
        return statisticsByDateRespons;
    }

    //mapper
    public RoleDto RoleToRoleDto(Role role) {
        RoleDto roleDto = new RoleDto();
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
        userDto.setPhone(user.getPhone());
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
        if (user.getRecoveryExpirationDate() != null) {
            userDetailDto.setRecoveryExpirationDate(OrphanUtils.DateToString(user.getRecoveryExpirationDate()));
        }
        userDetailDto.setUserStatus(user.getUserStatus());
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
