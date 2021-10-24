package com.byc.community.community.dto;

import com.byc.community.community.model.User;
import lombok.Data;

@Data
/*为了调用user的头像必须封装user，但数据库中没有user，所以定义一个dto*/
public class QuestionDTO {

    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User  user;
}