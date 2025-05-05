package br.com.postechfiap.fiap_pedido_service.adapters.clients.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaixaEstoqueRequest {

    private String skuProduto;
    private int quantidade;
}
