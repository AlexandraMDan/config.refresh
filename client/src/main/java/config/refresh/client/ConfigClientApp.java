package config.refresh.client;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ConfigClientApp {

    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApp.class, args);
    }
}

@RestController
class MessageRestController {

    private static final Logger LOG = LoggerFactory.getLogger(MessageRestController.class);
    
    @Autowired
    private RestTemplate restTemplate;

    @Value("${location: No location}")
    private String location;

    @RequestMapping("/location")
    String getMessage() {
        Map<String, String> vars = new HashMap<String, String>();

        vars.put("address", "Cluj Napoca, RO");
        vars.put("sensor", "false");

        location = restTemplate.getForObject("http://maps.googleapis.com/maps/api/geocode/xml?address={address}&sensor={sensor}", String.class, vars);
        LOG.info(location);
        return location;
    }
}