package br.com.digio.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = TestApiDigioApplication.BASE_APPLICATION_PACKAGE)
public class TestApiDigioApplication {

    static final String BASE_APPLICATION_PACKAGE = "br.com.digio";

    public static void main(String[] args) {
        SpringApplication.run(TestApiDigioApplication.class, args);
    }

}
