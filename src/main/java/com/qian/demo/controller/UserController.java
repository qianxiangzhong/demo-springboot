package com.qian.demo.controller;

import com.qian.demo.entity.User;
import com.qian.demo.service.UserService;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author qxz
 */
@Controller
public class UserController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserService userService;

    @RequestMapping("/getUsersByNameLike")
    @ResponseBody
    public List<User> queryUsersByNameLike(String userName) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        logger.info("queryUserLike()");
        List<User> users = userService.getUserByNameLike(userName);
        logger.info("queryUserLike(), users:{}", users);
        return users;
    }

    @RequestMapping("/saveUser")
    @ResponseBody
    public Integer saveUser(User user) {
        Integer count = userService.saveUser(user);
        Integer id = user.getId();
        logger.info("count:{}, id:{}", count, id);
        return id;
    }
}