package com.orphan.api.controller.manager.Children.children;

import com.google.gson.JsonObject;
import com.orphan.api.controller.manager.Children.children.dto.ChildrenDetailDto;
import com.orphan.api.controller.manager.Children.children.dto.ChildrenDto;
import com.orphan.api.controller.manager.Children.children.dto.ChildrenRequest;
import com.orphan.common.repository.ChildrenRepository;
import com.orphan.common.request.SearchRequest;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.ChildrenService;
import com.orphan.common.vo.PageInfo;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.PageableConstants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/manager/children")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER_CHILDREN')")
@RequiredArgsConstructor
public class ChildrenController {

    private final ChildrenService childrenService;

    @ApiOperation("Get All Childrens")
    @GetMapping("/all")
    public APIResponse<?> viewAllChildrens() throws NotFoundException {
        return APIResponse.okStatus(childrenService.viewAllChildrens());
    }
    @ApiOperation("Get Childrens By Pages")
    @GetMapping
    public APIResponse<?> viewChildrensByPages(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
            ,@ApiParam(value = "Limit", required = false) @RequestParam(value = "limit", required = false) Integer limit) throws NotFoundException {
        PageInfo<ChildrenDto> childrenDtoPageInfo;
        if (page != null) {
            childrenDtoPageInfo = childrenService.viewChildrensByPage(page, limit);
        } else {
            childrenDtoPageInfo = childrenService.viewChildrensByPage(1, limit);

        }
        return APIResponse.okStatus(childrenDtoPageInfo);
    }

    @ApiOperation("View Children Detail")
    @GetMapping("/{childrenId}")
    public APIResponse<?> viewChildrenDetail(@PathVariable("childrenId") Integer childrenId) throws NotFoundException {
        ChildrenDetailDto childrenDetailDto = childrenService.viewChildrenDetail(childrenId);
        return APIResponse.okStatus(childrenDetailDto);
    }

    @ApiOperation("Create new Children")
    @PostMapping
    public APIResponse<?> createChildren(@Valid @RequestBody ChildrenRequest childrenRequest, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        childrenRequest = childrenService.createChildren(childrenRequest);
        return APIResponse.okStatus(childrenRequest);
    }

    @ApiOperation("Update children detail")
    @PutMapping("/{childrenId}")
    public APIResponse<?> updateChildren(@PathVariable("childrenId") Integer childrenId, @Valid @RequestBody ChildrenRequest childrenRequest, Errors errors) throws NotFoundException, BadRequestException {
        if (errors.hasErrors()) {
            JsonObject messages = OrphanUtils.getMessageListFromErrorsValidation(errors);
            throw new BadRequestException(BadRequestException.ERROR_REGISTER_USER_INVALID, messages.toString(), true);
        }
        childrenRequest = childrenService.updateChildrenDetail(childrenRequest, childrenId);
        return APIResponse.okStatus(childrenRequest);
    }

    @ApiOperation("Delete children")
    @DeleteMapping("/{childrenId}")
    public APIResponse<?> deleteChildren(@PathVariable("childrenId") Integer childrenId) throws NotFoundException {
        childrenService.deleteById(childrenId);
        return APIResponse.okStatus();
    }

    @ApiOperation("Search Childrens By Pages")
    @PostMapping("/search")
    public APIResponse<?> searchChildrensByPages(@ApiParam(value = "Page", required = false) @RequestParam(value = "page", required = false) Integer page
            ,@ApiParam(value = "Limit", required = false) @RequestParam(value = "limit", required = false) Integer limit,@RequestBody SearchRequest searchRequest) throws NotFoundException {
        PageInfo<ChildrenDto> childrenDtoPageInfo;
        if (page != null) {
            childrenDtoPageInfo = childrenService.searchChildrensByPage(searchRequest.getKeyword(),page, limit);
        } else {
            childrenDtoPageInfo = childrenService.searchChildrensByPage(searchRequest.getKeyword(),1, limit);

        }
        return APIResponse.okStatus(childrenDtoPageInfo);
    }

}
