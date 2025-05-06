package br.com.postechfiap.fiap_pedido_service.usecase;

import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.domain.StatusPedido;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.EstoqueInsuficienteException;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.PagamentoRecusadoException;
import br.com.postechfiap.fiap_pedido_service.interfaces.repository.PedidoRepository;
import br.com.postechfiap.fiap_pedido_service.interfaces.usecases.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProcessarPedidoUseCaseImpl implements ProcessarPedidoUseCase {

    private final ValidarClienteUseCase validarClienteUseCase;
    private final PreencherInformacoesProdutosUseCase preencherInformacoesProdutosUseCase;
    private final CalcularValorTotalPedidoUseCase calcularValorTotalPedidoUseCase;
    private final BaixarEstoqueUseCase baixarEstoqueUseCase;
    private final SolicitarPagamentoUseCase solicitarPagamentoUseCase;
    private final PedidoRepository pedidoRepository;

    @Override
    public void executar(Pedido pedido) {

        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new IllegalArgumentException("Pedido sem itens não pode ser processado.");
        }

        // Valida cliente
        validarClienteUseCase.validar(pedido.getClienteId());

        // Preenche dados dos produtos (preço, etc.)
        preencherInformacoesProdutosUseCase.preencher(pedido);

        // Calcula valor total
        calcularValorTotalPedidoUseCase.calcular(pedido);

        // Baixa de estoque
        try {
            baixarEstoqueUseCase.baixar(pedido);
        } catch (EstoqueInsuficienteException e) {
            pedido.setStatusPedido(StatusPedido.FECHADO_SEM_ESTOQUE);
            pedidoRepository.save(pedido);
            return;
        }

        // Pagamento
        try {
            solicitarPagamentoUseCase.solicitar(pedido);
        } catch (PagamentoRecusadoException e) {
            pedido.setStatusPedido(StatusPedido.FECHADO_SEM_CREDITO);
            pedidoRepository.save(pedido);
            return;
        }

        // Se tudo ok, finaliza com sucesso
        pedido.setStatusPedido(StatusPedido.FECHADO_COM_SUCESSO);
        pedidoRepository.save(pedido);
    }
}

