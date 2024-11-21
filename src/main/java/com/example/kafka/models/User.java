package com.example.kafka.models;

import java.util.List;
// Lombok annotations to reduce boilerplate code like getters,
// setters, constructors, and more
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data // Generates getters, setters, equals, hashCode, toString,
// and other utility methods for both the User and Relationship classes.

@Builder // Adds a builder pattern to construct User instances fluently.
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
