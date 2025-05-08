package br.com.postechfiap.fiap_pedido_service.event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoCreatedEvent(
        UUID id,
        Long idCliente,
        List<ItemPedidoCreatedEvent> produtos,
        String numeroCartao,
        LocalDateTime dataCriacao
){}
