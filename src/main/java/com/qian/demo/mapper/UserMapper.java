package com.qian.demo.mapper;

import com.qian.demo.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserMapper {
    List<User> getUsersByNameLike(String userName);
    Integer saveUser(User user);
}
