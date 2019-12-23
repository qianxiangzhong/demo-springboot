package com.qian.demo.mapper;

import com.qian.demo.entity.School;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: qxz
 * @datetime : 2019/12/19 13:05
 **/
@Repository
public interface SchoolMapper {

    /**
     * 根据主键查询
     * @param id
     * @return
     */
    School getSchoolById(Integer id);

    /**
     * 根据名称模糊查询
     * @param name
     * @return
     */
    List<School> getSchoolByNameLike(String name);

    /**
     * 保存学校信息
     * @param school
     * @return
     */
    Integer saveSchool(School school);
}