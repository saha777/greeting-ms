package greetings;

import greetings.accounts.Account;
import greetings.accounts.AccountRepository;
import greetings.clients.Client;
import greetings.clients.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
class DataCommandLineRunner implements CommandLineRunner {

    private final AccountRepository accountRepository;

    private final ClientRepository clientRepository;


    @Override
    public void run(String... args) throws Exception {

        Stream
                .of("dsyer,cloud", "pwebb,boot", "mminella,batch", "rwinch,security", "jlong,spring")
                .map(s -> s.split(","))
                .forEach(
                        tuple -> accountRepository.save(new Account(tuple[0], tuple[1], true)));

        Stream.of("html5,password", "android,secret").map(x -> x.split(","))
                .forEach(x -> clientRepository.save(new Client(x[0], x[1])));
    }
}
