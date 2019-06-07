package com.xuecheng.manage_course.service;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_course.dao.TeachPlanMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class TeachPlanService {

    @Autowired
    private TeachPlanMapper teachPlanMapper;

    /**
     * 根据课程Id查询课程计划列表
     * @param courseId String 课程Id值
     * @return TeachplanNode 课程计划数据对象
     */
    public TeachplanNode findTeachplanTreeByCourseId(String courseId) {
        //判断CourseId
        if(StringUtils.isBlank( courseId ))
            ExceptionCast.cast( CommonCode.INVALID_PARAM );

        //获得课程计划
        TeachplanNode node = teachPlanMapper.findTeachPlanTreeByCourseId( courseId );

        //判断课程计划
        if(ObjectUtils.isEmpty( node ))
            ExceptionCast.cast( CourseCode.COURSE_PLAN_DATAISNULL );


        return node;
    }

}
