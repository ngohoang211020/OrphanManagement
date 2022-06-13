package com.orphan.api.controller.manager.Logistic.FundManagement;

import com.orphan.api.controller.manager.Logistic.FundManagement.dto.FundDto;
import com.orphan.common.response.APIResponse;
import com.orphan.common.service.FundManagementService;
import com.orphan.common.vo.PageInfo;
import com.orphan.enums.FundType;
import com.orphan.exception.NotFoundException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manager/fund")
@PreAuthorize("hasAnyRole('ADMIN','MANAGER_LOGISTIC')")
@RequiredArgsConstructor
public class FundManagementController {

    private final FundManagementService fundManagementService;

    @ApiOperation("Get Funds By Pages")
    @GetMapping
    public APIResponse<?> viewFundsByPages(
            @ApiParam(value = "Page") @RequestParam(value = "page", required = false) Integer page
            ,
            @ApiParam(value = "Limit") @RequestParam(value = "limit", required = false) Integer limit
            , @ApiParam(value = "Date") @RequestParam(value = "date", required = false) String date
            , @ApiParam(value = "Type") @RequestParam(value = "type", required = false) String type)
            throws NotFoundException {
        PageInfo<FundDto> fundDtoPageInfo;
        if (page != null) {
            fundDtoPageInfo = fundManagementService.viewFundByPage(page, limit, date, type);
        } else {
            fundDtoPageInfo = fundManagementService.viewFundByPage(1, limit, date, type);

        }
        return APIResponse.okStatus(fundDtoPageInfo);
    }

    @ApiOperation("View Fund IN/OUT Detail")
    @GetMapping("/{fundId}")
    public APIResponse<?> viewFundDetail(@PathVariable("fundId") Integer fundId)
            throws NotFoundException {
        FundDto fundDto = fundManagementService.viewFundDetail(fundId);
        return APIResponse.okStatus(fundDto);
    }

    @ApiOperation("Statistics Money By Month Type Picnic")
    @GetMapping("/picnic")
    public APIResponse<?> statisticsMoneyPicnic() {
        return APIResponse.okStatus(fundManagementService.moneyByMonth(FundType.PICNIC.getCode()));
    }

    @ApiOperation("Statistics Money By Month Type Charity Event")
    @GetMapping("/charity")
    public APIResponse<?> statisticsMoneyCharity() {
        return APIResponse.okStatus(
                fundManagementService.moneyByMonth(FundType.CHARITY_EVENT.getCode()));
    }

    @ApiOperation("Statistics Money By Month Type Furniture Request")
    @GetMapping("/furniture")
    public APIResponse<?> statisticsMoneyFurniture() {
        return APIResponse.okStatus(
                fundManagementService.moneyByMonth(FundType.FURNITURE_REQUEST.getCode()));
    }
}
