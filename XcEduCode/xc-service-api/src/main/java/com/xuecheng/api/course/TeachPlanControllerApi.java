package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import io.swagger.annotations.ApiOperation;

/**
 * 定义一个Course配置控制层接口，为前端提供规范的后端接口
 */
public interface TeachPlanControllerApi {
    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("通过课程id查询课程计划树")
    public TeachplanNode findTeachplanTreeByCourseId(String courseId);
}
