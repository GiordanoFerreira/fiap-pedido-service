package br.com.postechfiap.fiap_pedido_service.adapters.clients.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class SolicitarPagamentoRequest {

    private String numeroCartao;
    private Double valor;
    private UUID pedidoId;
}
