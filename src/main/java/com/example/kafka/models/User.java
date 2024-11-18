package com.example.kafka.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private int age;
    private String type;
    private String action;
    List<Relationship> relationship;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Relationship{
        String relationshipType;
        String type;
        String target;
        String action;
    }


}
