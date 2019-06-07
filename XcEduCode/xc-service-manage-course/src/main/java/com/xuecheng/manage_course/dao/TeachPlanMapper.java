package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeachPlanMapper {
    TeachplanNode findTeachPlanTreeByCourseId(String courseId);
}