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
    // @Autowired is an annotation used to automatically inject dependencies into a class.
    // It is part of Spring’s Dependency Injection (DI) mechanism,
    // which allows objects to be provided with their required dependencies
    // (other objects or services) automatically, rather than explicitly creating
    // them within the class.
    @Autowired
    Neo4jTypeFactory neo4jTypeFactory;

    // The @KafkaListener annotation is used to mark a method as a listener that will consume
    // messages from a Kafka topic. The method will be triggered whenever a message is received.

  //containerFactory = "userKafkaListenerContainerFactory"This defines the container factory to be used by the Kafka listener,
  // which sets up how the messages should be deserialized.

    //batch = "true": This indicates that the listener will process messages in batches,
    // meaning it will consume multiple records in a single call.
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
