package com.example.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.kafka.models.User;
import com.example.kafka.neo4j.Neo4jTypeFactory;
import com.example.kafka.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Consumer {
    @Autowired
    Neo4jTypeFactory neo4jTypeFactory;
    @KafkaListener(topics = "${spring.kafka.topic-4}", groupId = "${spring.kafka.group_id}", containerFactory = "userKafkaListenerContainerFactory",batch = "true")
    void listenerWithMessageConverter(ConsumerRecords<String, User> message) {
        log.info("Received Message : {}",message);
        message.forEach(record->writeNeo4j(record.value()));
    }

    private void writeNeo4j(final User value){
        String source = value.getName();
        neo4jTypeFactory.getWriterType(value.getType()).processMessageType(value.getName(), value);
        if (!value.getAction().equals(Constants.DELETE)){
            value.getRelationship().stream().forEach(relationship-> getaVoid(source, relationship));
        }
    }

    private void getaVoid(final String source, final User.Relationship relationship) {
        neo4jTypeFactory.getWriterType(relationship.getType()).processMessageType(source, relationship);
    }
}
