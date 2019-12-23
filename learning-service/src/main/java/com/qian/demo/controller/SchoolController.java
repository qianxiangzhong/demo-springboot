package com.qian.demo.controller;

import com.qian.demo.entity.School;
import com.qian.demo.service.SchoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @description:
 * @author: qxz
 * @datetime : 2019/12/19 11:35
 **/
@RequestMapping("/school")
@Controller
public class SchoolController {
    public static final Logger logger = LoggerFactory.getLogger(SchoolController.class);

    @Autowired
    SchoolService schoolService;

    /**
     * 跳转新增页面
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "pages/editSchool";
    }

    /**
     * 根据主键获取
     * @param id
     * @return
     */
    @RequestMapping("/getSchoolById")
    @ResponseBody
    public School getSchoolById(Integer id) {
        return schoolService.getSchoolById(id);
    }

    /**
     * 根据学校名称模糊查询
     * @param name
     * @return
     */
    @RequestMapping("/getSchoolByNameLike")
    @ResponseBody
    public List<School> getSchoolByNameLike(String name) {
        return schoolService.getSchoolByNameLike(name);
    }

    /**
     * 保存学校信息
     * @param school
     * @return
     */
    @RequestMapping("/saveSchool")
    @ResponseBody
    public Integer saveSchool(School school) {
        return schoolService.saveSchool(school);
    }

}