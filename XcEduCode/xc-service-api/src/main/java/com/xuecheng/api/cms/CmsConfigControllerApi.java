package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created By Rambo on 2018/11/15
 */

@Api(value = "cms配置管理接口",description = "cms配置管理接口,提供数据模型管理、查询接口")
public interface CmsConfigControllerApi {
    @ApiOperation("根据id查询cms配置信息")
    public CmsConfig getmodel(String id);
}
