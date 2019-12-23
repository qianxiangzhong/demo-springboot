package com.qian.demo.controller;

import com.qian.demo.entity.LearningRecord;
import com.qian.demo.service.LearningRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @author qxz
 */
@Controller
@RequestMapping("/learning")
public class LearningController {

    public static final Logger logger = LoggerFactory.getLogger(LearningController.class);
    @Autowired
    LearningRecordService learningRecordService;
    @Autowired
    LearningRecord learningRecord;

    /**
     * 方法上加入 @ExceptionHandler 注解即可，这样，controller发生异常会自动调用该方法。
     * 无需每个方法都添加 try catch
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public String doError(Exception ex) throws Exception {
        String msg = ex.getLocalizedMessage();
        logger.error("doError(), msg:{}", msg);
        return "系统错误，请联系管理员";
    }


    @RequestMapping("/queryRecordsBySchoolAndPerson")
    @ResponseBody
    public List<LearningRecord> queryRecordsBySchoolAndPerson(String schoolName, String personName) {
        logger.info("queryRecordsBySchoolAndPerson()");
        List<LearningRecord> users = learningRecordService.getLearningRecords(schoolName, personName);
        logger.info("queryRecordsBySchoolAndPerson(), users:{}", users);
        return users;
    }

    @RequestMapping("/index")
    public String index() {
        return "pages/editLearningRecord";
    }

    @RequestMapping("/getDefaultRecord")
    @ResponseBody
    public LearningRecord getDefaultRecord() {
        return learningRecord;
    }

    /**
     * 保存学习记录数据，使用Java的校验工具
     * @param learningRecord
     * @param bindingResult
     * @return
     */
    @RequestMapping("/saveLearningRecord")
    @ResponseBody
    public String saveLearningRecord(@Valid LearningRecord learningRecord, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            errors.forEach(fieldError -> errorMsg.append(fieldError.getDefaultMessage()));
            return errorMsg.toString();
        }else{
            Integer count = learningRecordService.saveLearningRecord(learningRecord);
            Integer id = learningRecord.getId();
            logger.info("count:{}, id:{}", count, id);
            return id.toString();
        }
    }

}