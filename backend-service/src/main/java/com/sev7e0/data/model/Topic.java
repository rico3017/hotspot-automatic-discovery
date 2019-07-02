package com.sev7e0.data.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Topic {

    @Id
    @GeneratedValue
    private String topic_id;
    private String batch_no;
    private String topmark;
    private String title;
    private String description;
    private String discuss;
    private String read;
    private String url;
}
