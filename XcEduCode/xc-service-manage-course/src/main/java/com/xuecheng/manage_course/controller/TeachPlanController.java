package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.TeachPlanControllerApi;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.manage_course.service.TeachPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course/teachPlan")
public class TeachPlanController implements TeachPlanControllerApi {

    @Autowired
    private TeachPlanService teachPlanService;


    @Override
    @GetMapping("/findPlanList/{courseId}")
    public TeachplanNode findTeachplanTreeByCourseId(@PathVariable("courseId") String courseId) {
        TeachplanNode teachplanNode = teachPlanService.findTeachplanTreeByCourseId( courseId );
        return teachplanNode;
    }

    @Override

    public TeachplanNode findTeachplanList(String courseId) {
        return null;
    }
}