package com.atguigu.demo.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

public class TestEasyExcel {
    public static void main(String[] args) {
        //实现Excel写的操作
        //1.设置写入文件夹地址和Excel文件名称
        String filename="F:\\write.xlsx";

        //2.调用easyExcel里面的方法实现写的操作
        // write方法里面有两个参数，第一个参数文件路径名称，第二个参数实体类class
        // sheet()设置sheet名称
        //dowrite()传入写入数据
        //EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());

        //3.实现Excel的读取操作
        //read()方法里面有三个参数，第一个参数文件路径名称，第二个参数实体类class，第三个参数new 一个监听器
        //doread()读取数据
        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();
    }
    //写一个方法返回一个List集合
    private static List<DemoData> getData(){
        List<DemoData> list=new ArrayList<DemoData>();
        for (int i = 0; i < 10; i++) {
            DemoData demoData=new DemoData();
            demoData.setSname("lucy"+i);
            demoData.setSno(i);
            list.add(demoData);
        }
        return list;
    }

}
