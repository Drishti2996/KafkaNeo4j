package com.example.kafka.neo4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.neo4j.driver.summary.ResultSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;

import com.example.kafka.models.User;
import com.example.kafka.util.Constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class Neo4jNodeType implements Neo4jType{
    @Autowired
    Neo4jClient neo4jClient;
    @Override
    public String messageType() {
        return Constants.NODE;
    }

    @Override
    public void processMessageType(final String label, final Object user) {
        User userDetails = (User) user;
        switch (userDetails.getAction()){
            case Constants.CREATE -> createObject(label, userDetails);
            case Constants.UPDATE -> updateObject(label, userDetails);
            case Constants.DELETE -> deleteObject(label);
        }
    }

    private void deleteObject(final String label) {
        String query = deleteQuery(label);
        ResultSummary run;
        try {
            run = neo4jClient.query(query)
                    .run();
            log.info("Delete Node query : {}", run.query().text());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private String deleteQuery(final String label) {
        String query = Constants.deleteQueryString;
        return String.format(query, label);
    }

    private void updateObject(final String label, final User userDetails) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(Constants.AGE,userDetails.getAge());
        String query = updateQuery(label,parameters.keySet());
        runNeoQuery(parameters, query);
    }

    private void runNeoQuery(final Map<String, Object> parameters, final String query) {
        ResultSummary run;
        try {
            run = neo4jClient.query(query)
                    .bindAll(parameters)
                    .run();
            log.debug("Neo4j query : {}", run.query().text());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    private String updateQuery(final String label, final Set<String> keySet) {
        String propsString = propertiesString(keySet,Constants.updateQueryFormat);
        String query = Constants.updateQueryString;
        return String.format(query, label, propsString);
    }

    private String propertiesString(final Set<String> keySet, final String queryFormat){
        return keySet.stream()
                .map(prop -> String.format(queryFormat, prop, prop))
                .collect(Collectors.joining(", "));
    }
    private void createObject(final String label, final User userDetails) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(Constants.AGE,userDetails.getAge());
        String query = createQuery(label,parameters.keySet());
        runNeoQuery(parameters,query);
    }

    private String createQuery(String labels, final Set<String> parameters){
        String propsString = propertiesString(parameters,Constants.createQueryFormat);
        String query = Constants.createQueryString;
        return String.format(query, labels, propsString);
    }

}
