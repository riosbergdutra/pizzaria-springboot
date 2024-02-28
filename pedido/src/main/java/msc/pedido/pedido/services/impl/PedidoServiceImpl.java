package msc.pedido.pedido.services.impl;

import msc.pedido.pedido.dtos.PedidoDto;
import msc.pedido.pedido.enums.StatusPedido;
import msc.pedido.pedido.model.Pedidos;
import msc.pedido.pedido.repositories.PedidoRepository;
import msc.pedido.pedido.services.PedidoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.awspring.cloud.sqs.operations.SqsTemplate;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private SqsTemplate sqsTemplate;

    @Override
    public PedidoDto criarPedido(PedidoDto pedidoDto) {
        Pedidos pedido = new Pedidos();
        pedido.setUser_id(pedidoDto.getUser_id());
        pedido.setProduct_id(pedidoDto.getProduct_id());
        pedido.setQuantidade(pedidoDto.getQuantidade());
        pedido.setEndereco_entrega(pedidoDto.getEndereco_entrega());
        pedido.setNumero_telefone_entrega(pedidoDto.getNumero_telefone_entrega());
        pedido.setFrete(pedidoDto.getFrete());
        pedido.setTotal_pedido(pedidoDto.getTotal_pedido());
        pedido.setData_pedido(new Timestamp(System.currentTimeMillis()));
        pedido.setStatus_pedido(StatusPedido.pendente);

        Pedidos novoPedido = pedidoRepository.save(pedido);

        // Envio da mensagem para a fila SQS
        String queueUrl = "http://localhost:4566/000000000000/minha-fila";
        String messageBody = "Novo pedido criado: " + novoPedido.getPedido_id();
        sqsTemplate.send(queueUrl, messageBody);

        return new PedidoDto(novoPedido);
    }

    @Override
    public PedidoDto getPedidoById(long pedido_id) {
        Optional<Pedidos> optionalProduto = pedidoRepository.findById(pedido_id);
        if (optionalProduto.isPresent()) {
            // Envio da mensagem para a fila SQS
            String queueUrl = "http://localhost:4566/000000000000/minha-fila";
            String messageBody = "Produto encontrado pelo ID: " + pedido_id;
            sqsTemplate.send(queueUrl, messageBody);
        }
        return optionalProduto.map(PedidoDto::new).orElse(null);
    }
    

    @Override
    public boolean realizarCheckout(long pedido_id) {
        Pedidos pedido = pedidoRepository.findById(pedido_id).orElse(null);
        if (pedido != null) {
            pedido.setData_pedido(new Timestamp(System.currentTimeMillis()));
            pedido.setStatus_pedido(StatusPedido.concluido);

            // Envio da mensagem para a fila SQS
            String queueUrl = "http://localhost:4566/000000000000/minha-fila";
            String messageBody = "Pedido concluído: " + pedido_id;
            sqsTemplate.send(queueUrl, messageBody);

            pedidoRepository.save(pedido);

            return true;
        }
        return false;
    }
}
