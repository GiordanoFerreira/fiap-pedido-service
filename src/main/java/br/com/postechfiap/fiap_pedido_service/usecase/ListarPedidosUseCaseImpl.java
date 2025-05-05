package br.com.postechfiap.fiap_pedido_service.usecase;

import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.interfaces.repository.PedidoRepository;
import br.com.postechfiap.fiap_pedido_service.interfaces.usecases.ListarPedidosUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListarPedidosUseCaseImpl implements ListarPedidosUseCase {

    private final PedidoRepository pedidoRepository;

    @Override
    public List<Pedido> executar() {
        return pedidoRepository.findAll();
    }
}