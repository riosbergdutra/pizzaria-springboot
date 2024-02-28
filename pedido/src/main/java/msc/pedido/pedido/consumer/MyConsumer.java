package msc.pedido.pedido.consumer;

import org.springframework.stereotype.Component;

import io.awspring.cloud.sqs.annotation.SqsListener;

@Component
public class MyConsumer {

    @SqsListener("minha-fila")
    public void receiveMessage(String message) {
            System.out.println(message);
    }
}
