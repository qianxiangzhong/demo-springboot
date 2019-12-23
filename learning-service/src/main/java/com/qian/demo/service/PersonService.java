package com.qian.demo.service;

import com.qian.demo.entity.Person;
import com.qian.demo.mapper.PersonMapper;
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
public class PersonService {
    public static final Logger logger = LoggerFactory.getLogger(PersonService.class);

    @Autowired
    PersonMapper personMapper;

    public Person getPersonById(Integer id) {
        return personMapper.getPersonById(id);
    }

    public List<Person> getPersonsByNameLike(String name) {
        return personMapper.getPersonsByNameLike(name);
    }

    public Person getPersonByIdNo(String idNo) {
        return personMapper.getPersonByIdNo(idNo);
    }

    public Integer savePerson(Person person) {
        return personMapper.savePerson(person);
    }
}