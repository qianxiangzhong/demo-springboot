package com.qian.demo.mapper;

import com.qian.demo.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @author qianxiangzhong
 */
@Repository
public interface UserMapper {
    /**
     * 根据用户名模糊查询用户
     * @param userName
     * @return
     */
    List<User> getUsersByNameLike(String userName);

    /**
     * 保存用户信息
     * @param user
     * @return
     */
    Integer saveUser(User user);
}
