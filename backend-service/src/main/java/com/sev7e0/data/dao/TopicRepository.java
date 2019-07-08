package com.sev7e0.data.dao;

import com.sev7e0.data.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, String> {

    List<Topic> findAllByTitle(String title);

    List<Topic> findAllByTitle(List<String> title);

}
