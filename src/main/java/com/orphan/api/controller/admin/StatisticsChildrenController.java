package com.orphan.api.controller.admin;

import com.orphan.common.request.DateRequest;
import com.orphan.common.request.GenderRequest;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.ChildrenService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/children")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class StatisticsChildrenController {
    private final ChildrenService childrenService;

    @ApiOperation("count the number of children introduced")
    @GetMapping("/introduce")
    public APIResponse<?> countChildrenIntroduceByDate(@RequestBody DateRequest dateRequest)  {
        if(dateRequest.getMonth()>0&&dateRequest.getMonth()!=null) {
            return APIResponse.okStatus(childrenService.countChildrenIntroduceByMonth(dateRequest));
        }
        else {
            return APIResponse.okStatus(childrenService.countChildrenIntroduceByYear(dateRequest));
        }
    }
    @ApiOperation("count the number of children adoptive")
    @GetMapping("/nurturer")
    public APIResponse<?> countChildrenNurturerByDate(@RequestBody DateRequest dateRequest)  {
        if(dateRequest.getMonth()>0&&dateRequest.getMonth()!=null) {
            return APIResponse.okStatus(childrenService.countChildrenAdoptiveByMonth(dateRequest));
        }
        else {
            return APIResponse.okStatus(childrenService.countChildrenAdoptiveByYear(dateRequest));
        }
    }
    @ApiOperation("Count children By Gender")
    @GetMapping("/gender")
    public APIResponse<?> countChildrenByGender(@RequestBody GenderRequest gender)  {
        return APIResponse.okStatus(childrenService.countChildrenByGender(gender));
    }

}
