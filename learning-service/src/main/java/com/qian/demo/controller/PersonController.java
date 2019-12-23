package com.qian.demo.controller;

import com.qian.demo.entity.Person;
import com.qian.demo.service.PersonService;
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
@RequestMapping("/person")
@Controller
public class PersonController {
    public static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    PersonService personService;

    /**
     * 跳转新增页面
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "pages/editPerson";
    }

    /**
     * 根据人员姓名模糊查询
     * @param name
     * @return
     */
    @RequestMapping("getPersonsByNameLike")
    @ResponseBody
    public List<Person> getPersonsByNameLike(String name) {
        return personService.getPersonsByNameLike(name);
    }

    /**
     * 根据主键获取
     * @param id
     * @return
     */
    @RequestMapping("getPersonById")
    @ResponseBody
    public Person getPersonById(Integer id) {
        return personService.getPersonById(id);
    }

    /**
     * 根据身份证号获取
     * @param idNo
     * @return
     */
    @RequestMapping("getPersonByIdNo")
    @ResponseBody
    public Person getPersonByIdNo(String idNo) {
        return personService.getPersonByIdNo(idNo);
    }

    /**
     * 保存人员信息
     * @param person
     * @return
     */
    @RequestMapping("savePerson")
    @ResponseBody
    public Integer savePerson(Person person) {
        return personService.savePerson(person);
    }


}