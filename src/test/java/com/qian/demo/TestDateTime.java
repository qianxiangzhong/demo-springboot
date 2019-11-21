package com.qian.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class TestDateTime {
    Logger logger = LoggerFactory.getLogger(TestDateTime.class);

    @Test
    public void test() {
        LocalDate d1 = LocalDate.now();
        LocalDate d2 = LocalDate.of(2019, 11, 20);
        LocalDate d3 = LocalDate.of(2019, Month.NOVEMBER, 20);
        LocalDate d4 = LocalDate.ofEpochDay(0);
        LocalDate d5 = LocalDate.parse("2019-11-20");
        d5 = d5.plus(1, ChronoUnit.DECADES);
        logger.info("d1:{}, d2:{}, d3:{}, d4:{}, d5:{}", d1, d2, d3, d4, d5);
        LocalTime t1 = LocalTime.now();
        LocalTime t2 = LocalTime.of(14, 30, 30);
        LocalTime t3 = LocalTime.of(14, 30, 30, 500);
        LocalTime t4 = LocalTime.parse("14:30:30");
        logger.info("t1:{}, t2:{}, t3:{}, t4:{}", t1, t2, t3, t4);

        LocalDateTime dt1 = LocalDateTime.now();
        LocalDateTime dt2 = LocalDateTime.of(2019, 11, 20, 11, 59, 59);
        dt2.withDayOfMonth(21);
        // 以1970-01-01T00:00:00为基准，往后推移特定秒、纳秒、偏移量（ZoneOffset.MIN:-18个小时对应的秒数）
        LocalDateTime dt3 = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.MIN);
        // ZoneOffset.MAX:18个小时对应的秒数
        LocalDateTime dt4 = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.MAX);
        ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(1 * 60 * 60);
        LocalDateTime dt5 = LocalDateTime.ofEpochSecond(0, 0, zoneOffset);
        LocalDateTime dt6 = LocalDateTime.parse("2019-11-20T14:01:01");
        logger.info("dt1:{}, dt2:{}, dt3:{}, dt4:{}, dt5:{} dt6:{}", dt1, dt2, dt3, dt4, dt5, dt6);
        dt6 = dt6.with(ChronoField.DAY_OF_MONTH, 1);
        logger.info("dt6:{}, dateStr:{}", dt6, dt6.format(DateTimeFormatter.BASIC_ISO_DATE));
        LocalDateTime dt7 = LocalDateTime.now();
        logger.info("dt7:{}, dateStr:{}", dt7, dt7.format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS.n")));
        LocalDateTime dt8 = LocalDateTime.now();
        logger.info("dt8.equals(dt7):{}", dt8.equals(dt7));
        Instant i = Instant.now();
        logger.info("epochSecond:{}, epochMilliSecond:{}, nanos:{}", i.getEpochSecond(), i.toEpochMilli(), i.getNano());
        YearMonth ym1 = YearMonth.now();
        MonthDay md1 = MonthDay.now();
        MonthDay md2 = MonthDay.of(11, 20);
        logger.info("ym1:{}, md1:{}, md1.equals(md2):{}", ym1, md1, md1.equals(md2));
    }
}