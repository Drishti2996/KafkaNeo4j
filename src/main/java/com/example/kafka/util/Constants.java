package com.example.kafka.util;

//Indicates that the class cannot be subclassed (extended).
//This is often used for utility or helper classes that are not meant to be inherited.
public final class Constants {

    //static means the field belongs to the class rather than any instance of the class.
    //final ensures the field’s value cannot be changed after it is assigned.
    public static final String updateQueryString = "MATCH (n:%1$s) SET %2$s return n";
    public static final String createQueryString = "CREATE (n:%1$s {%2$s}) return n";
    public static final String deleteQueryString = "MATCH (n:%1$s) DETACH DELETE n";
    public static final String createQueryFormat = "%s: $%s";
    public static final String updateQueryFormat = "n.%s= $%s";
    public static final String RELATIONSHIP = "relationship";
    public static final String NODE = "node";
    public static final String AGE = "age";
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
}
