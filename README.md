Spring boot project - kafka(producer and consumer), neo4j (neo4jclient), creation of topic using topic builder, retryable logic, factory design pattern and restful api.

In order to run this project, you need kafka and neo4j setup.

This project runs on 9000 port.

Steps after running the project:-
1. Open postman and run - http://localhost:9000/produce/message with request body as
     {
  "name": "try",
  "age": 26,
  "type": "node",
  "action": "create",
  "relationship": [
  ]
}
In order to create a node in Neo4j without relationship.


2. run - http://localhost:9000/produce/message with request body as
    {
  "name": "demo",
  "age": 26,
  "type": "node",
  "action": "create",
  "relationship": [{
      "relationshipType":"friend",
      "type":"relationship",
      "target":"try",
      "action":"create"
  }
  ]
}

In order to create a node and a relationship.

For node, we can change the action to create, update and delete.

For Relationship, we can change action to create and delete.
In order to create a node in Neo4j without relationship.
