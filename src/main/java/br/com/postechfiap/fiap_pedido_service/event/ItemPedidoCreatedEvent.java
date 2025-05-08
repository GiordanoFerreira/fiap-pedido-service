package br.com.postechfiap.fiap_pedido_service.event;

public record ItemPedidoCreatedEvent(
        String sku,
        Integer quantidade
){}