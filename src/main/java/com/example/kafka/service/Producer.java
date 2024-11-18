package com.example.kafka.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.juli.logging.Log;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.example.kafka.models.User;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Producer {

    @Value("${spring.kafka.topic-4}")
    private String topic4;

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    public void produceMessage(final User user) {
        sendUserMessage(user, topic4);
    }

    public void sendUserMessage(User message, String topic) {
        List <Header> headers = new ArrayList<>();
        headers.add(new RecordHeader("kafkaProject", "kafka".getBytes()));
        ProducerRecord <String, User> record = new ProducerRecord<>(topic, null, message.getName(), message, headers);
        log.info(String.format("Producing message -> %s", message));
        kafkaTemplate.send( record);
    }
}
