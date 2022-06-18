package pl.blazejherzog.mywallet;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.Entity;
import javax.persistence.Table;

@SpringBootApplication
public class MyWalletApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	public static void main(String[] args) {
		SpringApplication.run(MyWalletApplication.class, args);
	}
}
