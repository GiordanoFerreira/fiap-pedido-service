package br.com.postechfiap.fiap_pedido_service.event;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ItemPedidoCreatedEvent(
        String sku,
        Integer quantidade
){}