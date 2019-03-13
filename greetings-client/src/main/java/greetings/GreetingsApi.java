package greetings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Profile("feign")
@RestController
@RequestMapping("/api")
public class GreetingsApi {
    @Autowired
    private GreetingsClient greetingsClient;

    @GetMapping("/feign/{name}")
    public Map<String, String> hi(@PathVariable String name) {
        return greetingsClient.greet(name);
    }
}
