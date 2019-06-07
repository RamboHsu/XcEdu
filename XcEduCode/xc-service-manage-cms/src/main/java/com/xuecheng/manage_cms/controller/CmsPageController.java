package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CmsPageController implements CmsPageControllerApi {
    //为了方便Api接口访问地址的统一性和维护性，编写接口路径常亮值
    public final String API_URL="/cms/page";

    @Autowired
    private PageService pageService;

    @Override
    @GetMapping(API_URL+"/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {

       return pageService.findList(page,size,queryPageRequest);
    }

    @Override
    @PostMapping(API_URL+"/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return pageService.add(cmsPage);
    }

    @Override
    @GetMapping(API_URL+"/get/{id}")
    public CmsPageResult findById(@PathVariable("id") String id) {
        return pageService.getById(id);
    }

    @Override
    @GetMapping(API_URL+"/findByPageName/{pageName}")
    public CmsPageResult findByPageName(@PathVariable("pageName") String pageName) {
        return pageService.findByPageName(pageName);
    }

    @Override
    @PutMapping(API_URL+"/edit/{id}")
    public CmsPageResult edit(@PathVariable("id") String id, @RequestBody CmsPage cmsPage) {
        return pageService.update(id,cmsPage);
    }

    @Override
    @DeleteMapping(API_URL+"/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id) {
        return pageService.delete(id);
    }
}
