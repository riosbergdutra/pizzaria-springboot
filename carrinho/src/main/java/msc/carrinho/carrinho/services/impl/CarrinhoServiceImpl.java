package msc.carrinho.carrinho.services.impl;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import msc.carrinho.carrinho.dtos.CarrinhoDto;
import msc.carrinho.carrinho.model.Carrinho;
import msc.carrinho.carrinho.repository.CarrinhoRepository;
import msc.carrinho.carrinho.services.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarrinhoServiceImpl implements CarrinhoService {

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private SqsTemplate sqsTemplate;

    @Override
    public CarrinhoDto createCarrinhoService(CarrinhoDto carrinhoDto) {
        Carrinho carrinho = new Carrinho();
        carrinho.setUser_Id(carrinhoDto.getUser_Id());
        carrinho.setProduct_Id(carrinhoDto.getProduct_Id());
        carrinho.setQuantidade(carrinhoDto.getQuantidade());
        carrinho.setPreco_total(carrinhoDto.getPreco_total());
        carrinho.setFrete(carrinhoDto.getFrete());

        Carrinho novoCarrinho = carrinhoRepository.save(carrinho);

        // Envio da mensagem para a fila SQS
        String queueUrl = "http://localhost:4566/000000000000/minha-fila";
        String messageBody = "Novo carrinho criado: " + novoCarrinho.getCarrinho_id();
        sqsTemplate.send(queueUrl, messageBody);

        return new CarrinhoDto(novoCarrinho);
    }

    @Override
    public CarrinhoDto findCarrinhoByIdService(Long carrinhoId) {
        Optional<Carrinho> optionalCarrinho = carrinhoRepository.findById(carrinhoId);
        if (optionalCarrinho.isPresent()) {
            // Envio da mensagem para a fila SQS
            String queueUrl = "http://localhost:4566/000000000000/minha-fila";
            String messageBody = "Carrinho encontrado pelo ID: " + carrinhoId;
            sqsTemplate.send(queueUrl, messageBody);
        }
        return optionalCarrinho.map(CarrinhoDto::new).orElse(null);
    }

    @Override
    public List<CarrinhoDto> findAllCarrinhoService() {
        List<Carrinho> carrinhos = carrinhoRepository.findAll();
        // Envio da mensagem para a fila SQS
        String queueUrl = "http://localhost:4566/000000000000/minha-fila";
        String messageBody = "Consultando todos os carrinhos";
        sqsTemplate.send(queueUrl, messageBody);
        return carrinhos.stream().map(CarrinhoDto::new).collect(Collectors.toList());
    }

    @Override
    public CarrinhoDto updateCarrinhoService(Long carrinhoId, CarrinhoDto carrinhoDto) {
        Optional<Carrinho> optionalCarrinho = carrinhoRepository.findById(carrinhoId);
        if (optionalCarrinho.isPresent()) {
            Carrinho carrinho = optionalCarrinho.get();
            carrinho.setUser_Id(carrinhoDto.getUser_Id());
            carrinho.setProduct_Id(carrinhoDto.getProduct_Id());
            carrinho.setQuantidade(carrinhoDto.getQuantidade());
            carrinho.setPreco_total(carrinhoDto.getPreco_total());
            carrinho.setFrete(carrinhoDto.getFrete());

            try {
                carrinho = carrinhoRepository.save(carrinho);

                // Envio da mensagem para a fila SQS
                String queueUrl = "http://localhost:4566/000000000000/minha-fila";
                String messageBody = "Carrinho atualizado pelo ID: " + carrinhoId;
                sqsTemplate.send(queueUrl, messageBody);

                return new CarrinhoDto(carrinho);
            } catch (Exception e) {
                // Tratamento de exceção em caso de erro ao salvar no banco de dados
                throw new RuntimeException("Erro ao atualizar o carrinho", e);
            }
        } else {
            return null;
        }
    }

    @Override
    public void removeCarrinhoService(Long carrinhoId) {
        carrinhoRepository.deleteById(carrinhoId);

        // Envio da mensagem para a fila SQS
        String queueUrl = "http://localhost:4566/000000000000/minha-fila";
        String messageBody = "Carrinho removido pelo ID: " + carrinhoId;
        sqsTemplate.send(queueUrl, messageBody);
    }
}
