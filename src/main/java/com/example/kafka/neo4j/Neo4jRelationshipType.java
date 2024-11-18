package com.example.kafka.neo4j;

import org.neo4j.driver.summary.ResultSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import com.example.kafka.models.User;
import com.example.kafka.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Neo4jRelationshipType implements Neo4jType{
    @Autowired
    Neo4jClient neo4jClient;
    @Override
    public String messageType() {
        return Constants.RELATIONSHIP;
    }

    @Override
    public void processMessageType(final String label, final Object user) {
        User.Relationship userDetails = (User.Relationship) user;
        switch (userDetails.getAction()){
            case Constants.CREATE -> createRelationshipObject(label, userDetails);
            case Constants.DELETE -> deleteRelationshipObject(label,userDetails);
        }
    }

    private void deleteRelationshipObject(final String label, final User.Relationship userDetails) {
        String query = deleteRelationship(label,userDetails);
        neo4jQuery(query);
    }

    private void neo4jQuery(final String query) {
        ResultSummary run = null;
        try {
            run = neo4jClient.query(query)
                    .run();
            log.debug("Relationship query : {}", run.query().text());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void createRelationshipObject(final String label, final User.Relationship userDetails) {
        String query = createRelationship(label,userDetails);
        neo4jQuery(query);
    }

    private String createRelationship(final String label, final User.Relationship userDetails){
        String direction = String.format("-[r:%1$s]->", userDetails.getRelationshipType());
        String query = """
                        MATCH (source:%1$s), (target:%2$s)
                        MERGE (source) %3$s (target)
                        return *
                """;
        return String.format(query, label, userDetails.getTarget(), direction);
    }

    private static String deleteRelationship(final String label, final User.Relationship userDetails){
        String query = """
                MATCH(source:%1$s)-[r:%2$s]-(target:%3$s)
                DELETE r
                """;
        return String.format(query, label, userDetails.getRelationshipType(), userDetails.getTarget());
    }
}
