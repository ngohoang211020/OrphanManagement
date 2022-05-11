package com.orphan.common.service;

import com.orphan.api.controller.manager.Children.children.dto.ChildrenDetailDto;
import com.orphan.api.controller.manager.Children.children.dto.ChildrenDto;
import com.orphan.api.controller.manager.Children.children.dto.ChildrenRequest;
import com.orphan.common.entity.Children;
import com.orphan.common.entity.OrphanIntroducer;
import com.orphan.common.entity.OrphanNurturer;
import com.orphan.common.repository.ChildrenRepository;
import com.orphan.common.repository.OrphanIntroducerRepository;
import com.orphan.common.repository.OrphanNurturerRepository;
import com.orphan.common.response.StatisticsByDateResponse;
import com.orphan.common.response.StatisticsResponse;
import com.orphan.common.vo.PageInfo;
import com.orphan.enums.ChildrenStatus;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.APIConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChildrenService extends BaseService {

    private final ChildrenRepository childrenRepository;

    private final OrphanIntroducerRepository orphanIntroducerRepository;

    private final OrphanNurturerRepository orphanNuturerRepository;

    public Children findById(Integer childrenId) throws NotFoundException {
        Optional<Children> childrenOptional = childrenRepository.findById(childrenId);
        if (!childrenOptional.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_CHILDREN_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.CHILDREN));
        }
        return childrenOptional.get();
    }

    public ChildrenRequest createChildren(ChildrenRequest childrenRequest) throws NotFoundException {
        Children children = ChildrenRequestToChidren(childrenRequest);
        children.setCreatedId(String.valueOf(getCurrentUserId()));
        this.childrenRepository.save(children);
        childrenRequest.setId(children.getId());
        return childrenRequest;
    }

    public ChildrenRequest updateChildrenDetail(ChildrenRequest childrenRequest, Integer childrenId) throws NotFoundException {
        Children children = findById(childrenId);
        children.setGender(childrenRequest.getGender());
        children.setDateOfBirth(OrphanUtils.StringToDate(childrenRequest.getDateOfBirth()));
        children.setFullName(childrenRequest.getFullName());
        children.setIntroductoryDate(OrphanUtils.StringToDate(childrenRequest.getIntroductoryDate()));

        if (childrenRequest.getAdoptiveDate() != "") {
            children.setAdoptiveDate(OrphanUtils.StringToDate(childrenRequest.getAdoptiveDate()));
            OrphanNurturer orphanNurturer = orphanNuturerRepository.findById(childrenRequest.getNurturerId()).get();
            children.setStatus(ChildrenStatus.RECEIVED.getCode());
            children.setOrphanNurturer(orphanNurturer);
        } else {
            children.setStatus(ChildrenStatus.WAIT_TO_RECEIVE.getCode());
        }
        if (childrenRequest.getIntroducerId() > 0 && childrenRequest.getIntroducerId() != null) {
            OrphanIntroducer orphanIntroducer = orphanIntroducerRepository.findById(childrenRequest.getIntroducerId()).get();
            children.setOrphanIntroducer(orphanIntroducer);
        }
        if (childrenRequest.getImage() != "" && childrenRequest.getImage() != null) {
            children.setImage(childrenRequest.getImage());
        }
        children.setModifiedId(String.valueOf(getCurrentUserId()));
        this.childrenRepository.save(children);
        childrenRequest.setId(childrenId);
        return childrenRequest;
    }

    public ChildrenDetailDto viewChildrenDetail(Integer childrenId) throws NotFoundException {
        Children children = findById(childrenId);
        return ChildrenToChildrenDetailDto(children);
    }

    public List<ChildrenDto> viewAllChildrens() throws NotFoundException {
        List<Children> childrenList = childrenRepository.findAll();
        if (childrenList.isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_CHILDREN_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.CHILDREN));
        }
        List<ChildrenDto> childrenDtoList = childrenList.stream().map(children -> {
            return ChildrenToChildrenDto(children);
        }).collect(Collectors.toList());
        return childrenDtoList;
    }

    public PageInfo<ChildrenDto> viewChildrensByPage(Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit);
        Page<Children> childrenPage = childrenRepository.findByOrderByCreatedAtAsc(pageRequest);
        if (childrenPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_CHILDREN_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.CHILDREN));
        }
        List<ChildrenDto> childrenDtoList = childrenPage.getContent().stream().map(this::ChildrenToChildrenDto).collect(Collectors.toList());
        PageInfo<ChildrenDto> childrenDtoPageInfo = new PageInfo<>();
        childrenDtoPageInfo.setPage(page);
        childrenDtoPageInfo.setLimit(limit);
        childrenDtoPageInfo.setResult(childrenDtoList);
        childrenDtoPageInfo.setTotal(childrenPage.getTotalElements());
        childrenDtoPageInfo.setPages(childrenPage.getTotalPages());
        return childrenDtoPageInfo;
    }

    public void deleteById(Integer childrenId) throws NotFoundException {
        if (!childrenRepository.findById(childrenId).isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_CHILDREN_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.CHILDREN));
        }
        childrenRepository.deleteById(childrenId);
    }

    public List<StatisticsByDateResponse> countChildrenIntroductiveByMonth() {
        List<StatisticsByDateResponse> statisticsByDateRespons = childrenRepository.countChildrenByIntroduceDate();
        return statisticsByDateRespons;
    }
    public List<StatisticsByDateResponse> countChildrenIntroductiveByYear() {
        List<StatisticsByDateResponse> statisticsByDateRespons = childrenRepository.countChildrenByIntroduceDateYear();
        return statisticsByDateRespons;
    }
    public List<StatisticsByDateResponse> countChildrenNurturerByMonth() {
        List<StatisticsByDateResponse> statisticsByDateRespons = childrenRepository.countChildrenByAdoptiveDate();
        return statisticsByDateRespons;
    }
    public List<StatisticsByDateResponse> countChildrenNurturerByYear() {
        List<StatisticsByDateResponse> statisticsByDateRespons = childrenRepository.countChildrenByAdoptiveDateYear();
        return statisticsByDateRespons;
    }

    public List<StatisticsResponse> countChildrenByGender() {
        List<StatisticsResponse> statisticsResponses = childrenRepository.countChildrenByGender();
        return statisticsResponses;
    }

    //search

    public PageInfo<ChildrenDto> searchChildrensByPage(String keyword,Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest = buildPageRequest(page, limit);
        Page<Children> childrenPage = childrenRepository.searchChildren(keyword,pageRequest);
        if (childrenPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_CHILDREN_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.CHILDREN));
        }
        List<ChildrenDto> childrenDtoList = childrenPage.getContent().stream().map(this::ChildrenToChildrenDto).collect(Collectors.toList());
        PageInfo<ChildrenDto> childrenDtoPageInfo = new PageInfo<>();
        childrenDtoPageInfo.setPage(page);
        childrenDtoPageInfo.setLimit(limit);
        childrenDtoPageInfo.setResult(childrenDtoList);
        childrenDtoPageInfo.setTotal(childrenPage.getTotalElements());
        childrenDtoPageInfo.setPages(childrenPage.getTotalPages());
        return childrenDtoPageInfo;
    }
    //mapper


    private ChildrenDto ChildrenToChildrenDto(Children children) {
        ChildrenDto childrenDto = new ChildrenDto();
        childrenDto.setId(children.getId());
        childrenDto.setFullName(children.getFullName());
        childrenDto.setImage(children.getImage());
        childrenDto.setDateOfBirth(OrphanUtils.DateToString(children.getDateOfBirth()));
        childrenDto.setStatus(children.getStatus());
        return childrenDto;
    }

    private ChildrenDetailDto ChildrenToChildrenDetailDto(Children children) {
        ChildrenDetailDto childrenDetailDto = new ChildrenDetailDto();
        childrenDetailDto.setId(children.getId());
        childrenDetailDto.setFullName(children.getFullName());
        childrenDetailDto.setImage(children.getImage());
        childrenDetailDto.setDateOfBirth(OrphanUtils.DateToString(children.getDateOfBirth()));
        childrenDetailDto.setStatus(children.getStatus());
        childrenDetailDto.setGender(children.getGender());
        childrenDetailDto.setIntroductoryDate(OrphanUtils.DateToString(children.getIntroductoryDate()));

        if (children.getOrphanIntroducer() != null) {
            childrenDetailDto.setNameOfIntroducer(children.getOrphanIntroducer().getFullName());
            childrenDetailDto.setIntroducerId(children.getOrphanIntroducer().getIntroducerId());
        }
        if (children.getOrphanNurturer() != null) {
            childrenDetailDto.setAdoptiveDate(OrphanUtils.DateToString(children.getAdoptiveDate()));
            childrenDetailDto.setNameOfNurturer(children.getOrphanNurturer().getFullName());
            childrenDetailDto.setNurturerId(children.getOrphanNurturer().getNurturerId());
        }
        return childrenDetailDto;
    }

    private Children ChildrenRequestToChidren(ChildrenRequest childrenRequest) {
        Children children = new Children();
        children.setGender(childrenRequest.getGender());
        children.setDateOfBirth(OrphanUtils.StringToDate(childrenRequest.getDateOfBirth()));
        children.setFullName(childrenRequest.getFullName());
        children.setIntroductoryDate(OrphanUtils.StringToDate(childrenRequest.getIntroductoryDate()));
        if (childrenRequest.getImage() != "" && childrenRequest.getImage() != null) {
            children.setImage(childrenRequest.getImage());
        }
        if (childrenRequest.getAdoptiveDate() != "" && childrenRequest.getNurturerId() > 0 && !childrenRequest.getNurturerId().equals(null)) {
            children.setAdoptiveDate(OrphanUtils.StringToDate(childrenRequest.getAdoptiveDate()));
            OrphanNurturer orphanNurturer = orphanNuturerRepository.findById(childrenRequest.getNurturerId()).get();
            children.setStatus(ChildrenStatus.RECEIVED.getCode());
            children.setOrphanNurturer(orphanNurturer);
        } else {
            children.setStatus(ChildrenStatus.WAIT_TO_RECEIVE.getCode());
        }
        if (childrenRequest.getIntroducerId() > 0 && !childrenRequest.getIntroducerId().equals(null)) {
            OrphanIntroducer orphanIntroducer = orphanIntroducerRepository.findById(childrenRequest.getIntroducerId()).get();
            children.setOrphanIntroducer(orphanIntroducer);
        }
        children.setId(childrenRequest.getId());
        return children;
    }


}
