package br.com.usuario.usuario.consumer;

import org.springframework.stereotype.Component;

import io.awspring.cloud.sqs.annotation.SqsListener;

@Component
public class Myconsumer {

    @SqsListener("minha-fila")
    public void receiveMessage(String message) {
        if (message != null && !message.isEmpty()) {
            System.out.println(message);
        } else {
            System.out.println("Mensagem vazia recebida.");
        }
    }
}
