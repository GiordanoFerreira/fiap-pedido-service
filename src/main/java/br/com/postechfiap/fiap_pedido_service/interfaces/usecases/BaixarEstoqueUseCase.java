package br.com.postechfiap.fiap_pedido_service.interfaces.usecases;

import br.com.postechfiap.fiap_pedido_service.domain.Pedido;

public interface BaixarEstoqueUseCase {
    void baixar(Pedido pedido);
}
