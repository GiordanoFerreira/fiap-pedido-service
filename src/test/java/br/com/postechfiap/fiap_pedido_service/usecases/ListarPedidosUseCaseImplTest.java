package br.com.postechfiap.fiap_pedido_service.usecases;

import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.interfaces.repository.PedidoRepository;
import br.com.postechfiap.fiap_pedido_service.usecase.ListarPedidosUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListarPedidosUseCaseImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private ListarPedidosUseCaseImpl useCase;

    private Pedido p1;
    private Pedido p2;

    @BeforeEach
    void setUp() {
        p1 = Pedido.builder()
                .id(UUID.randomUUID())
                .clienteId(10L)
                .valorTotal(new BigDecimal("50.00"))
                .build();
        p2 = Pedido.builder()
                .id(UUID.randomUUID())
                .clienteId(20L)
                .valorTotal(new BigDecimal("100.00"))
                .build();
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremPedidos() {
        // Arrange
        when(pedidoRepository.findAll()).thenReturn(List.of());

        // Act
        List<Pedido> resultado = useCase.executar();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(pedidoRepository).findAll();
    }

    @Test
    void deveRetornarTodosOsPedidosQuandoExistirem() {
        // Arrange
        when(pedidoRepository.findAll()).thenReturn(List.of(p1, p2));

        // Act
        List<Pedido> resultado = useCase.executar();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(List.of(p1, p2), resultado);
        verify(pedidoRepository).findAll();
    }
}