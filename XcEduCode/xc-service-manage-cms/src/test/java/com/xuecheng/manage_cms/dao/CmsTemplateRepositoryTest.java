package com.xuecheng.manage_cms.dao;

import com.xuecheng.manage_cms.service.PageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created By Rambo on 2018/11/20
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsTemplateRepositoryTest {
    @Autowired
    PageService pageService;
    @Test
    public void testGetPageHtml(){
        String pageHtml = pageService.getPageHtml("5bf1939e112a4a1148ddcb86");
        System.out.println(pageHtml);
    }
}
