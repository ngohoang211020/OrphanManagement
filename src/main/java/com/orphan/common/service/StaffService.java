package com.orphan.common.service;

import com.orphan.api.controller.UpdateImageResponse;
import com.orphan.api.controller.admin.dto.UserDto;
import com.orphan.api.controller.manager.staff.dto.StaffDetailDto;
import com.orphan.api.controller.manager.staff.dto.StaffDto;
import com.orphan.api.controller.manager.staff.dto.StaffRequest;
import com.orphan.common.entity.Staff;
import com.orphan.common.repository.StaffRepository;
import com.orphan.common.vo.PageInfo;
import com.orphan.enums.TypeStaff;
import com.orphan.exception.BadRequestException;
import com.orphan.exception.NotFoundException;
import com.orphan.utils.OrphanUtils;
import com.orphan.utils.constants.APIConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StaffService extends BaseService {
    private final StaffRepository staffRepository;

    private final MessageService messageService;

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

        staff.setAddress(staffRequest.getAddress());
        staff.setFullName(staffRequest.getFullName());
        staff.setDateOfBirth(OrphanUtils.StringToDate(staffRequest.getDateOfBirth()));
        staff.setTypeStaff(staffRequest.getTypeStaff().equals(TypeStaff.NhanVien) ? TypeStaff.NhanVien : TypeStaff.CanBo);
        staff.setEmail(staffRequest.getEmail());
        staff.setGender(staffRequest.getGender());
        staff.setIdentification(staffRequest.getIdentification());
        staff.setPhone(staffRequest.getPhone());

        staff.setModifiedId(String.valueOf(getCurrentUserId()));

        staff = staffRepository.save(staff);

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

    public UpdateImageResponse updateStaffImage(String image, byte[] procPic, Integer id) throws IOException {

        staffRepository.updateStaffImage(image,procPic,id);
//        ClassPathResource imageFile = new ClassPathResource("/user-photos/" + userId + "/" + image);
//
//        byte[] imageBytes = StreamUtils.copyToByteArray(imageFile.getInputStream());

        UpdateImageResponse updateImageResponse=new UpdateImageResponse();
        updateImageResponse.setImage(image);
        updateImageResponse.setId(id);
        updateImageResponse.setImageFile(procPic);
        return  updateImageResponse;
    }

    public PageInfo<StaffDto> viewAllStaffs(Integer page, Integer limit) throws NotFoundException {
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
        staff.setTypeStaff(staffRequest.getTypeStaff().equals(TypeStaff.NhanVien) ? TypeStaff.NhanVien : TypeStaff.CanBo);
        staff.setEmail(staffRequest.getEmail());
        staff.setGender(staffRequest.getGender());
        staff.setIdentification(staffRequest.getIdentification());
        staff.setPhone(staffRequest.getPhone());
        return staff;
    }

    private StaffDto StaffToStaffDto(Staff staff) {
        StaffDto staffDto = new StaffDto();

        staffDto.setStaffId(staff.getStaffId());
        staffDto.setPhone(staff.getPhone());
        staffDto.setFullName(staff.getFullName());
        staffDto.setImage(staff.getImage());
        staffDto.setProcFile(staff.getProfPic());
        staffDto.setTypeStaff(staff.getTypeStaff().equals(TypeStaff.NhanVien) ? TypeStaff.NhanVien.name() : TypeStaff.CanBo.name());

        return staffDto;
    }

    private StaffDetailDto StaffToStaffDetailDto(Staff staff) {
        StaffDetailDto staffDetailDto = new StaffDetailDto();

        staffDetailDto.setStaffId(staff.getStaffId());
        staffDetailDto.setPhone(staff.getPhone());
        staffDetailDto.setFullName(staff.getFullName());
        staffDetailDto.setImage(staff.getImage());
        staffDetailDto.setProcFile(staff.getProfPic());
        staffDetailDto.setTypeStaff(staff.getTypeStaff().equals(TypeStaff.NhanVien) ? TypeStaff.NhanVien.name() : TypeStaff.CanBo.name());

        staffDetailDto.setAddress(staff.getAddress());
        staffDetailDto.setGender(staff.getGender());
        staffDetailDto.setEmail(staff.getEmail());
        staffDetailDto.setIdentification(staff.getIdentification());

        return staffDetailDto;
    }
}
