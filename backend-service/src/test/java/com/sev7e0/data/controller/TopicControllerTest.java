package com.sev7e0.data.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;


@Slf4j
public class TopicControllerTest {


    @Test
    public void search() {
        TopicController topicController = new TopicController();
        while (true) {
            topicController.search("test");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
