package com.orphan.common.service;

import com.orphan.api.controller.manager.Logistic.Picnic.dto.PicnicDto;
import com.orphan.api.controller.manager.Logistic.Picnic.dto.PicnicRequest;
import com.orphan.common.entity.Picnic;
import com.orphan.common.entity.User;
import com.orphan.common.repository.PicnicRepository;
import com.orphan.common.repository.UserRepository;
import com.orphan.common.vo.PageInfo;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.APIConstants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class PicnicService extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(PicnicService.class);
    private final UserRepository userRepository;
    private final PicnicRepository picnicRepository;

    public Picnic findById(Integer entertainmentId) throws NotFoundException {
        Optional<Picnic> entertainment = picnicRepository.findById(entertainmentId);
        if (!entertainment.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_PICNIC_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.PICNIC));
        }
        return entertainment.get();
    }

    public PicnicRequest createPicnic(PicnicRequest picnicRequest) {
        Picnic picnic = toEntity(picnicRequest);
        picnic.setCreatedId(String.valueOf(getCurrentUserId()));

        picnic =this.picnicRepository.save(picnic);

        picnicRequest.setId(picnic.getId());
        return picnicRequest;
    }

    public PicnicRequest updatePicnic(PicnicRequest picnicRequest, Integer picnicId) throws NotFoundException {
        picnicRequest.setId(picnicId);
        Picnic picnic = toEntity(picnicRequest);
        if(picnicRequest.getImage()!=""){
            picnic.setNamePicnic(picnicRequest.getImage());
            picnicRequest.setImage(picnic.getImage());
        }
        picnic.setModifiedId(String.valueOf(getCurrentUserId()));
        this.picnicRepository.save(picnic);
        return picnicRequest;
    }

    public void deleteById(Integer picnicId) throws NotFoundException {
        if (!picnicRepository.findById(picnicId).isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_PICNIC_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.PICNIC));
        }
        picnicRepository.deleteById(picnicId);
    }

    public PageInfo<PicnicDto> viewPicnicsByPage(Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest=buildPageRequest(page,limit);
        Page<Picnic> entertainmentPage = picnicRepository.findByOrderByDateOfPicnicAsc(pageRequest);
        if (entertainmentPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_PICNIC_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.PICNIC));
        }
        List<PicnicDto> picnicDtoList = entertainmentPage.getContent().stream().map(
                picnic -> toDto(picnic)).collect(Collectors.toList());
        PageInfo<PicnicDto> entertainmentDtoPageInfo=new PageInfo<>();
        entertainmentDtoPageInfo.setPage(page);
        entertainmentDtoPageInfo.setLimit(limit);
        entertainmentDtoPageInfo.setResult(picnicDtoList);
        entertainmentDtoPageInfo.setTotal(entertainmentPage.getTotalElements());
        entertainmentDtoPageInfo.setPages(entertainmentPage.getTotalPages());
        return entertainmentDtoPageInfo;
    }

    public List<PicnicDto> viewAllPicnic() throws NotFoundException {
        List<Picnic> picnicList = picnicRepository.findAll();
        if (picnicList.isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_PICNIC_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.PICNIC));
        }
        List<PicnicDto> picnicDtoList = picnicList.stream().map(
                picnic -> toDto(picnic)).collect(Collectors.toList());
        return picnicDtoList;
    }

    public PicnicRequest viewDetailPicnic(Integer picnicId) throws NotFoundException {
        Picnic picnic = findById(picnicId);
        return toDetaiDto(picnic);
    }


    //mapper
    private PicnicDto toDto(Picnic picnic) {
        PicnicDto picnicDto = new PicnicDto();
        picnicDto.setId(picnic.getId());
        picnicDto.setNamePicnic(picnic.getNamePicnic());
        picnicDto.setIsCompleted(picnic.getIsCompleted());
        picnicDto.setTitle(picnic.getTitle());
        picnicDto.setDateOfPicnic(OrphanUtils.DateToString(picnic.getDateOfPicnic()));
        picnicDto.setAddress(picnic.getAddress());
        picnicDto.setImage(picnic.getImage());
        return picnicDto;
    }

    private Picnic toEntity(PicnicRequest picnicRequest) {
        Picnic picnic = new Picnic();
        picnic.setId(picnicRequest.getId());
        picnic.setNamePicnic(picnicRequest.getNamePicnic());
        picnic.setContent(picnicRequest.getContent());
        picnic.setDateOfPicnic(OrphanUtils.StringToDate(picnicRequest.getDateOfPicnic()));
        picnic.setAddress(picnicRequest.getAddress());
        picnic.setImage(picnicRequest.getImage());
        picnic.setFrom(OrphanUtils.StringToLocalTime(picnicRequest.getFrom()));
        picnic.setTo(OrphanUtils.StringToLocalTime(picnicRequest.getTo()));
        User user=userRepository.findById(picnicRequest.getPersonInChargeId()).get();
        picnic.setIsCompleted(picnicRequest.getIsCompleted());
        picnic.setPersonInChargeId(user);
        picnic.setMoney(picnicRequest.getMoney());
        picnic.setTitle(picnicRequest.getTitle());
        return picnic;
    }

    private PicnicRequest toDetaiDto(Picnic picnic) {
        PicnicRequest picnicRequest = new PicnicRequest();
        picnicRequest.setId(picnic.getId());
        picnicRequest.setNamePicnic(picnic.getNamePicnic());
        picnicRequest.setContent(picnic.getContent());
        picnicRequest.setDateOfPicnic(OrphanUtils.DateToString(picnic.getDateOfPicnic()));
        picnicRequest.setAddress(picnic.getAddress());
        picnicRequest.setImage(picnic.getImage());
        picnicRequest.setFrom(OrphanUtils.LocalTimeToString(picnic.getFrom()));
        picnicRequest.setTo(OrphanUtils.LocalTimeToString(picnic.getTo()));
        User user=userRepository.findById(picnicRequest.getPersonInChargeId()).get();
        picnicRequest.setIsCompleted(picnic.getIsCompleted());
        picnicRequest.setPersonInChargeId(user.getLoginId());
        picnicRequest.setMoney(picnic.getMoney());
        picnicRequest.setTitle(picnic.getTitle());
        return picnicRequest;
    }


}
