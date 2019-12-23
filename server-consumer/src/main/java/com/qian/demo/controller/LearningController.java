package com.qian.demo.controller;

import com.qian.demo.entity.LearningRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @description:
 * @author: qxz
 * @datetime : 2019/12/13 16:22
 **/
@RestController
public class LearningController {
    public static final Logger logger = LoggerFactory.getLogger(LearningController.class);

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/getDefaultRecordFromOther")
    public LearningRecord getLearningRecord (String personId, String schoolId) {
        LearningRecord learningRecord = restTemplate.getForObject("http://learning-service/learning/getDefaultRecord", LearningRecord.class);
        return learningRecord;
    }

}