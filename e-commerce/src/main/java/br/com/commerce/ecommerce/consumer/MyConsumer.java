package br.com.commerce.ecommerce.consumer;

import io.awspring.cloud.sqs.annotation.SqsListener;

public class MyConsumer {
    @SqsListener("minha-fila")
    public void receiveMessage(String message) {
            System.out.println(message);
    }
}
