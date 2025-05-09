package br.com.postechfiap.fiap_pedido_service.listener;

import br.com.postechfiap.fiap_pedido_service.domain.DadosPagamento;
import br.com.postechfiap.fiap_pedido_service.domain.ItemPedido;
import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.event.ItemPedidoCreatedEvent;
import br.com.postechfiap.fiap_pedido_service.event.PedidoCreatedEvent;
import br.com.postechfiap.fiap_pedido_service.interfaces.usecases.ProcessarPedidoUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@KafkaListener(
        topics = "pedido-topic",
        groupId = "pedido-service",
        id = "string-listener-test"
)
public class PedidoListener {

    private final ProcessarPedidoUseCase processarPedidoUseCase;
    private final ObjectMapper objectMapper;

    @KafkaHandler
    public void onPedidoCreated(@Payload String messageAsJson) throws JsonProcessingException {
        try{
        System.out.println("📥 Mensagem JSON bruta recebida do Kafka: " + messageAsJson);
        String realJson = objectMapper.readValue(messageAsJson, String.class);
        JsonNode rootNode = objectMapper.readTree(realJson);
        System.out.println("JSON NODE   " + rootNode);

        List<ItemPedidoCreatedEvent> produtos = new ArrayList<>();
        JsonNode produtosNode = rootNode.get("produtos");


        if (produtosNode.isArray()) {
            for (JsonNode produtoNode : produtosNode) {
                ItemPedidoCreatedEvent item = objectMapper.treeToValue(produtoNode, ItemPedidoCreatedEvent.class);
                produtos.add(item);
            }
        }

        UUID id = UUID.fromString(rootNode.get("id").asText());
        Long idCliente = rootNode.get("id_cliente").asLong();
        String numeroCartao = rootNode.get("numero_cartao").asText();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime dataCriacao = LocalDateTime.parse(rootNode.get("data_criacao").asText(), formatter);

        PedidoCreatedEvent event = new PedidoCreatedEvent(id, idCliente, produtos, numeroCartao, dataCriacao);
        System.out.println("✅ Desserialização (via JsonNode) para PedidoCreatedEvent: " + event);
        System.out.print("Lista de produtos --->" + event.produtos().toString());

        // 1. Monta domínio Pedido
        Pedido pedido = new Pedido();
        pedido.setId(event.id());
        pedido.setClienteId(event.idCliente());
        pedido.setItens(
          event.produtos().stream().map(itemCreated -> {
              ItemPedido itemPedido = new ItemPedido();
              itemPedido.setSkuProduto(itemCreated.sku());
              itemPedido.setQuantidade(itemCreated.quantidade());
              return itemPedido;}).collect(Collectors.toList()));

        DadosPagamento dp = new DadosPagamento(
                event.numeroCartao()
            );
        pedido.setDadosPagamento(dp);

        // Adicionando o System.out para debug
        System.out.println("Pedido criado: ");
        System.out.println("ID: " + pedido.getId());
        System.out.println("Cliente ID: " + pedido.getClienteId());
        System.out.println("Itens: " + pedido.getItens());
        System.out.println("Dados de Pagamento: " + pedido.getDadosPagamento().getNumeroCartao());

        // 2. Executa toda a lógica de validação, estoque, pagamentos etc.
        processarPedidoUseCase.executar(pedido);

        } catch (Exception e) {
            System.err.println("❌ Erro ao desserializar: " + e.getMessage());
            e.printStackTrace(); // Imprima o stack trace completo para entender o erro
        }
    }
}
