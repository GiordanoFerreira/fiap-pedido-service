package br.com.postechfiap.fiap_pedido_service.usecase;

import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.interfaces.usecases.CalcularValorTotalPedidoUseCase;
import org.springframework.stereotype.Service;

@Service
public class CalcularValorTotalPedidoUseCaseImpl implements CalcularValorTotalPedidoUseCase {

    @Override
    public void calcular(Pedido pedido) {
        pedido.calcularEAtualizarValorTotal();
    }
}
