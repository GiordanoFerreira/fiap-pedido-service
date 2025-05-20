package br.com.postechfiap.fiap_pedido_service.usecases;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.EstoqueClient;
import br.com.postechfiap.fiap_pedido_service.adapters.clients.request.BaixaEstoqueRequest;
import br.com.postechfiap.fiap_pedido_service.domain.ItemPedido;
import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.usecase.ReporEstoqueUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporEstoqueUseCaseImplTest {

    @Mock
    private EstoqueClient estoqueClient;

    @InjectMocks
    private ReporEstoqueUseCaseImpl useCase;

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        ItemPedido i1 = ItemPedido.builder()
                .skuProduto("SKU-A")
                .quantidade(3)
                .precoUnitario(new BigDecimal("1.00"))
                .build();
        ItemPedido i2 = ItemPedido.builder()
                .skuProduto("SKU-B")
                .quantidade(5)
                .precoUnitario(new BigDecimal("2.00"))
                .build();
        pedido = Pedido.builder()
                .id(UUID.randomUUID())
                .clienteId(2L)
                .itens(List.of(i1, i2))
                .build();
    }

    @Test
    void deveReporEstoqueParaCadaItem_semErros() {
        // Arrange
        doNothing().when(estoqueClient)
                .reporEstoque(eq("SKU-A"), any(BaixaEstoqueRequest.class));
        doNothing().when(estoqueClient)
                .reporEstoque(eq("SKU-B"), any(BaixaEstoqueRequest.class));

        // Act
        useCase.repor(pedido);

        // Assert
        verify(estoqueClient, times(1))
                .reporEstoque(eq("SKU-A"), any(BaixaEstoqueRequest.class));
        verify(estoqueClient, times(1))
                .reporEstoque(eq("SKU-B"), any(BaixaEstoqueRequest.class));
    }

    @Test
    void deveContinuarMesmoSeReporEstoqueFalhar() {
        // Arrange
        doNothing().when(estoqueClient)
                .reporEstoque(eq("SKU-A"), any(BaixaEstoqueRequest.class));
        doThrow(new RuntimeException("downstream error"))
                .when(estoqueClient)
                .reporEstoque(eq("SKU-B"), any(BaixaEstoqueRequest.class));

        // Act
        useCase.repor(pedido);

        // Assert
        verify(estoqueClient, times(1))
                .reporEstoque(eq("SKU-A"), any(BaixaEstoqueRequest.class));
        verify(estoqueClient, times(1))
                .reporEstoque(eq("SKU-B"), any(BaixaEstoqueRequest.class));
    }
}
