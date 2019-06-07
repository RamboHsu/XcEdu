package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsPageParam;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmsPageRepositoryTest {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    //分页测试
    @Test
    public void testFindPage() {
        int page = 0;//从0开始
        int size = 10;//每页记录数
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<CmsPage> all = cmsPageRepository.findAll(pageRequest);
        System.out.println(all);
    }

    //添加
    @Test
    public void testInsert() {
        //定义页面管理实体类
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("s01");
        cmsPage.setTemplateId("t01");
        cmsPage.setPageName("测试页面");
        cmsPage.setPageCreateTime(new Date());

        //定义页面管理参数列表实体类
        List<CmsPageParam> cmsPageParams = new ArrayList<>();
        CmsPageParam cmsPageParam = new CmsPageParam();
        cmsPageParam.setPageParamName("param1");
        cmsPageParam.setPageParamValue("value1");
        cmsPageParams.add(cmsPageParam);

        //将页面管理参数添加到页面管理中
        cmsPage.setPageParams(cmsPageParams);

        //保存页面管理对象
        cmsPageRepository.save(cmsPage);
        System.out.println(cmsPage);

    }

    //删除
    @Test
    public void testDelete() {
        cmsPageRepository.deleteById("5be14b84207a682fbcf893ab");
        System.out.println("删除成功！");
    }

    //修改
    @Test
    public void testUpdate() {
        Optional<CmsPage> optional = cmsPageRepository.findById("5be14d01207a681b8c38a968");
        if (optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            cmsPage.setPageName("测试页面01");
            cmsPageRepository.save(cmsPage);
            System.out.println("更新成功！");
        }
    }

    @Test
    public void testCusQuery() {
        CmsPage cmsPage = this.cmsPageRepository.findByPageName("测试页面01");
        System.out.println(cmsPage);
    }

    @Test
    public void testFindAll() {
        //1.创建条件查询匹配器
        ExampleMatcher matcher = ExampleMatcher.matching();
        //1.1给页面别名属性添加模糊匹配
        matcher = matcher.withMatcher("pageAlias", ExampleMatcher.GenericPropertyMatchers.contains());
        //2.创建查询条件实体对象
        CmsPage cmsPage = new CmsPage();
        //2.1站点id
        cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");
        //2.2模板id
        cmsPage.setTemplateId("5a962c16b00ffc514038fafd");
        //2.3页面别名
        cmsPage.setPageAlias("分类");
        //3.根据匹配器和查询条件对象创建出条件查询对象
        Example<CmsPage> example = Example.of(cmsPage, matcher);
        //4.创建分页对象
        Pageable pageable = PageRequest.of(0, 10);
        //5.使用条件查询对象和分页对象查询分页数据
        Page<CmsPage> pages = cmsPageRepository.findAll(example, pageable);
        //6.打印查询数据
        System.out.println(pages.getContent());
    }



}
