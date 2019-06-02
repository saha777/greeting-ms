package greetings.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {

    private final LoadBalancerClient loadBalancerClient;

    @Bean
    ClientDetailsService clientDetailsService(ClientRepository clientRepository){
        return clientId -> clientRepository.findByClientId(clientId)
                .map(client -> {
                    BaseClientDetails details = new BaseClientDetails(
                            client.getClientId(),
                            null,
                            client.getScopes(),
                            client.getAuthorizedGrantTypes(),
                            client.getAuthorities());

                    details.setClientSecret(client.getSecret());

//                    details.setAutoApproveScopes(
//                            Arrays.asList(client.getAutoApproveScopes().split(",")));

//                    String greetingClientRedirectUri =
//                            Optional.ofNullable(this.loadBalancerClient.choose("greetings-client"))
//                                    .map(si -> String.format("http://%s:%s/",si.getHost(), si.getPort()))
//                                    .orElseThrow(() -> new ClientRegistrationException("could not find and bind greetings-client IP"));


//                    details.setRegisteredRedirectUri(Collections.singleton(greetingClientRedirectUri));
                    return details;
                })
                .orElseThrow(() -> new ClientRegistrationException(String.format("no client %s registered", clientId)));
    }

}
