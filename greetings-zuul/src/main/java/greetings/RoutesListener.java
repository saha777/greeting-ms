package greetings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RoutesListener {
    private final RouteLocator routeLocator;
    private final DiscoveryClient discoveryClient;

    private Logger log = LoggerFactory.getLogger(RoutesListener.class);

    @Autowired
    public RoutesListener(RouteLocator routeLocator, DiscoveryClient discoveryClient) {
        this.routeLocator = routeLocator;
        this.discoveryClient = discoveryClient;
    }

    @EventListener(HeartbeatEvent.class)
    public void onHeartbeatEvent(HeartbeatEvent heartbeatEvent){
        log.info("onHeartbeatEvent");
        discoveryClient.getServices()
                .stream()
                .map(x -> " " + x)
                .forEach(log::info);
    }

    @EventListener(RoutesRefreshedEvent.class)
    public void onRoutesRefreshEvent(RoutesRefreshedEvent routesRefreshedEvent){
        log.info("onRoutesRefreshEvent");
        routeLocator.getRoutes()
                .stream()
                .map(x -> " " + x)
                .forEach(log::info);
    }

}
