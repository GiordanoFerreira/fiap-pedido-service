package br.com.postechfiap.fiap_pedido_service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Cliente {

    private Long id;
    private String nome;
    private String cpf;
}
