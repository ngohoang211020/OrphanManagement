package com.orphan.common.service;

import com.orphan.api.controller.manager.Logistic.FundManagement.dto.FundDto;
import com.orphan.common.entity.FundManagement;
import com.orphan.common.repository.FundManagementRepository;
import com.orphan.common.repository.InformationSystemRepository;
import com.orphan.common.response.StatisticsByDateResponse;
import com.orphan.common.vo.PageInfo;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.APIConstants;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FundManagementService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(FundManagementService.class);
    private final FundManagementRepository fundManagementRepository;
    private final InformationSystemRepository informationSystemRepository;

    public FundManagement findById(Integer fundId) throws NotFoundException {
        Optional<FundManagement> fundManagement = fundManagementRepository.findById(fundId);
        if (!fundManagement.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_FUND_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR,
                            APIConstants.FUND));
        }
        return fundManagement.get();
    }

    public PageInfo<FundDto> viewFundByPage(Integer page, Integer limit, String date, String type)
            throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit, Sort.by("date").descending());
        Page<FundManagement> fundManagementPage = fundManagementRepository.findByDateAndTypeOrderByDateDesc(
                OrphanUtils.StringToDateTime(date), type, pageRequest);
        if (fundManagementPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_FUND_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR,
                            APIConstants.FUND));
        }
        List<FundDto> fundDtos = fundManagementPage.getContent().stream()
                .map(fundManagement -> toDto(fundManagement)).collect(
                        Collectors.toList());
        PageInfo<FundDto> fundDtoPageInfo = new PageInfo<>();
        fundDtoPageInfo.setPage(page);
        fundDtoPageInfo.setLimit(limit);
        fundDtoPageInfo.setResult(fundDtos);
        fundDtoPageInfo.setTotal(fundManagementPage.getTotalElements());
        fundDtoPageInfo.setPages(fundManagementPage.getTotalPages());
        return fundDtoPageInfo;
    }

    public FundDto viewFundDetail(Integer id) throws NotFoundException {
        FundManagement fundManagement = findById(id);
        return toDto(fundManagement);
    }

    public List<StatisticsByDateResponse> moneyByMonth(String type) {
        List<StatisticsByDateResponse> statisticsByDateRespons = fundManagementRepository.moneyByMonth(
                type);
        return statisticsByDateRespons;
    }

    public List<StatisticsByDateResponse> moneyPicnicByYear(String type) {
        List<StatisticsByDateResponse> statisticsByDateRespons = fundManagementRepository.moneyByYear(
                type);
        return statisticsByDateRespons;
    }

    //mapper
    private FundDto toDto(FundManagement fundManagement) {
        FundDto fundDto = new FundDto();
        fundDto.setId(fundManagement.getId());
        fundDto.setDate(OrphanUtils.DateToString(fundManagement.getDate()));
        fundDto.setDescription(fundManagement.getDescription());
        fundDto.setType(fundManagement.getType());
        fundDto.setIsCalculated(fundManagement.getIsCalculated());
        fundDto.setUserId(fundManagement.getUserId());
        fundDto.setMoney(fundManagement.getMoney());
        return fundDto;
    }
}
