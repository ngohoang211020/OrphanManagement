package com.orphan.common.service;

import com.orphan.api.controller.profile.dto.NotificationDto;
import com.orphan.common.entity.UserNotifyEntity;
import com.orphan.common.repository.UserNotifyRepository;
import com.orphan.utils.OrphanUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserNotifyService extends BaseService {
    private final UserNotifyRepository userNotifyRepository;

    public List<NotificationDto> findListNotifyById() {

        List<UserNotifyEntity> userNotifyEntities = userNotifyRepository.findByUser_LoginIdOrderByDateSendDesc(getCurrentUserId());
        if (userNotifyEntities.isEmpty()) {
            return null;
        } else {
            return userNotifyEntities.stream().map(userNotifyEntity -> toDto(userNotifyEntity)).collect(Collectors.toList());
        }
    }

    //mapper
    private NotificationDto toDto(UserNotifyEntity userNotifyEntity) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setUserId(userNotifyEntity.getUser().getLoginId());
        notificationDto.setSubject(userNotifyEntity.getSubject());
        notificationDto.setContent(userNotifyEntity.getContent());
        notificationDto.setDateSend(OrphanUtils.DateTimeToString(userNotifyEntity.getDateSend()));
        notificationDto.setSenderId(userNotifyEntity.getSender().getLoginId());
        notificationDto.setId(userNotifyEntity.getId());
        return notificationDto;
    }
}
