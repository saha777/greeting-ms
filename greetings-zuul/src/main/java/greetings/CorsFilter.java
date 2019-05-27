package greetings;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.event.HeartbeatEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Profile("cors")
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class CorsFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(CorsFilter.class);

    private final Map<String, List<ServiceInstance>> catalog = new ConcurrentHashMap<>();

    private final DiscoveryClient discoveryClient;

    public CorsFilter(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
        this.refreshCatalog();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest request = (HttpServletRequest) req;
        String originHeaderValue = originFor(request);
        boolean clientAllowed = isClientAllowed(originHeaderValue);

        if (clientAllowed) {
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, originHeaderValue);
        }

        chain.doFilter(request, response);
    }

    private boolean isClientAllowed(String originHeaderValue) {
        if (StringUtils.hasText(originHeaderValue)) {
            URI orginUri = URI.create(originHeaderValue);
            int port = orginUri.getPort();
            String match = orginUri.getHost() + ':' + (port <= 0 ? 80 : port);

            this.catalog.forEach((k, v) -> {
                String collect = v.stream()
                        .map(si -> si.getHost() + ':' + si.getPort() + '(' + si.getServiceId() + ')')
                        .collect(Collectors.joining());
            });

            boolean svcMatch = catalog.keySet()
                    .stream()
                    .anyMatch(serviceId -> catalog.get(serviceId)
                            .stream()
                            .map(si -> si.getHost() + ':' + si.getPort())
                            .anyMatch(hp -> hp.equalsIgnoreCase(match)));
            return svcMatch;
        }
        return false;
    }

    private String originFor(HttpServletRequest request) {
        return StringUtils.hasText(request.getHeader(HttpHeaders.ORIGIN)) ?
                request.getHeader(HttpHeaders.ORIGIN) :
                request.getHeader(HttpHeaders.REFERER);
    }

    @Override
    public void destroy() {
    }

    @EventListener(HeartbeatEvent.class)
    public void onHeartbeatEvent(HeartbeatEvent heartbeatEvent) {
        refreshCatalog();
    }

    private void refreshCatalog() {
        discoveryClient.getServices()
                .forEach(svc -> catalog.put(svc, discoveryClient.getInstances(svc)));
    }
}
