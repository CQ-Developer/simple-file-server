package com.chen.sfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.chen.sfs.config.properties")
public class App {

	public static void main(String[] args) {
                SpringApplication.run(App.class, args);
        }

}
