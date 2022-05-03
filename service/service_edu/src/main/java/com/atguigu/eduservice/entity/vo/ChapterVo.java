package com.atguigu.eduservice.entity.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "章节信息")
@Data
public class ChapterVo {
    private String id;
    private String title;
    private List<VideoVo> children = new ArrayList<>();
}
