package com.imooc.o2o;


import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 配置spring和junit整合，使得junit在启动时加载springIOC容器
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告知junit， spring配置文件的位置
@ContextConfiguration(locations = "classpath:spring/spring-*.xml")
public class BaseTest {
}
