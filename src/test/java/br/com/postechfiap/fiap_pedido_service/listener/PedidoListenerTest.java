package br.com.postechfiap.fiap_pedido_service.listener;

import br.com.postechfiap.fiap_pedido_service.domain.DadosPagamento;
import br.com.postechfiap.fiap_pedido_service.domain.ItemPedido;
import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.interfaces.usecases.ProcessarPedidoUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class PedidoListenerTest {

    @Mock
    private ProcessarPedidoUseCase processarPedidoUseCase;

    // aqui usamos a implementação real, não mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PedidoListener listener;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        listener = new PedidoListener(processarPedidoUseCase, objectMapper);
    }

    @Test
    void onPedidoCreated_deveChamarUseCase_quandoJsonValido() throws Exception {
        // --- monta um JSON de evento conforme o listener espera ---
        UUID id = UUID.randomUUID();
        long idCliente = 123L;
        LocalDate dataValidade = LocalDate.of(2025,5,20);
        LocalDateTime dataCriacao = LocalDateTime.of(2025,5,20,14,30);

        // array de produtos
        var produtosArray = objectMapper.createArrayNode();
        var produtoNode = objectMapper.createObjectNode();
        produtoNode.put("sku", "SKU-XYZ");
        produtoNode.put("quantidade", 4);
        produtosArray.add(produtoNode);

        // nó raiz
        var root = objectMapper.createObjectNode();
        root.put("id", id.toString());
        root.put("id_cliente", idCliente);
        root.set("produtos", produtosArray);
        root.put("numero_cartao", "4111111111111111");
        root.put("codigo_seguranca_cartao", "999");
        root.put("nome_titular_cartao", "Teste FULANO");
        root.put("data_validade", dataValidade.format(DateTimeFormatter.ISO_LOCAL_DATE));
        root.put("data_criacao", dataCriacao.format(DateTimeFormatter.ISO_DATE_TIME));

        // transforme em “JSON de string” duas vezes
        String realJson = objectMapper.writeValueAsString(root);
        String messageAsJson = objectMapper.writeValueAsString(realJson);

        // --- chama o listener ---
        listener.onPedidoCreated(messageAsJson);

        // --- captura o Pedido enviado ao use case ---
        ArgumentCaptor<Pedido> captor = ArgumentCaptor.forClass(Pedido.class);
        verify(processarPedidoUseCase).executar(captor.capture());

        Pedido pedido = captor.getValue();
        assertEquals(idCliente, pedido.getClienteId());
        assertNotNull(pedido.getItens());
        assertEquals(1, pedido.getItens().size());
        ItemPedido item = pedido.getItens().get(0);
        assertEquals("SKU-XYZ", item.getSkuProduto());
        assertEquals(4, item.getQuantidade());
        assertSame(pedido, item.getPedido());

        DadosPagamento dp = pedido.getDadosPagamento();
        assertEquals("4111111111111111", dp.getNumeroCartao());
        assertEquals("999", dp.getCodigoSegurancaCartao());
        assertEquals("Teste FULANO", dp.getNomeTitularCartao());
        assertEquals(dataValidade, dp.getDataValidade());
    }

    @Test
    void onPedidoCreated_naoChamaUseCase_quandoJsonInvalido() throws Exception {
        // passa uma string que não é JSON válido
        String garbage = "!!! não é JSON !!!";

        // não deve lançar exceção pra fora
        assertDoesNotThrow(() -> listener.onPedidoCreated(garbage));

        // e não deve chamar o use case
        verifyNoInteractions(processarPedidoUseCase);
    }
}
