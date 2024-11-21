package com.example.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.kafka.models.User;

/** Kafka Concepts

 Producer: A component that sends messages to Kafka topics.
 Consumer: A component that reads messages from Kafka topics.
 Topic: A channel where messages are sent by producers and consumed by consumers.
 Partition: Kafka topics are divided into partitions for scalability and parallel processing.
 Broker: A Kafka server that stores data and serves client requests.
 Serialization: Converts data to bytes for transport over Kafka (e.g., StringSerializer, JsonSerializer).
 Deserialization: Converts data back to objects after transport (e.g., StringDeserializer, JsonDeserializer). **/


@Configuration // Marks the class as a Spring configuration class where beans are defined.
public class KafkaConfiguration {

    // @ValueInjects properties from the application’s configuration file (application.yml or application.properties).

   /**
    * bootstrapServers: Kafka broker's address.
    * batch: Batch size for producers.
    * linger: Delay before sending a batch of messages.
    * topic4: Name of the topic.
    * group: Consumer group ID.
    * **/
    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.producer.batch_size}")
    private String batch;
    @Value("${spring.kafka.producer.linger}")
    private String linger;
    @Value("${spring.kafka.topic-4}")
    private String topic4;
    @Value("${spring.kafka.group_id}")
    private String group;

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    public Map<String, Object> kafkaAdminProperties() {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return properties;
    }
// AUTO_OFFSET_RESET_CONFIG: Sets the offset behavior
// (earliest means start reading from the beginning if no previous offset exists).
    public ConsumerFactory<String, User> userConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
                                                 new JsonDeserializer<>(User.class));
    }

    //userKafkaListenerContainerFactory:
    //    Creates a container factory for Kafka listeners, enabling them to process messages asynchronously
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, User> userKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, User> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userConsumerFactory());
        return factory;
    }

    @Bean
    ProducerFactory<String, User> userProducerFactory(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batch);
        props.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        return new DefaultKafkaProducerFactory<>(props);
    }


    // userKafkaTemplate:
    //
    //    Provides a Kafka template for sending messages.
    //    It uses the ProducerFactory configuration.
    @Bean
    public KafkaTemplate<String, User> userKafkaTemplate(){
        return new KafkaTemplate<>(userProducerFactory());
    }


    // topicUser:
    //    Creates a new Kafka topic programmatically using the TopicBuilder.
    //        Topic Name: From the topic4 property.
    //        Partitions: Number of partitions for the topic.
    //        Replicas: Number of replicas for fault tolerance.
    public NewTopic topicUser(){
        return TopicBuilder.name(topic4).partitions(3).replicas(1)
                .build();
    }
}
