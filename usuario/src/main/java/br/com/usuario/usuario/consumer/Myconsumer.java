package br.com.usuario.usuario.consumer;

import org.springframework.stereotype.Component;

import io.awspring.cloud.sqs.annotation.SqsListener;

@Component
public class Myconsumer {

    @SqsListener("minha-fila")
    public void receiveMessage(String message) {
            System.out.println(message);
    }
}
