package com.atguigu.demo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

//AnalysisEventListener:该类由EasyExcel提供的一个类，
public class ExcelListener extends AnalysisEventListener<DemoData> {
    //EasyExcel一行一行的读取数据
    @Override
    public void invoke(DemoData demoData, AnalysisContext analysisContext) {
        System.out.println("*******"+demoData+"*********");
    }
    //重写父类的invokeHeadMap方法实现读取表头的内容：
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头："+headMap);
    }
    //读取完成之后
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
