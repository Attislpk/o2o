package com.imooc.o2o.service.impl;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerTest {
    Logger logger = LoggerFactory.getLogger(LoggerTest.class);


    @Test
    public void LoggerTest(){
        logger.info("测试");

    }
}
