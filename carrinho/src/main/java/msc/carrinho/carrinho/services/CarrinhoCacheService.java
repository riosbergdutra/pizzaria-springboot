package msc.carrinho.carrinho.services;

import msc.carrinho.carrinho.dtos.CarrinhoDto;

public interface CarrinhoCacheService {
    void cacheCarrinho(String key, CarrinhoDto carrinhoDto);
    CarrinhoDto getCarrinhoFromCache (String carrinho_id);
    void removeCarrinhoFromCache (String carrinho_id);
    void updateCarrinhoInCache(String key, CarrinhoDto carrinhoDto);
}
