package greetings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.EnableZuulServer;

@SpringBootApplication
public class GreetingsZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreetingsZuulApplication.class, args);
    }

}
