package com.example.kafka;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.example.kafka.config.KafkaConfiguration;
import com.example.kafka.models.User;
import com.example.kafka.service.AdminTopic;
import com.example.kafka.service.Producer;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StartupKafka {

    @Value("${spring.kafka.topic-4}")
    private String topic4;

    @Autowired
    KafkaConfiguration kafkaConfiguration;
    @Autowired
    Producer producer;

    @Autowired
    AdminTopic adminTopic;

    @EventListener(ApplicationReadyEvent.class)
    void produceMessages(){
        var userTopic = kafkaConfiguration.topicUser();
        adminTopic.createTopic(userTopic);
    }



}
