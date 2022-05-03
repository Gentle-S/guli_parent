package com.atguigu.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;

public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {
    //因为SubjectExcelListener不能交给spring进行管理，所以需要自己new对象，不能注入其他对象
    //不能实现操作数据库
    //通过构造有参构造方法实现EduSubjectService注入
    public EduSubjectService eduSubjectService;

    public SubjectExcelListener(){ }
    //创建有参构造函数，传递subjectService用于操作数据库
    public SubjectExcelListener(EduSubjectService subjectService){
        this.eduSubjectService=subjectService;
    }
    //一行一行读取excel内容
    @Override
    public void invoke(SubjectData subjectData, AnalysisContext analysisContext) {
        System.out.println(subjectData.getOneSubjectName());
            if(subjectData == null){
                throw new GuliException(20001,"数据文件为空");
            }
            //一行一行的读取，每次读取两个值，第一个值一级分类，第二个值二级分类
            //判断一级分类是否重复
        EduSubject existOneSubject = this.existOneSubject(eduSubjectService, subjectData.getOneSubjectName());

        if(existOneSubject == null){
            existOneSubject=new EduSubject();
            existOneSubject.setParentId("0");
            existOneSubject.setTitle(subjectData.getOneSubjectName());//一级分类名称
            System.out.println(existOneSubject);
            boolean save = eduSubjectService.save(existOneSubject);

        }
        //获取一级分类的值，传入二级分类作为其ParentId
        String pid=existOneSubject.getId();
        //添加二级分类，判断二级分类是否重复添加
        EduSubject existTwoSubject = this.existTwoSubject(eduSubjectService, subjectData.getTwoSubjectName(), pid);
        System.out.println(existTwoSubject);
        if(existTwoSubject == null){
            existTwoSubject=new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(subjectData.getTwoSubjectName());//二级分类名称
            eduSubjectService.save(existTwoSubject);
        }

    }

        //判断一级分类不能重复添加
        public  EduSubject existOneSubject(EduSubjectService eduSubjectService,String name){
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("title",name);
            wrapper.eq("parent_id","0");
            EduSubject oneSubject = eduSubjectService.getOne(wrapper);
            return oneSubject;
        }
        //判断二级分类不能重复添加
        public  EduSubject existTwoSubject(EduSubjectService eduSubjectService,String name,String pid){
            QueryWrapper wrapper=new QueryWrapper();
            wrapper.eq("title",name);
            wrapper.eq("parent_id",pid);
            EduSubject twoSubject = eduSubjectService.getOne(wrapper);
            return twoSubject;
        }
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
