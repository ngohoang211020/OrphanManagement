package com.orphan.common.service;

import com.orphan.common.entity.Staff;
import com.orphan.common.repository.StaffRepository;
import com.orphan.common.vo.PageInfo;
import com.orphan.enums.TypeStaff;
import com.orphan.exception.BadRequestException;
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
public class StaffService extends BaseService {
    private final StaffRepository staffRepository;

    private final MessageService messageService;

    private static final Logger logger = LoggerFactory.getLogger(StaffService.class);


    public Staff findById(Integer staffId) throws NotFoundException {
        Optional<Staff> staffOptional = staffRepository.findById(staffId);
        if (!staffOptional.isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_STAFF_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.STAFF));
        }
        return staffOptional.get();
    }

    public StaffRequest createStaff(StaffRequest staffRequest) throws BadRequestException {
        if (staffRepository.existsByEmail(staffRequest.getEmail())) {
            throw new BadRequestException(BadRequestException.ERROR_EMAIL_ALREADY_EXIST,
                    this.messageService.buildMessages("error.msg.email-existed"));
        }

        if (staffRepository.existsByIdentification(staffRequest.getIdentification())) {
            throw new BadRequestException(BadRequestException.ERROR_IDENTIFICATION_ALREADY_EXIST,
                    this.messageService.buildMessages("error.msg.identification-existed"));
        }

        Staff staff = StaffRequestToStaff(staffRequest);

        staff.setCreatedId(String.valueOf(getCurrentUserId()));

        staff = staffRepository.save(staff);

        staffRequest.setStaffId(staff.getStaffId());

        return staffRequest;
    }

    public StaffRequest updateStaff(StaffRequest staffRequest, Integer staffId) throws NotFoundException, BadRequestException {

        Staff staff = findById(staffId);

        if (!staff.getEmail().equals(staffRequest.getEmail())) {
            if (staffRepository.existsByEmail(staffRequest.getEmail())) {
                throw new BadRequestException(BadRequestException.ERROR_EMAIL_ALREADY_EXIST,
                        this.messageService.buildMessages("error.msg.email-existed"));
            }
            staff.setEmail(staffRequest.getEmail());
        }

        if (!staff.getIdentification().equals(staffRequest.getIdentification())) {
            if (staffRepository.existsByIdentification(staffRequest.getIdentification())) {
                throw new BadRequestException(BadRequestException.ERROR_IDENTIFICATION_ALREADY_EXIST,
                        this.messageService.buildMessages("error.msg.identification-existed"));
            }
            staff.setIdentification(staffRequest.getIdentification());
        }

        if(staffRequest.getImage()!=""){
            staff.setImage(staffRequest.getImage());
        }

        staff.setAddress(staffRequest.getAddress());
        staff.setFullName(staffRequest.getFullName());
        staff.setDateOfBirth(OrphanUtils.StringToDate(staffRequest.getDateOfBirth()));
        staff.setTypeStaff(typeStaffToString(staffRequest.getTypeStaff()));
        staff.setEmail(staffRequest.getEmail());
        staff.setGender(staffRequest.getGender());
        staff.setIdentification(staffRequest.getIdentification());
        staff.setPhone(staffRequest.getPhone());

        staff.setModifiedId(String.valueOf(getCurrentUserId()));

        this.staffRepository.save(staff);

        staffRequest.setStaffId(staffId);

        return staffRequest;
    }

    public void deleteById(Integer staffId) throws NotFoundException {
        if (!staffRepository.findById(staffId).isPresent()) {
            throw new NotFoundException(NotFoundException.ERROR_STAFF_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.STAFF));
        }
        staffRepository.deleteById(staffId);
    }

    public PageInfo<StaffDto> viewStaffsByPage(Integer page, Integer limit) throws NotFoundException {
        PageRequest pageRequest=buildPageRequest(page,limit);
        Page<Staff> staffPage = staffRepository.findByOrderByFullNameAsc(pageRequest);
        if (staffPage.getContent().isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_STAFF_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.STAFF));
        }
        List<StaffDto> staffDtoList = staffPage.getContent().stream().map(staff -> {
            return StaffToStaffDto(staff);
        }).collect(Collectors.toList());
        PageInfo<StaffDto> staffDtoPageInfo=new PageInfo<>();
        staffDtoPageInfo.setPage(page);
        staffDtoPageInfo.setLimit(limit);
        staffDtoPageInfo.setResult(staffDtoList);
        staffDtoPageInfo.setTotal(staffPage.getTotalElements());
        staffDtoPageInfo.setPages(staffPage.getTotalPages());
        return staffDtoPageInfo;
    }

    public List<StaffDto> viewAllStaffs() throws NotFoundException {
        List<Staff> staffList = staffRepository.findAll();
        if (staffList.isEmpty()) {
            throw new NotFoundException(NotFoundException.ERROR_STAFF_NOT_FOUND,
                    APIConstants.NOT_FOUND_MESSAGE.replace(APIConstants.REPLACE_CHAR, APIConstants.STAFF));
        }
        List<StaffDto> staffDtoList = staffList.stream().map(staff -> StaffToStaffDto(staff)).collect(Collectors.toList());
        return staffDtoList;
    }

    public StaffDetailDto viewStaffDetail(Integer staffId) throws NotFoundException {
        StaffDetailDto staffDetailDto = StaffToStaffDetailDto(findById(staffId));
        return staffDetailDto;
    }
    //mapper

    private Staff StaffRequestToStaff(StaffRequest staffRequest) {
        Staff staff = new Staff();
        staff.setAddress(staffRequest.getAddress());
        staff.setFullName(staffRequest.getFullName());
        staff.setDateOfBirth(OrphanUtils.StringToDate(staffRequest.getDateOfBirth()));
        staff.setTypeStaff(typeStaffToString(staffRequest.getTypeStaff()));
        staff.setEmail(staffRequest.getEmail());
        staff.setGender(staffRequest.getGender());
        staff.setIdentification(staffRequest.getIdentification());
        staff.setPhone(staffRequest.getPhone());
        staff.setImage(staffRequest.getImage());
        return staff;
    }

    private StaffDto StaffToStaffDto(Staff staff) {
        StaffDto staffDto = new StaffDto();
        staffDto.setStaffId(staff.getStaffId());
        staffDto.setPhone(staff.getPhone());
        staffDto.setFullName(staff.getFullName());
        staffDto.setImage(staff.getImage());
        staffDto.setTypeStaff(typeStaffToString(staff.getTypeStaff()));

        return staffDto;
    }

    private StaffDetailDto StaffToStaffDetailDto(Staff staff) {
        StaffDetailDto staffDetailDto = new StaffDetailDto();

        staffDetailDto.setStaffId(staff.getStaffId());
        staffDetailDto.setPhone(staff.getPhone());
        staffDetailDto.setFullName(staff.getFullName());
        staffDetailDto.setImage(staff.getImage());
        staffDetailDto.setTypeStaff(typeStaffToString(staff.getTypeStaff()));

        staffDetailDto.setAddress(staff.getAddress());
        staffDetailDto.setGender(staff.getGender());
        staffDetailDto.setEmail(staff.getEmail());
        staffDetailDto.setIdentification(staff.getIdentification());
        staffDetailDto.setDateOfBirth(OrphanUtils.DateToString(staff.getDateOfBirth()));
        return staffDetailDto;
    }

    private String typeStaffToString(String typeStaff){
        return typeStaff.equals(TypeStaff.NhanVien.getCode()) ? TypeStaff.NhanVien.getCode() : TypeStaff.CanBo.getCode();
    }
}
