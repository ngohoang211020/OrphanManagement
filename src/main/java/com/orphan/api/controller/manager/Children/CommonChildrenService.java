package com.orphan.api.controller.manager.Children;


import com.orphan.common.entity.Children;
import com.orphan.utils.OrphanUtils;

public class CommonChildrenService {
    public static ChildrenCommonDto childrenToChildrenCommonDto(Children children){
        ChildrenCommonDto childrenCommonDto=new ChildrenCommonDto();
        childrenCommonDto.setChildrenId(children.getId());
        childrenCommonDto.setIntroductoryDate(OrphanUtils.DateToString(children.getIntroductoryDate()));
        childrenCommonDto.setGender(children.getGender());
        childrenCommonDto.setDateOfBirth(OrphanUtils.DateToString(children.getDateOfBirth()));
        childrenCommonDto.setFullName(children.getFullName());
        children.setStatus(children.getStatus());
        return  childrenCommonDto;
    }
}
