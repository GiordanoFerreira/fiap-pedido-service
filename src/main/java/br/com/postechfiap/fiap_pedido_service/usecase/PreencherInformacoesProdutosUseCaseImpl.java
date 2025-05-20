package br.com.postechfiap.fiap_pedido_service.usecase;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.ProdutoClient;
import br.com.postechfiap.fiap_pedido_service.adapters.clients.dto.ListaProdutoResponseDTO;
import br.com.postechfiap.fiap_pedido_service.adapters.clients.dto.ProdutoResponseDTO;
import br.com.postechfiap.fiap_pedido_service.domain.ItemPedido;
import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.ProdutoNaoEncontradoException;
import br.com.postechfiap.fiap_pedido_service.interfaces.usecases.PreencherInformacoesProdutosUseCase;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PreencherInformacoesProdutosUseCaseImpl implements PreencherInformacoesProdutosUseCase {

    private final ProdutoClient produtoClient;

    @Override
    public void preencher(Pedido pedido) {
        for (ItemPedido item : pedido.getItens()) {
            try {
                ListaProdutoResponseDTO resp = produtoClient.buscarProduto(item.getSkuProduto());
                Optional<ProdutoResponseDTO> encontrado =
                        resp.produtos().stream()
                                .filter(p -> p.getSku().equals(item.getSkuProduto()))
                                .findFirst();

                if (encontrado.isEmpty()) {
                    throw new ProdutoNaoEncontradoException("Produto com SKU " + item.getSkuProduto() + " não encontrado.");
                }

                ProdutoResponseDTO prod = encontrado.get();
                item.setSkuProduto(prod.getSku());
                item.setPrecoUnitario(prod.getPreco());
                item.setPedido(pedido);
            } catch (FeignException e) {
                throw new ProdutoNaoEncontradoException("Produto com SKU " + item.getSkuProduto() + " não encontrado.");
            }
        }
    }
}