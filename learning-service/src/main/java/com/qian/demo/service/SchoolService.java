package com.qian.demo.service;

import com.qian.demo.entity.Person;
import com.qian.demo.entity.School;
import com.qian.demo.mapper.SchoolMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: qxz
 * @datetime : 2019/12/19 11:51
 **/
@Service
public class SchoolService {
    public static final Logger logger = LoggerFactory.getLogger(SchoolService.class);

    @Autowired
    SchoolMapper schoolMapper;

    public School getSchoolById(Integer id) {
        return schoolMapper.getSchoolById(id);
    }

    public List<School> getSchoolByNameLike(String name) {
        return schoolMapper.getSchoolByNameLike(name);
    }

    public Integer saveSchool(School school) {
        return schoolMapper.saveSchool(school);
    }
}