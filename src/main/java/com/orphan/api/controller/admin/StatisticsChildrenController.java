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

    @ApiOperation("Count children Introduce By Month")
    @GetMapping("/introduce/month")
    public APIResponse<?> countChildrenIntroduceByMonth()  {
        return APIResponse.okStatus(childrenService.countChildrenIntroductiveByMonth());
    }

    @ApiOperation("Count children Introduce By Year")
    @GetMapping("/introduce/year")
    public APIResponse<?> countChildrenIntroduceByYear()  {
        return APIResponse.okStatus(childrenService.countChildrenIntroductiveByYear());
    }

    @ApiOperation("Count children Nurturer By Month")
    @GetMapping("/nurturer/month")
    public APIResponse<?> countChildrenNurturerByMonth()  {
        return APIResponse.okStatus(childrenService.countChildrenNurturerByMonth());
    }

    @ApiOperation("Count children Nurturer By Year")
    @GetMapping("/nurturer/year")
    public APIResponse<?> countChildrenNurturerByYear()  {
        return APIResponse.okStatus(childrenService.countChildrenNurturerByYear());
    }

    @ApiOperation("Count children By Gender")
    @GetMapping("/gender")
    public APIResponse<?> countChildrenByGender()  {
        return APIResponse.okStatus(childrenService.countChildrenByGender());
    }
}
