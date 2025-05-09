package br.com.postechfiap.fiap_pedido_service.event;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDate;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DadosPagamentoEvent(
        String numeroCartao,
        String codigoSegurancaCartao,
        String nomeTitularCartao,
        LocalDate dataValidade
) {}