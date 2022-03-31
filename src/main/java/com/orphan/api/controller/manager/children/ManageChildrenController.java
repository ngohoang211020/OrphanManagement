package com.orphan.api.controller.manager.children;

import com.orphan.api.controller.admin.dto.UserDto;
import com.orphan.api.controller.manager.children.dto.ChildrenDto;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.ChildrenService;
import com.orphan.exception.NotFoundException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manager")
@PreAuthorize("hasRole('MANAGER')")
@RequiredArgsConstructor
public class ManageChildrenController {

    private final ChildrenService childrenService;

//    @ApiOperation("Get All Childrens")
//    @GetMapping
//    public APIResponse<?> viewAllChildrens() throws NotFoundException {
//        List<ChildrenDto> childrenDtoList = childrenService.viewAllChildrens();
//        return APIResponse.okStatus(childrenDtoList);
//    }
}
