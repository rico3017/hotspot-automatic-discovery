package com.sev7e0.data.model;

import lombok.Data;

@Data
public class Weibo {

    private String weiboId;

    private String batchNo;

    private String title;

    private String weiboDatetime;

    private String content;

    private String userId;

    private String userName;

    private String userGender;

    private String userVerified;

    private String userFollowers;

    private String userFollow;

    private String reposts;

    private String coments;

    private String attitudes;
}
