package com.qian.demo.mapper;

import com.qian.demo.entity.LearningRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author qianxiangzhong
 */
@Repository
public interface LearningRecordMapper {
    /**
     * 根据学校和人员获取学历记录
     * @param schoolName
     * @param personName
     * @return
     */
    List<LearningRecord> getLearningRecordBySchoolAndPerson(String schoolName, String personName);

    /**
     * 保存学习记录
     * @param learningRecord
     * @return
     */
    Integer saveLearningRecord(LearningRecord learningRecord);
}