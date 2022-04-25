package pl.blazejherzog.mywallet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @GetMapping("/test")
    public int test() {
        return 1;
    }
}
