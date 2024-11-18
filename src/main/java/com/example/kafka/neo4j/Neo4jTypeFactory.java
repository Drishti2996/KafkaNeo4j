package com.example.kafka.neo4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public final class Neo4jTypeFactory {
    private final Map<String, Neo4jType> neo4jTypeMap=new HashMap<>();
    private final List<Neo4jType> neo4jWriters;
    @PostConstruct
    void updateNeo4jMap(){ neo4jWriters.forEach(neoWriter-> neo4jTypeMap.put(neoWriter.messageType(), neoWriter));}
    public Neo4jType getWriterType(String neo4jType) {
        return neo4jTypeMap.get(neo4jType);
    }
}
