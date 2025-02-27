package com.owasptopten.insecure.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptographicFailureController {

    @GetMapping("/echo")
    public String echo() {
        return "Cryptographic Failure";
    }

    

}
