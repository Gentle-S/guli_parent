package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.entity.subject.OneSubject;
import com.atguigu.eduservice.entity.subject.TwoSubject;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.GuliException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-02-29
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService subjectService) {
        try {
            //文件输入流
            InputStream in = file.getInputStream();
            //调用方法进行读取
            EasyExcel.read(in, SubjectData.class,new SubjectExcelListener(subjectService)).sheet().doRead();
        }catch(Exception e){
            e.printStackTrace();
            throw new GuliException(20002,"添加课程分类失败");
        }
    }

    //课程分类列表(树形)
    @Override
    public List<OneSubject> getAllOneTwoSubject() {
        //查询所有一级分类
        QueryWrapper wrapper1=new QueryWrapper();
        wrapper1.eq("parent_id",0);
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapper1);

        //查询所有二级分类
        QueryWrapper wrapper2=new QueryWrapper();
        wrapper2.ne("parent_id",0);
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapper2);
        for (EduSubject s:twoSubjectList
        ) {
            System.out.println(s);
        }
        //封装一级分类
        //创建list集合用于储存封装最终数据

        List<OneSubject> finalSubjectList=new ArrayList<>();
        //将查询出来的所有一级分类的list集合遍历，得到每一个一级分类对象，获取每一个分类对象值
        for (int i = 0; i <oneSubjectList.size() ; i++) {
            EduSubject eduSubject=oneSubjectList.get(i);
            OneSubject oneSubject=new OneSubject();
            //通过spring提供的BeanUtils.copyProperties 实现封装,相当于以下两行代码
            //oneSubject.setId(eduSubject.getId());
           // oneSubject.setTitle(eduSubject.getTitle());
            BeanUtils.copyProperties(eduSubject,oneSubject);
            // 封装到要求的list集合， List<OneSubject> finalSubjectList
            finalSubjectList.add(oneSubject);
            //封装二级分类

            List<TwoSubject> twoFinalSubjectList=new ArrayList<>();
            //判断该二级分类是否为该一级分类下属分类
            for (int j = 0; j <twoSubjectList.size() ; j++) {
                EduSubject teduSubject=twoSubjectList.get(j);
                if (teduSubject.getParentId().equals(eduSubject.getId())){
                    TwoSubject twoSubject=new TwoSubject();
                    BeanUtils.copyProperties(teduSubject,twoSubject);
                    // 封装到要求的list集合，   List<TwoSubject> twoFinalSubjectList
                    twoFinalSubjectList.add(twoSubject);
                }

            }
            //将封装的list集合封装到children属性里面
            oneSubject.setChildren(twoFinalSubjectList);
        }



        return finalSubjectList;

    }

}
