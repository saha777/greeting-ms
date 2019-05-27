package greetings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableZuulProxy
public class ZuulConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(RouteLocator routeLocator) {
        Logger log = LoggerFactory.getLogger(getClass());
        return args -> routeLocator.getRoutes()
                .forEach(r -> log.info("{} ({}) {}",
                        r.getId(), r.getLocation(), r.getFullPath()));
    }

}
