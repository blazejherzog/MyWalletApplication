package pl.blazejherzog.mywallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.Entity;
import javax.persistence.Table;

@SpringBootApplication
public class MyWalletApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyWalletApplication.class, args);

	}
}
