package com.sev7e0.data.controller;

import com.sev7e0.data.model.Topic;
import com.sev7e0.data.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Topic Controller
 */
@RestController()
@RequestMapping("/topic")
public class TopicController {

    @Autowired
    TopicService topicService;

    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Topic> search(@RequestParam(value = "title") String title) {
        return topicService.findAllByTitle(title);
    }
}
