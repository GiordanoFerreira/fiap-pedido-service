package br.com.postechfiap.fiap_pedido_service.adapters.clients.dto;

import java.util.List;

public record ListaProdutoResponseDTO(
        List<ProdutoResponseDTO> produtos
) {
}
