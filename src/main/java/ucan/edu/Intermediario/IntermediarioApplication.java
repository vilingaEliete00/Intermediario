package ucan.edu.Intermediario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
public class IntermediarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(IntermediarioApplication.class, args);
	}

}
