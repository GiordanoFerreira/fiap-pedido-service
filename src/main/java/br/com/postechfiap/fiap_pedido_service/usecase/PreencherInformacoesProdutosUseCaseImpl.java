package br.com.postechfiap.fiap_pedido_service.usecase;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.ProdutoClient;
import br.com.postechfiap.fiap_pedido_service.domain.ItemPedido;
import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.domain.Produto;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.ProdutoNaoEncontradoException;
import br.com.postechfiap.fiap_pedido_service.interfaces.usecases.PreencherInformacoesProdutosUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreencherInformacoesProdutosUseCaseImpl implements PreencherInformacoesProdutosUseCase {

    private final ProdutoClient produtoClient;

    @Override
    public void preencher(Pedido pedido) {
        for (ItemPedido item : pedido.getItens()) {
            try {
                Produto produto = produtoClient.buscarProduto(item.getSkuProduto());
                item.setPrecoUnitario(produto.getPreco());
                item.setPedido(pedido);
            } catch (Exception e) {
                throw new ProdutoNaoEncontradoException("Produto com SKU " + item.getSkuProduto() + " não encontrado.");
            }
        }
    }
}
