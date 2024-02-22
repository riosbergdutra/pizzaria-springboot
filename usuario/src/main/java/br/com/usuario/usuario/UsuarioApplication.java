package br.com.usuario.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.awspring.cloud.sqs.operations.SqsTemplate;


@SpringBootApplication
public class UsuarioApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(UsuarioApplication.class, args);
	}
	@Autowired
	private SqsTemplate sqsTemplate;
	@Override
	public void run(String... args) throws Exception {
		String queueUrl = "http://localhost:4566/000000000000/minha-fila";
        String messageBody = "mensagem";
        sqsTemplate.send(queueUrl,messageBody);
	}
}
