package com.example.kafka.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.kafka.models.User;
import com.example.kafka.service.Producer;

@RestController
@RequestMapping("/produce")
public class kafkaProducerController {

    @Autowired
    Producer producer;

    /**
     * This is a prototype, therefore instead of returning responseEntity, I am just returning a string.
     */
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public String postMessages(@RequestBody User user){
        producer.produceMessage(user);
        return "done";
    }
}
