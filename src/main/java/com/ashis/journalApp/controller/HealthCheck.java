package com.ashis.journalApp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheck {
                    //end-point
        @GetMapping("/health-check") //this is GET contoller so with GET call control will come to this function
        public String healthCheck()
        {
            return "OK";
        }
}
