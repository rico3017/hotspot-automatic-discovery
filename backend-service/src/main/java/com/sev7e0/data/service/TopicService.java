package com.sev7e0.data.service;

import com.sev7e0.data.HotSpot;
import com.sev7e0.data.dao.TopicRepository;
import com.sev7e0.data.model.Topic;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Topic Controller
 */
@Slf4j
@Service
public class TopicService {

    @Autowired
    TopicRepository repository;

    @HotSpot(name = "Topic")
    public List<Topic> findAllByTitle(@NonNull String title) {
        List<Topic> allByTitle = repository.findAllByTitle(title);
        return allByTitle;
    }
}
