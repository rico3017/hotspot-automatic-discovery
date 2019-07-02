package com.sev7e0.data.model;

import lombok.Data;

@Data
public class Weibo {

    private String created_at;
    private int id;
    private String mid;
    private String topic;
    private String idstr;
    private String text;
    private String source;
    private boolean favorited;
    private boolean truncated;
    private String in_reply_to_status_id;
    private String in_reply_to_user_id;
    private String in_reply_to_screen_name;
    private String thumbnail_pic;
    private String bmiddle_pic;
    private String original_pic;
    private String geo;


}
