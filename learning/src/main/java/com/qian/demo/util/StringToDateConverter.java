package com.qian.demo.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author qianxiangzhong
 */
@Component
public class StringToDateConverter implements Converter<String, LocalDate> {

    private static final Map<String, String> FORMATS = new HashMap<String, String>();

    static {
        FORMATS.put("^\\d{4}-\\d{1,2}$", "yyyy-MM");
        FORMATS.put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
        FORMATS.put("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$","yyyy-MM-dd hh:mm");
        FORMATS.put("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$","yyyy-MM-dd hh:mm:ss");
    }

    @Override
    public LocalDate convert(String source) {
        if (source == null || source == "") {
            return null;
        }
        for (Map.Entry<String, String> entry : FORMATS.entrySet()) {
            String regex = entry.getKey();
            String pattern = entry.getValue();
            if (source.matches(regex)) {
                return LocalDate.parse(source,DateTimeFormatter.ofPattern(pattern));
            }
        }
        return null;
    }
}
