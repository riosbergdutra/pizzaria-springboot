package br.com.commerce.ecommerce.services.impl;

import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.commerce.ecommerce.dtos.ProdutoDto;
import br.com.commerce.ecommerce.models.Produto;
import br.com.commerce.ecommerce.respositories.ProdutoRepository;
import br.com.commerce.ecommerce.services.ProdutoService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private SqsTemplate sqsTemplate;

    @Override
    public ProdutoDto createProdutoService(ProdutoDto produtoDto) {
        Produto produto = new Produto();
        produto.setNome(produtoDto.getNome());
        produto.setDescricao(produtoDto.getDescricao());
        produto.setPreco_unitario(produtoDto.getPreco_unitario());
        produto.setImagem(produtoDto.getImagem());
        produto.setCodigo_barra(produtoDto.getCodigo_barra());
        produto.setCategoria(produtoDto.getCategoria());

        Produto novoProduto = produtoRepository.save(produto);

        // Envio da mensagem para a fila SQS
        String queueUrl = "http://localhost:4566/000000000000/minha-fila";
        String messageBody = "Novo produto cadastrado: " + produtoDto.getNome();
        sqsTemplate.send(queueUrl, messageBody);

        return new ProdutoDto(novoProduto);
    }

    @Override
    public ProdutoDto findProdutoByIdService(Long product_id) {
        @SuppressWarnings("null")
        Optional<Produto> optionalProduto = produtoRepository.findById(product_id);
        if (optionalProduto.isPresent()) {
            // Envio da mensagem para a fila SQS
            String queueUrl = "http://localhost:4566/000000000000/minha-fila";
            String messageBody = "Produto encontrado pelo ID: " + product_id;
            sqsTemplate.send(queueUrl, messageBody);
        }
        return optionalProduto.map(ProdutoDto::new).orElse(null);
    }

    @Override
    public List<ProdutoDto> findAllProdutosService() {
        List<Produto> produtos = produtoRepository.findAll();
        // Envio da mensagem para a fila SQS
        String queueUrl = "http://localhost:4566/000000000000/minha-fila";
        String messageBody = "Consultando todos os produtos";
        sqsTemplate.send(queueUrl, messageBody);
        return produtos.stream().map(ProdutoDto::new).collect(Collectors.toList());
    }

    @Override
public ProdutoDto updateProdutoService(Long product_id, ProdutoDto produtoDto) {
    @SuppressWarnings("null")
    Optional<Produto> optionalProduto = produtoRepository.findById(product_id);
    if (optionalProduto.isPresent()) {
        Produto produto = optionalProduto.get();
        produto.setNome(produtoDto.getNome());
        produto.setDescricao(produtoDto.getDescricao());
        produto.setPreco_unitario(produtoDto.getPreco_unitario());
        produto.setImagem(produtoDto.getImagem());
        produto.setCodigo_barra(produtoDto.getCodigo_barra());
        produto.setCategoria(produtoDto.getCategoria());

        try {
            produto = produtoRepository.save(produto);

            // Envio da mensagem para a fila SQS
            String queueUrl = "http://localhost:4566/000000000000/minha-fila";
            String messageBody = "Produto atualizado pelo ID: " + product_id;
            sqsTemplate.send(queueUrl, messageBody);

            return new ProdutoDto(produto);
        } catch (Exception e) {
            // Tratamento de exceção em caso de erro ao salvar no banco de dados
            throw new RuntimeException("Erro ao atualizar o produto", e);
        }
    } else {
        return null;
    }
}   
@SuppressWarnings("null")
@Override
    public void removeProdutoService(Long product_id) {
        produtoRepository.deleteById(product_id);

        // Envio da mensagem para a fila SQS
        String queueUrl = "http://localhost:4566/000000000000/minha-fila";
        String messageBody = "Produto removido pelo ID: " + product_id;
        sqsTemplate.send(queueUrl, messageBody);
    }
}