package com.orphan.common.service;

import com.orphan.api.controller.manager.introducer.dto.IntroducerDetailDto;
import com.orphan.common.repository.OrphanIntroducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrphanIntroducerService extends BaseService{

    private static OrphanIntroducerRepository orphanIntroducerRepository;

//    public IntroducerDetailDto createIntroducer(IntroducerDetailDto introducerInfo){
//
//    }

}
