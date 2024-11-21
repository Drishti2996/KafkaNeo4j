package com.example.kafka.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.kafka.models.User;
import com.example.kafka.service.Producer;

// @RestController - Indicates that this class is a controller that handles HTTP requests and returns
// responses in JSON or other formats directly (rather than resolving a view).
@RestController
@RequestMapping("/produce")
// @RequestMapping("/produce"):
//Sets the base path for all HTTP endpoints defined in this controller.
// All endpoints will start with /produce
public class kafkaProducerController {

    //@autowired - Injects an instance of the Producer class,
    // enabling access to its produceMessage method.
    @Autowired
    Producer producer;

    /**
     * This is a prototype, therefore instead of returning responseEntity, I am just returning a string.
     */

    // @RequestBodyIndicates that the method parameter (User user) should be bound
    // to the body of the incoming HTTP request (typically in JSON format).
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public String postMessages(@RequestBody User user){
        producer.produceMessage(user);
        return "done";
    }
}
