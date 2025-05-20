package br.com.postechfiap.fiap_pedido_service.usecases;

import br.com.postechfiap.fiap_pedido_service.domain.ItemPedido;
import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.usecase.CalcularValorTotalPedidoUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalcularValorTotalPedidoUseCaseImplTest {

    private CalcularValorTotalPedidoUseCaseImpl useCase;
    private Pedido pedido;

    @BeforeEach
    void setUp() {
        useCase = new CalcularValorTotalPedidoUseCaseImpl();
        pedido = Pedido.builder()
                .id(UUID.randomUUID())
                .clienteId(1L)
                .itens(null)
                .valorTotal(null)
                .build();
    }

    @Test
    void deveZerarValorQuandoItensNulo() {
        // Arrange
        pedido.setItens(null);

        // Act
        useCase.calcular(pedido);

        // Assert
        assertEquals(BigDecimal.ZERO, pedido.getValorTotal());
    }

    @Test
    void deveZerarValorQuandoListaDeItensVazia() {
        // Arrange
        pedido.setItens(List.of());

        // Act
        useCase.calcular(pedido);

        // Assert
        assertEquals(BigDecimal.ZERO, pedido.getValorTotal());
    }

    @Test
    void deveSomarSubtotaisParaListaDeItens() {
        // Arrange
        ItemPedido i1 = ItemPedido.builder()
                .skuProduto("A")
                .quantidade(2)
                .precoUnitario(new BigDecimal("10.50"))
                .build();
        ItemPedido i2 = ItemPedido.builder()
                .skuProduto("B")
                .quantidade(3)
                .precoUnitario(new BigDecimal("5.00"))
                .build();
        pedido.setItens(List.of(i1, i2));

        // Act
        useCase.calcular(pedido);

        // Assert
        // i1 subtotal = 2 * 10.50 = 21.00
        // i2 subtotal = 3 * 5.00  = 15.00
        assertEquals(new BigDecimal("36.00"), pedido.getValorTotal());
    }
}