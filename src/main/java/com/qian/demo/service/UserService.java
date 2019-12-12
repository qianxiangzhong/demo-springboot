package com.qian.demo.service;

import com.qian.demo.entity.User;
import com.qian.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author qianxiangzhong
 */
@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    public List<User> getUserByNameLike(String userName) {
        return userMapper.getUsersByNameLike(userName);
    }

    public Integer saveUser(User user) {
        Integer id = userMapper.saveUser(user);
        return id;
    }
}
