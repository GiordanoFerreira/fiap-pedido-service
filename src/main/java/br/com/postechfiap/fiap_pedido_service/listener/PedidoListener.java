package br.com.postechfiap.fiap_pedido_service.listener;

import br.com.postechfiap.fiap_pedido_service.domain.DadosPagamento;
import br.com.postechfiap.fiap_pedido_service.domain.ItemPedido;
import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.event.PedidoCreatedEvent;
import br.com.postechfiap.fiap_pedido_service.interfaces.usecases.ProcessarPedidoUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PedidoListener {

    private final ProcessarPedidoUseCase processarPedidoUseCase;

    @KafkaListener(
            topics = "pedido-topic",
            groupId = "pedido-service"
    )
    public void onPedidoCreated(PedidoCreatedEvent event) {
        // 1. Monta domínio Pedido
        Pedido pedido = new Pedido();
        pedido.setId(event.id());
        pedido.setClienteId(event.idCliente());
        pedido.setItens(
                event.produtos().stream()
                        .map(i -> new ItemPedido(i.sku(), i.quantidade()))
                        .collect(Collectors.toList())
        );
        // converte dataCriacao e numeroCartao em DadosPagamento
        DadosPagamento dp = new DadosPagamento(
                event.numeroCartao()
        );
        pedido.setDadosPagamento(dp);

        // 2. Executa toda a lógica de validação, estoque, pagamentos etc.
        processarPedidoUseCase.executar(pedido);
    }
}
