package com.qian.demo.mapper;

import com.qian.demo.entity.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: qxz
 * @datetime : 2019/12/18 18:30
 **/
@Repository
public interface PersonMapper {

    /**
     * 根据主键获取
     * @param id
     * @return
     */
    Person getPersonById(Integer id);

    /**
     * 根据身份证号获取
     * @param idNo
     * @return
     */
    Person getPersonByIdNo(String idNo);

    /**
     * 根据名字模糊查询
     * @param name
     * @return
     */
    List<Person> getPersonsByNameLike(String name);

    /**
     * 保存Person
     * @param person
     * @return
     */
    Integer savePerson(Person person);
}
