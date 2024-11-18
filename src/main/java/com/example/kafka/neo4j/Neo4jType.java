package com.example.kafka.neo4j;

import com.example.kafka.models.User;

public interface Neo4jType {

    String messageType();
    void processMessageType(String label, Object user);
}
