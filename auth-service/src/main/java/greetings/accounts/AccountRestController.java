package greetings.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AccountRestController {

    public final AccountRepository accountRepository;

    @PostMapping("/registration")
    public Account register(@RequestBody Account account) {
        return accountRepository.save(account);
    }

}
