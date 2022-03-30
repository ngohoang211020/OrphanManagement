package com.orphan.common.service;

import com.orphan.api.controller.admin.dto.UpdateImageResponse;
import com.orphan.api.controller.admin.dto.UserDetailDto;
import com.orphan.api.controller.admin.dto.UserDto;
import com.orphan.api.controller.common.dto.RegisterRequestDto;
import com.orphan.common.entity.Role;
import com.orphan.common.entity.User;
import com.orphan.common.repository.RoleRepository;
import com.orphan.common.repository.UserRepository;
import com.orphan.enums.ERole;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.APIConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService extends BaseService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final MessageService messageService;

    public User findById(Integer userId) throws NotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_USER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.USER));
        }
        return user.get();
    }

    /**
     * Register individual
     *
     * @param registerRequestDto
     * @return String
     */


    public RegisterRequestDto createUser(RegisterRequestDto registerRequestDto) throws BadRequestException {
        User user = new User();

        validateEmail_Phone_Identification(registerRequestDto.getEmail(), registerRequestDto.getPhone(), registerRequestDto.getIdentification());

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
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
                        roleList.add(adminRole);
                        break;
                    case "manager":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MANAGER).get();
                        roleList.add(modRole);
                        break;
                }
            });
        } else {
            Role userRole = roleRepository.findByName(ERole.ROLE_MANAGER).get();
            roleList.add(userRole);
        }

        user.setFullName(registerRequestDto.getFullName());

        user.setEmail(registerRequestDto.getEmail());

        user.setRoles(roleList);

        user.setPhone(registerRequestDto.getPhone());

        user.setGender(registerRequestDto.getGender());

        user.setIdentification(registerRequestDto.getIdentification());

        user.setDate_of_birth(OrphanUtils.StringToDate(registerRequestDto.getDate_of_birth()));

        user.setAddress(registerRequestDto.getAddress());

        this.userRepository.save(user);

        registerRequestDto.setLoginId(user.getLoginId());

        return registerRequestDto;
    }

    public RegisterRequestDto updateUser(RegisterRequestDto registerRequestDto, Integer userId) throws BadRequestException, NotFoundException {
        User user = findById(userId);

        validateEmail_Phone_Identification(registerRequestDto.getEmail(), registerRequestDto.getPhone(), registerRequestDto.getIdentification());


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

        if (user != null) {
            if (!registerRequestDto.getPassword().isEmpty() || registerRequestDto.getPassword() != "") {
                validatePassword(registerRequestDto.getPassword(), registerRequestDto.getConfirmPassword());
                user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
            }
        }

        List<Role> roleList = new ArrayList<>();
        if (registerRequestDto.getRoles().size() != 0) {
            registerRequestDto.getRoles().forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
                        roleList.add(adminRole);
                        break;
                    case "manager":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MANAGER).get();
                        roleList.add(modRole);
                        break;
                }
            });
        } else {
            Role userRole = roleRepository.findByName(ERole.ROLE_MANAGER).get();
            roleList.add(userRole);
        }

        user.setFullName(registerRequestDto.getFullName());

        user.setRoles(roleList);

        user.setPhone(registerRequestDto.getPhone());

        user.setGender(registerRequestDto.getGender());

        user.setDate_of_birth(OrphanUtils.StringToDate(registerRequestDto.getDate_of_birth()));

        user.setAddress(registerRequestDto.getAddress());

        this.userRepository.save(user);

        registerRequestDto.setLoginId(user.getLoginId());

        return registerRequestDto;
    }

    public List<UserDto> viewAllUsers() throws NotFoundException {
            List<User> userList = userRepository.findAll(Sort.by("fullName").ascending());
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

    public UserDetailDto viewUserDetail(Integer userId) throws NotFoundException, IOException {
        UserDetailDto userDetailDto = UserToUserDetailDto(findById(userId));
        return userDetailDto;
    }

    public void deleteUserById(Integer userId) throws NotFoundException {
        if (userRepository.existsByLoginId(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new NotFoundException(NotFoundException.ERROR_USER_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.USER));
        }
    }

    public void validateEmail_Phone_Identification(String email, String phone, String identification) throws BadRequestException {
        if (!OrphanUtils.isEmailValid(email)) {
            throw new BadRequestException(BadRequestException.EMAIL_IS_INVALID,
                    this.messageService.buildMessages("error.msg.email-invalid"));
        }

        if (!OrphanUtils.isPhoneNumber(phone)) {
            throw new BadRequestException(BadRequestException.PHONE_NUMBER_IS_INVALID,
                    this.messageService.buildMessages("error.msg.phone-number-invalid"));
        }
        if (!OrphanUtils.isIdentification(identification)) {
            throw new BadRequestException(BadRequestException.IDENTIFICATION_IS_INVALID,
                    this.messageService.buildMessages("error.msg.identification-number-invalid"));
        }

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

    public UpdateImageResponse updateUserImage(String image,byte[] procPic,Integer userId) throws IOException {
        userRepository.updateUserImage(image,procPic,userId);
//        ClassPathResource imageFile = new ClassPathResource("/user-photos/" + userId + "/" + image);
//
//        byte[] imageBytes = StreamUtils.copyToByteArray(imageFile.getInputStream());

        UpdateImageResponse updateImageResponse=new UpdateImageResponse();
        updateImageResponse.setImage(image);
        updateImageResponse.setUserId(userId);
        updateImageResponse.setImageFile(procPic);
        return  updateImageResponse;
    }
    //mapper
    public Role StringToRole(String role) {
        Optional<Role> oRole;
        if (role.equals(ERole.ROLE_MANAGER)) {
            oRole = roleRepository.findByName(ERole.ROLE_MANAGER);
        } else {
            oRole = roleRepository.findByName(ERole.ROLE_ADMIN);
        }
        return oRole.get();
    }

    public UserDto UserToUserDto(User user) throws IOException {

        UserDto userDto = new UserDto();
        if(user.getImage()!=""||!user.getImage().isEmpty()){
//            ClassPathResource imageFile = new ClassPathResource(user.getPhotosImagePath());
//
//            byte[] imageBytes = StreamUtils.copyToByteArray(imageFile.getInputStream());
            userDto.setImageFile(user.getProfPic());
            userDto.setImage(user.getImage());
        }
        userDto.setId(user.getLoginId());
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setRoles(user.getRoles().stream()
                .map(role -> role.getName().toString())
                .collect(Collectors.toList()));
        return userDto;
    }

    public UserDetailDto UserToUserDetailDto(User user) throws IOException {



        UserDetailDto userDetailDto = new UserDetailDto();
        userDetailDto.setId(user.getLoginId());
        userDetailDto.setEmail(user.getEmail());
        userDetailDto.setFullName(user.getFullName());
        userDetailDto.setRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));
        if(user.getImage()!=""||!user.getImage().isEmpty()){

            userDetailDto.setImageFile(user.getProfPic());
            userDetailDto.setImage(user.getImage());
        }

        userDetailDto.setAddress(user.getAddress());
        userDetailDto.setGender(user.getGender());
        userDetailDto.setDate_of_birth(OrphanUtils.DateToString(user.getDate_of_birth()));
        userDetailDto.setIdentification(user.getIdentification());
        userDetailDto.setPhone(user.getPhone());
        return userDetailDto;
    }

    public User UserDetailDtoToUser(UserDetailDto userDetailDto) {
        User user = new User();
        user.setLoginId(userDetailDto.getId());
        user.setEmail(userDetailDto.getEmail());
        user.setFullName(userDetailDto.getFullName());
        user.setRoles(userDetailDto.getRoles().stream()
                .map(role -> StringToRole(role))
                .collect(Collectors.toList()));
        user.setImage(userDetailDto.getImage());
        user.setAddress(userDetailDto.getAddress());
        user.setGender(userDetailDto.getGender());
        user.setDate_of_birth(OrphanUtils.StringToDate(userDetailDto.getDate_of_birth()));
        user.setIdentification(userDetailDto.getIdentification());
        user.setPhone(userDetailDto.getPhone());
        return user;
    }

}
