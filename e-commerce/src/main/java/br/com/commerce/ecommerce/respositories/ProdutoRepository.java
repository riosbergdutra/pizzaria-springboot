package br.com.commerce.ecommerce.respositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.commerce.ecommerce.models.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}