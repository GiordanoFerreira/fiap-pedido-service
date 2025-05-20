package br.com.postechfiap.fiap_pedido_service.usecases;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.EstoqueClient;
import br.com.postechfiap.fiap_pedido_service.domain.ItemPedido;
import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.EstoqueInsuficienteException;
import br.com.postechfiap.fiap_pedido_service.usecase.BaixarEstoqueUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaixarEstoqueUseCaseImplTest {

    @Mock
    private EstoqueClient estoqueClient;

    @InjectMocks
    private BaixarEstoqueUseCaseImpl useCase;

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedido = Pedido.builder()
                .id(UUID.randomUUID())
                .clienteId(1L)
                .valorTotal(BigDecimal.ZERO)
                .itens(List.of(
                        ItemPedido.builder()
                                .skuProduto("SKU-OK")
                                .quantidade(5)
                                .precoUnitario(new BigDecimal("1.00"))
                                .build(),
                        ItemPedido.builder()
                                .skuProduto("SKU-ERR")
                                .quantidade(2)
                                .precoUnitario(new BigDecimal("2.00"))
                                .build()
                ))
                .build();
    }

    @Test
    void deveBaixarEstoqueParaCadaItem_comSucesso() {
        // Arrange
        doNothing().when(estoqueClient)
                .baixarEstoque(eq("SKU-OK"), any());
        doNothing().when(estoqueClient)
                .baixarEstoque(eq("SKU-ERR"), any());

        // Act
        useCase.baixar(pedido);

        // Assert
        verify(estoqueClient, times(1))
                .baixarEstoque(eq("SKU-OK"), any());
        verify(estoqueClient, times(1))
                .baixarEstoque(eq("SKU-ERR"), any());
    }

    @Test
    void deveLancarExcecaoQuandoEstoqueInsuficienteParaAlgumItem() {
        // Arrange
        // simula falha no segundo item
        doNothing().when(estoqueClient)
                .baixarEstoque(eq("SKU-OK"), any());
        doThrow(new RuntimeException("Downstream error"))
                .when(estoqueClient)
                .baixarEstoque(eq("SKU-ERR"), any());

        // Act & Assert
        EstoqueInsuficienteException ex = assertThrows(
                EstoqueInsuficienteException.class,
                () -> useCase.baixar(pedido)
        );
        assertTrue(ex.getMessage().contains("SKU-ERR"));

        // garante que não continuará após a falha
        verify(estoqueClient, times(1))
                .baixarEstoque(eq("SKU-OK"), any());
        verify(estoqueClient, times(1))
                .baixarEstoque(eq("SKU-ERR"), any());
    }
}