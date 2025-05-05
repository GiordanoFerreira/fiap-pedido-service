package br.com.postechfiap.fiap_pedido_service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class Produto {

    private String sku;
    private String nome;
    private BigDecimal preco;
}
