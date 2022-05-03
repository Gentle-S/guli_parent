package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-04-17
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
   @Autowired
   private EduCourseDescriptionService eduCourseDescriptionService;

    //添加课程基本信息的方法
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1.向课程表添加课程基本信息
        //CourseInfoVo对象转换eduCourse对象==> BeanUtils.copyProperties
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert <= 0) {
            //添加失败
            throw new GuliException(20001, "添加课程信息失败");
        }

            String descriptionId = eduCourse.getId();
            //2.向课程简介表添加课程简介
            EduCourseDescription eduCourseDescription = new EduCourseDescription();
            eduCourseDescription.setDescription(courseInfoVo.getDescription());
            eduCourseDescription.setId(descriptionId);
            boolean save = eduCourseDescriptionService.save(eduCourseDescription);
            if (!save) {
                throw new GuliException(20001, "添加课程详情失败");
            }
            return descriptionId;
        }


    @Override
    public CourseInfoVo getCourseInfo(String courseId) {
        //1.查询课程表
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo=new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);
        //2.查询描述表
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(courseId);
        BeanUtils.copyProperties(eduCourseDescription,courseInfoVo);
        return courseInfoVo;
    }

    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //1.修改课程表
        EduCourse eduCourse=new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int i = baseMapper.updateById(eduCourse);
        if (i==0){
            throw  new GuliException(20001,"修改课程信息失败!");
        }
        //2.修改描述表
        EduCourseDescription eduCourseDescription=new EduCourseDescription();
        BeanUtils.copyProperties(courseInfoVo,eduCourseDescription);
        boolean b = eduCourseDescriptionService.updateById(eduCourseDescription);
        if(!b){
            throw new GuliException(20001,"修改课程描述失败!");
        }

    }
}
