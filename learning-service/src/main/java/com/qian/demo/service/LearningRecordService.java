package com.qian.demo.service;

import com.qian.demo.entity.LearningRecord;
import com.qian.demo.entity.Person;
import com.qian.demo.mapper.LearningRecordMapper;
import com.qian.demo.mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author qianxiangzhong
 */
@Service
public class LearningRecordService {
    @Autowired
    LearningRecordMapper learningRecordMapper;
    @Autowired
    PersonMapper personMapper;

    public List<LearningRecord> getLearningRecords(String schoolName, String personName) {
        return learningRecordMapper.getLearningRecordBySchoolAndPerson(schoolName, personName);
    }

    public Integer saveLearningRecord(LearningRecord learningRecord) {
        Integer id = learningRecordMapper.saveLearningRecord(learningRecord);
        return id;
    }
}
