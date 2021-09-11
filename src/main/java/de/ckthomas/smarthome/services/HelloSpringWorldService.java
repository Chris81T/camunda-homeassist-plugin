package de.ckthomas.smarthome.services;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class HelloSpringWorldService {

    @PostConstruct
    public void sayHello() {
        LoggerFactory.getLogger(HelloSpringWorldService.class).info("////////////////// ||||||||||||||||||||| H E L L O - I am A S P R I N G   S E R V I C E   B E A N :-)");
    }

}
