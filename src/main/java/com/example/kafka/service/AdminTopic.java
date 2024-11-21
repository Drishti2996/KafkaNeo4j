package com.example.kafka.service;

import java.util.Collections;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.example.kafka.config.KafkaConfiguration;
import com.example.kafka.exception.TopicCreationFailedException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

// @EnableRetry:
//
//    This enables Spring's retry functionality in the application,
//    allowing the method createTopic to be retried on failure according to the defined retry policy.
@EnableRetry
public class AdminTopic {

    @Autowired
    KafkaConfiguration kafkaConfiguration;

    @Retryable(retryFor = { TopicCreationFailedException.class },
            maxAttemptsExpression = "${spring.kafka.availability.retry_attempts}",
            // backoff defines the backoff strategy between retries,
            // where the delay is dynamically defined using a value from the application's
            // configuration (spring.kafka.availability.retry_interval).
            backoff = @Backoff(delayExpression = "${spring.kafka.availability.retry_interval}"))
    public void createTopic(NewTopic userTopic) {
        try (var client = AdminClient.create(kafkaConfiguration.kafkaAdminProperties())) {
            var topics = client.listTopics().names().get();
            if(!topics.contains(userTopic.name())){
                var result = client.createTopics(Collections.singleton(userTopic));
                final var future = result.values().get(userTopic.name());
                future.get();
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new TopicCreationFailedException(e.getMessage(), e);
        }
    }

    @Recover
    public void createTopicFallBack(final TopicCreationFailedException exception){
        log.warn(exception.getMessage());
        throw new TopicCreationFailedException(exception.getMessage(), exception);
    }

}
