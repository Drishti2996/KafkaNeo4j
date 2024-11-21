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
@Slf4j // Integrates a logging framework (e.g., Logback or Log4j) with the class,
// allowing to use log for logging messages.
public class StartupKafka {

    @Value("${spring.kafka.topic-4}")
    private String topic4;

    //Injects the KafkaConfiguration bean to access Kafka-related configurations,
    // such as creating topics programmatically.
    @Autowired
    KafkaConfiguration kafkaConfiguration;

    @Autowired
    Producer producer;

    //Injects a service class (AdminTopic) that handles administrative tasks related to Kafka,
    // such as creating and managing topics.
    @Autowired
    AdminTopic adminTopic;

    //Listens for the ApplicationReadyEvent, which is triggered after the Spring application
    // context is initialized and the application is fully ready.
    @EventListener(ApplicationReadyEvent.class)
    void produceMessages(){
        var userTopic = kafkaConfiguration.topicUser();
        adminTopic.createTopic(userTopic);
    }



}
