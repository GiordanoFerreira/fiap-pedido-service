package br.com.postechfiap.fiap_pedido_service.usecase;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.PagamentoClient;
import br.com.postechfiap.fiap_pedido_service.adapters.clients.request.PerfilPagamentoRequestDTO;
import br.com.postechfiap.fiap_pedido_service.adapters.clients.request.SolicitarPagamentoRequestDTO;
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
            var item = pedido.getItens().stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Pedido sem itens"));

            var perfil = PerfilPagamentoRequestDTO.builder()
                    .numeroCartao(pedido.getDadosPagamento().getNumeroCartao())
                    .codigoSegurancaCartao(pedido.getDadosPagamento().getCodigoSegurancaCartao())
                    .nomeTitularCartao(pedido.getDadosPagamento().getNomeTitularCartao())
                    .dataValidade(pedido.getDadosPagamento().getDataValidade())
                    .build();

            var request = SolicitarPagamentoRequestDTO.builder()
                    .valor(pedido.getValorTotal())
                    .idCliente(pedido.getClienteId())
                    .idPedido(pedido.getId())
                    .skuProduto(item.getSkuProduto())
                    .perfilPagamento(perfil)
                    .build();

            pagamentoClient.criarPagamento(request);

        } catch (Exception e) {
            throw new PagamentoRecusadoException("Pagamento recusado para o pedido: " + pedido.getId());
        }
    }
}
