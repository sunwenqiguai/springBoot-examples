package com.example.demo.hello;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class HelloSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send() {
        String context = "Hello," + new Date();
        System.out.println("Sender:" + context);
        rabbitTemplate.convertAndSend("hello", context);

    }

}
