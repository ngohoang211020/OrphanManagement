package com.orphan.common.service;

import com.orphan.common.entity.User;
import com.orphan.common.repository.*;
import com.orphan.config.jwt.AccessToken;
import com.orphan.config.jwt.JwtUtils;
import com.orphan.config.security.UserPrincipal;
import com.orphan.utils.constants.PageableConstants;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class BaseService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChildrenRepository childrenRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OrphanIntroducerRepository orphanIntroducerRepository;

    @Autowired
    private OrphanNuturerRepository orphanNuturerRepository;

    @Autowired
    private JwtUtils jwtProvider;

    private static final Logger logger = LoggerFactory.getLogger(BaseService.class);

    /**
     * Get current userid has logged.
     *
     * @return
     */
    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = null;
        if (principal instanceof UserDetails) {
            userId = ((UserPrincipal) principal).getUserId();
        } else if (!principal.equals("anonymousUser")) {
            userId = Integer.parseInt((String) principal);
        }
        return userId;
    }

    /**
     * Get current user has logged
     *
     * @return
     */
    public User getCurrentUserLogged() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = new User();
        if (principal instanceof UserDetails) {
            UserPrincipal userPrincipal = (UserPrincipal) principal;
            user.setLoginId(userPrincipal.getUserId());
        }
        return user;
    }

    /**
     * Create JwtToken
     *
     * @param authentication
     * @param isRememberMe
     * @return
     */
    public AccessToken jwtForAPIResponse(Authentication authentication, boolean isRememberMe) {
        return jwtProvider.createAccessToken(authentication, isRememberMe);
    }
    /**
     * Create Token register
     *
     * @param email request
     * @return
     */
    public String jwtForRegister(String email) {
        return jwtProvider.createTokenRegister(email);
    }

    /**
     * get information user by Email
     *
     * @return User
     * @throws NotFoundException
     */
    public Optional<User> getUserByLoginId(String loginId) throws com.orphan.exception.NotFoundException {
        Optional<User> user = this.userRepository.findByLoginId(Integer.parseInt(loginId));
        if (user.isPresent()) {
            return user;
        }
        return Optional.empty();
    }

    public Optional<User> getUserByEmail(String email) throws com.orphan.exception.NotFoundException {
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user;
        }
        return Optional.empty();
    }


    public Boolean isExpiredToken(String token) {
        return jwtProvider.isTokenExpired(token);
    }

    public String getEmailFromJwtToken(String token) {
        return jwtProvider.getUserNameFromJwtToken(token);
    }

    /**
     * Build {@link PageRequest} with page and limit
     *
     * @param page
     * @param limit
     * @return
     */
    public PageRequest buildPageRequest(Integer page, Integer limit) {
        page = (limit == null || page == null) ? PageableConstants.DEFAULT_PAGE : page - PageableConstants.DEFAULT_PAGE_INIT;
        limit = limit == null ? PageableConstants.DEFAULT_SIZE : limit;
        return PageRequest.of(page, limit);
    }

    /**
     * Build {@link PageRequest} with page, limit and sort
     *
     * @param page
     * @param limit
     * @param sort
     * @return
     */
    public PageRequest buildPageRequest(Integer page, Integer limit, Sort sort) {
        page = page == null ? PageableConstants.DEFAULT_PAGE : page - PageableConstants.DEFAULT_PAGE_INIT;
        limit = limit == null ? PageableConstants.DEFAULT_SIZE : limit;
        return PageRequest.of(page, limit, sort);
    }

}
