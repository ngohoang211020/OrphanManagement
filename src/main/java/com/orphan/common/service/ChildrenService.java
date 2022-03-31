package com.orphan.common.service;

import com.orphan.api.controller.manager.children.dto.ChildrenDetailDto;
import com.orphan.api.controller.manager.children.dto.ChildrenDto;
import com.orphan.api.controller.manager.children.dto.ChildrenRequest;
import com.orphan.common.entity.Children;
import com.orphan.common.entity.OrphanIntroducer;
import com.orphan.common.entity.OrphanNurturer;
import com.orphan.common.repository.ChildrenRepository;
import com.orphan.common.repository.OrphanIntroducerRepository;
import com.orphan.common.repository.OrphanNuturerRepository;
import com.orphan.enums.ChildrenStatus;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.APIConstants;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Sort;
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

    private final OrphanNuturerRepository orphanNuturerRepository;

    public Children findById(Integer childrenId) throws NotFoundException {
        Optional<Children> childrenOptional=childrenRepository.findById(childrenId);
        if(!childrenOptional.isPresent()){
            throw new NotFoundException(NotFoundException.ERROR_CHILDREN_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.CHILDREN));
        }
        return childrenOptional.get();
    }

    public ChildrenRequest createChildren(ChildrenRequest childrenRequest) throws NotFoundException {

        Children children=ChildrenRequestToChidren(childrenRequest);
        children.setCreatedId(String.valueOf(getCurrentUserId()));
        this.childrenRepository.save(children);
        childrenRequest.setId(children.getId());
        return childrenRequest;
    }

    public ChildrenRequest updateChildrenDetail(ChildrenRequest childrenRequest, Integer childrenId) throws NotFoundException {
        Children children=findById(childrenId);

        children.setStatus(childrenRequest.getStatus().equals(ChildrenStatus.RECEIVED.name())?ChildrenStatus.RECEIVED:ChildrenStatus.WAIT_TO_RECEIVE);
        children.setGender(childrenRequest.getGender());
        children.setDateOfBirth(OrphanUtils.StringToDate(childrenRequest.getDateOfBirth()));
        children.setFullName(childrenRequest.getFullName());

        if(childrenRequest.getNameOfIntroducer()!=""){
            OrphanIntroducer orphanIntroducer=orphanIntroducerRepository.findByFullName(childrenRequest.getNameOfIntroducer()).get();
            orphanIntroducer.setDateIntroduce(OrphanUtils.StringToDate(childrenRequest.getDateReceived()));
            orphanIntroducer.setFullName(childrenRequest.getNameOfIntroducer());
            orphanIntroducer.setCreatedId(String.valueOf(getCurrentUserId()));
            children.setOrphanIntroducer(orphanIntroducer);
        }

        if(childrenRequest.getNameOfNurturer()!="") {
            OrphanNurturer orphanNurturer = orphanNuturerRepository.findByFullName(childrenRequest.getNameOfNurturer()).get();
            orphanNurturer.setDateReceivedOfNurturer(OrphanUtils.StringToDate(childrenRequest.getDateLeaved()));
            orphanNurturer.setFullName(childrenRequest.getNameOfNurturer());
            orphanNurturer.setCreatedId(String.valueOf(getCurrentUserId()));
            children.setOrphanNurturer(orphanNurturer);
        }
        children.setModifiedId(String.valueOf(getCurrentUserId()));

        this.childrenRepository.save(children);
        childrenRequest.setId(children.getId());
        return childrenRequest;
    }

    public List<ChildrenDto> viewAllChildrens() throws NotFoundException {
        List<Children> childrenList=childrenRepository.findAll(Sort.by("fullName").ascending());
        if (childrenList.isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_CHILDREN_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.CHILDREN));
        }
        List<ChildrenDto> childrenDtoList = childrenList.stream().map(children -> {
            return ChildrenToChildrenDto(children);
        }).collect(Collectors.toList());
        return childrenDtoList;
    }



    //mapper

    public ChildrenDto ChildrenToChildrenDto(Children children){
        ChildrenDto childrenDto=new ChildrenDto();
        childrenDto.setId(children.getId());
        childrenDto.setFullName(children.getFullName());
        childrenDto.setImage(children.getImage());
        childrenDto.setDateOfBirth(OrphanUtils.DateToString(children.getDateOfBirth()));
        childrenDto.setImageFile(children.getProfPic());
        childrenDto.setStatus(children.getStatus().name());

        return childrenDto;
    }

    public ChildrenDetailDto ChildrenToChildrenDetailDto(Children children){
        ChildrenDetailDto childrenDetailDto=new ChildrenDetailDto();
        childrenDetailDto.setId(children.getId());
        childrenDetailDto.setFullName(children.getFullName());
        childrenDetailDto.setImage(children.getImage());
        childrenDetailDto.setDateOfBirth(OrphanUtils.DateToString(children.getDateOfBirth()));
        childrenDetailDto.setImageFile(children.getProfPic());
        childrenDetailDto.setStatus(children.getStatus().name());
        childrenDetailDto.setGender(children.getGender());

        childrenDetailDto.setDateReceived(OrphanUtils.DateToString(children.getOrphanIntroducer().getDateIntroduce()));
        childrenDetailDto.setNameOfIntroducer(children.getOrphanIntroducer().getFullName());

        childrenDetailDto.setDateLeaved(OrphanUtils.DateToString(children.getOrphanNurturer().getDateReceivedOfNurturer()));
        childrenDetailDto.setNameOfNurturer(children.getOrphanNurturer().getFullName());

        return childrenDetailDto;
    }

    public Children ChildrenRequestToChidren(ChildrenRequest childrenRequest){
        Children children=new Children();
        children.setStatus(childrenRequest.getStatus().equals(ChildrenStatus.RECEIVED.name())?ChildrenStatus.RECEIVED:ChildrenStatus.WAIT_TO_RECEIVE);
        children.setGender(childrenRequest.getGender());
        children.setDateOfBirth(OrphanUtils.StringToDate(childrenRequest.getDateOfBirth()));
        children.setFullName(childrenRequest.getFullName());

        if(childrenRequest.getNameOfIntroducer()!=""){
            OrphanIntroducer orphanIntroducer=orphanIntroducerRepository.findByFullName(childrenRequest.getNameOfIntroducer()).get();
            orphanIntroducer.setDateIntroduce(OrphanUtils.StringToDate(childrenRequest.getDateReceived()));
            orphanIntroducer.setFullName(childrenRequest.getNameOfIntroducer());
            orphanIntroducer.setCreatedId(String.valueOf(getCurrentUserId()));
            children.setOrphanIntroducer(orphanIntroducer);
        }

        if(childrenRequest.getNameOfNurturer()!="") {
            OrphanNurturer orphanNurturer = orphanNuturerRepository.findByFullName(childrenRequest.getNameOfNurturer()).get();
            orphanNurturer.setDateReceivedOfNurturer(OrphanUtils.StringToDate(childrenRequest.getDateLeaved()));
            orphanNurturer.setFullName(childrenRequest.getNameOfNurturer());
            orphanNurturer.setCreatedId(String.valueOf(getCurrentUserId()));
            children.setOrphanNurturer(orphanNurturer);
        }
        return children;
    }
}
