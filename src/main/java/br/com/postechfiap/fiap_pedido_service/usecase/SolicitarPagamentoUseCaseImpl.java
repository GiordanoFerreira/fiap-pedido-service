package br.com.postechfiap.fiap_pedido_service.usecase;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.PagamentoClient;
import br.com.postechfiap.fiap_pedido_service.adapters.clients.request.SolicitarPagamentoRequest;
import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.PagamentoRecusadoException;
import br.com.postechfiap.fiap_pedido_service.interfaces.usecases.SolicitarPagamentoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SolicitarPagamentoUseCaseImpl implements SolicitarPagamentoUseCase {

    private final PagamentoClient pagamentoClient;

    @Override
    public void solicitar(Pedido pedido) {
        try {
            SolicitarPagamentoRequest request = new SolicitarPagamentoRequest(
                    pedido.getDadosPagamento().getNumeroCartao(),
                    pedido.getValorTotal().doubleValue(),
                    pedido.getId()
            );
            pagamentoClient.criarPagamento(request);
        } catch (Exception e) {
            throw new PagamentoRecusadoException("Pagamento recusado para o pedido: " + pedido.getId());
        }
    }
}
