package br.com.postechfiap.fiap_pedido_service.event;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PedidoCreatedEvent(
        UUID id,
        Long idCliente,
        List<ItemPedidoCreatedEvent> produtos,
        String numeroCartao,
        LocalDateTime dataCriacao
){}
