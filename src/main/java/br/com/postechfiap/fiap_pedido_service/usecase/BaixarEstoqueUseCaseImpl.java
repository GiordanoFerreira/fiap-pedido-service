package br.com.postechfiap.fiap_pedido_service.usecase;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.EstoqueClient;
import br.com.postechfiap.fiap_pedido_service.adapters.clients.request.BaixaEstoqueRequest;
import br.com.postechfiap.fiap_pedido_service.domain.ItemPedido;
import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.EstoqueInsuficienteException;
import br.com.postechfiap.fiap_pedido_service.interfaces.usecases.BaixarEstoqueUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BaixarEstoqueUseCaseImpl implements BaixarEstoqueUseCase {

    private final EstoqueClient estoqueClient;

    @Override
    public void baixar(Pedido pedido) {
        for (ItemPedido item : pedido.getItens()) {
            try {
                BaixaEstoqueRequest request = new BaixaEstoqueRequest(
                        item.getSkuProduto(),
                        item.getQuantidade().longValue()
                );
                estoqueClient.baixarEstoque(item.getSkuProduto(), request);
            } catch (Exception e) {
                throw new EstoqueInsuficienteException("Estoque insuficiente para o produto: " + item.getSkuProduto());
            }
        }
    }
}
