package msc.carrinho.carrinho.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import msc.carrinho.carrinho.model.Carrinho;

public interface CarrinhoRepository  extends JpaRepository<Carrinho, Long> {
}