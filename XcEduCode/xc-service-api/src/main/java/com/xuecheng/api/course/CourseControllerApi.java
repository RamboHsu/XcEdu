package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 定义一个Cms配置控制层接口，为前端提供规范的后端接口
 */
@Api(value = "课程管理接口",description = "课程管理接口，提课程的新增，修改，营销")
public interface CourseControllerApi {

    //查询课程列表
    @ApiOperation("查询我的课程列表")
    public QueryResponseResult findCourseList(
            int page,//页码
            int size,//每页显示条数
            CourseListRequest courseListRequest//查询条件
    );



}
