package br.com.postechfiap.fiap_pedido_service.usecases;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.ProdutoClient;
import br.com.postechfiap.fiap_pedido_service.adapters.clients.dto.ListaProdutoResponseDTO;
import br.com.postechfiap.fiap_pedido_service.adapters.clients.dto.ProdutoResponseDTO;
import br.com.postechfiap.fiap_pedido_service.domain.ItemPedido;
import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.ProdutoNaoEncontradoException;
import br.com.postechfiap.fiap_pedido_service.usecase.PreencherInformacoesProdutosUseCaseImpl;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PreencherInformacoesProdutosUseCaseImplTest {

    @Mock
    private ProdutoClient produtoClient;

    @InjectMocks
    private PreencherInformacoesProdutosUseCaseImpl useCase;

    private Pedido pedido;
    private ItemPedido item;

    @BeforeEach
    void setUp() {
        item = ItemPedido.builder()
                .skuProduto("SKU-123")
                .quantidade(2)
                .precoUnitario(null)
                .build();
        pedido = Pedido.builder()
                .id(UUID.randomUUID())
                .clienteId(1L)
                .itens(List.of(item))
                .build();
    }

    @Test
    void devePreencherInformacoesQuandoProdutoEncontrado() {
        // Arrange
        ProdutoResponseDTO dto = ProdutoResponseDTO.builder()
                .sku("SKU-123")
                .preco(new BigDecimal("15.50"))
                .build();
        ListaProdutoResponseDTO resp = new ListaProdutoResponseDTO(List.of(dto));
        when(produtoClient.buscarProduto("SKU-123")).thenReturn(resp);

        // Act
        useCase.preencher(pedido);

        // Assert
        assertEquals("SKU-123", item.getSkuProduto());
        assertEquals(new BigDecimal("15.50"), item.getPrecoUnitario());
        assertSame(pedido, item.getPedido());
    }

    @Test
    void deveLancarQuandoProdutoNaoEstiverNaLista() {
        // Arrange
        // retorna lista sem o SKU correto
        ProdutoResponseDTO dto = ProdutoResponseDTO.builder()
                .sku("OUTRO-SKU")
                .preco(new BigDecimal("99.99"))
                .build();
        when(produtoClient.buscarProduto("SKU-123"))
                .thenReturn(new ListaProdutoResponseDTO(List.of(dto)));

        // Act & Assert
        ProdutoNaoEncontradoException ex = assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> useCase.preencher(pedido)
        );
        assertTrue(ex.getMessage().contains("SKU-123"));
    }

    @Test
    void deveLancarQuandoFeignException() {
        // Arrange
        Request dummy = Request.create(
                Request.HttpMethod.GET, "/produtos/SKU-123",
                Collections.emptyMap(), new byte[0], null
        );
        FeignException fe = new FeignException.NotFound(
                "404 Not Found", dummy, new byte[0], Collections.emptyMap()
        );
        doThrow(fe).when(produtoClient).buscarProduto("SKU-123");

        // Act & Assert
        ProdutoNaoEncontradoException ex = assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> useCase.preencher(pedido)
        );
        assertTrue(ex.getMessage().contains("SKU-123"));
    }
}
