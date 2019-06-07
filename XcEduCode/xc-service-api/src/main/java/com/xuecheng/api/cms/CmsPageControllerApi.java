package com.xuecheng.api.cms;


import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.*;

/**
 * 定义一个Cms配置控制层接口，为前端提供规范的后端接口
 * @author Rambo
 */
@Api(value = "cms页面管理接口",description = "cms页面管理接口，提供页面的增删改查")
public interface CmsPageControllerApi {
    /**
     * 根据条件分页查询CmsPage页面信息集合
     * @param page  页码
     * @param size  每页记录数
     * @param queryPageRequest  查询页面的返回结果
     * @return
     */
    @ApiOperation("分页查询页面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "页码",required = true,paramType = "path",dataType = "int"),
            @ApiImplicitParam(name = "size",value = "每页记录数",required = true,paramType = "path",dataType = "int")
    })
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);


    /**
     * 添加页面
     * @param cmsPage 要新增的cmsPage数据
     * @return  CmsPageResult Cms定义的规范响数据
     */
    @ApiOperation("添加页面")
    public CmsPageResult add(CmsPage cmsPage);


    /**
     * 根据Id查询CmsPage数据
     * @param id -- String  CmsPage的Id值
     * @return CmsPageResult Cms定义的规范响数据
     */
    @ApiOperation("通过Id查询页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id值",required = true,paramType = "path",dataType = "String")
    })
    public CmsPageResult findById(String id);



    /**
     * 根据pageName查询CmsPage数据
     * @param pageName -- String  CmsPage的pageName值
     * @return CmsPageResult Cms定义的规范响数据
     */
    @ApiOperation("通过pageName查询页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageName",value = "pageName",required = true,paramType = "path",dataType = "String")
    })
    public CmsPageResult findByPageName(String pageName);




    /**
     * 根据CmsPage的Id值修改CmsPage数据
     * @param id   String  CmsPage的Id值
     * @param cmsPage 要修改的CmsPage数据
     * @return CmsPageResult Cms定义的规范响数据
     */
    @ApiOperation("修改页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id值",required = true,paramType = "path",dataType = "String")
    })
    public CmsPageResult edit(String id,CmsPage cmsPage);


    /**
     * 根据id删除页面
     * @param id    String  CmsPage的Id值
     * @return ResponseResult Api定义的通用规范响数据
     */
    @ApiOperation("删除页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "id值",required = true,paramType = "path",dataType = "String")
    })
    public ResponseResult delete(String id);
}
