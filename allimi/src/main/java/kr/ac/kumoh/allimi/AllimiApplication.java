package kr.ac.kumoh.allimi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server Url")}) //https사용 시 swagger사용하기 위한 cors해결법
@SpringBootApplication
public class AllimiApplication {
	public static void main(String[] args) {
		SpringApplication.run(AllimiApplication.class, args);
	}
}