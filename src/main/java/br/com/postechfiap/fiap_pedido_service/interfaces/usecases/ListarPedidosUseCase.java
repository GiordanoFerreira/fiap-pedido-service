package br.com.postechfiap.fiap_pedido_service.interfaces.usecases;

import br.com.postechfiap.fiap_pedido_service.domain.Pedido;

import java.util.List;

public interface ListarPedidosUseCase {
    List<Pedido> executar();
}
