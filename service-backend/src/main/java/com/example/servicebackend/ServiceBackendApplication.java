package com.example.servicebackend;

import com.example.servicebackend.service.CSVReaderTest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@SpringBootApplication
public class ServiceBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceBackendApplication.class, args);

    }




}
