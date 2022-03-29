package com.orphan.common.service;

import com.orphan.common.repository.ChildrenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChildrenService {
    @Autowired
    private ChildrenRepository childrenRepository;


}
