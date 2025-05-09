package br.com.postechfiap.fiap_pedido_service.usecase;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.EstoqueClient;
import br.com.postechfiap.fiap_pedido_service.adapters.clients.request.BaixaEstoqueRequest;
import br.com.postechfiap.fiap_pedido_service.domain.ItemPedido;
import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.interfaces.usecases.ReporEstoqueUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReporEstoqueUseCaseImpl implements ReporEstoqueUseCase {

    private final EstoqueClient estoqueClient;

    @Override
    public void repor(Pedido pedido) {
        for (ItemPedido item : pedido.getItens()) {
            try {
                BaixaEstoqueRequest request = new BaixaEstoqueRequest(
                        item.getSkuProduto(),
                        item.getQuantidade().longValue()
                );
                estoqueClient.reporEstoque(item.getSkuProduto(), request);
            } catch (Exception e) {
                // Loga o erro, mas não impede o fechamento do pedido
                System.err.println("Erro ao repor estoque para SKU " + item.getSkuProduto() + ": " + e.getMessage());
            }
        }
    }
}

