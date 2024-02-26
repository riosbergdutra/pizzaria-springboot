package msc.carrinho.carrinho.services;

import java.util.List;

import msc.carrinho.carrinho.dtos.CarrinhoDto;

public interface CarrinhoService {
    CarrinhoDto createCarrinhoService(CarrinhoDto carrinhoDto);
    CarrinhoDto findCarrinhoByIdService(Long carrinho_id);
    List<CarrinhoDto> findAllCarrinhoService();
    CarrinhoDto updateCarrinhoService(Long carrinho_id, CarrinhoDto carrinhoDto);
    void removeCarrinhoService(Long carrinho_id);
}
