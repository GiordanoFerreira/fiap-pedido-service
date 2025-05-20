package br.com.postechfiap.fiap_pedido_service.usecases;

import br.com.postechfiap.fiap_pedido_service.domain.ItemPedido;
import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.domain.StatusPedido;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.ClienteNaoEncontradoException;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.EstoqueInsuficienteException;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.PagamentoRecusadoException;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.ProdutoNaoEncontradoException;
import br.com.postechfiap.fiap_pedido_service.interfaces.repository.PedidoRepository;
import br.com.postechfiap.fiap_pedido_service.interfaces.usecases.*;
import br.com.postechfiap.fiap_pedido_service.usecase.ProcessarPedidoUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessarPedidoUseCaseImplTest {

    @Mock
    private ValidarClienteUseCase validarClienteUseCase;
    @Mock
    private PreencherInformacoesProdutosUseCase preencherInformacoesProdutosUseCase;
    @Mock
    private CalcularValorTotalPedidoUseCase calcularValorTotalPedidoUseCase;
    @Mock
    private BaixarEstoqueUseCase baixarEstoqueUseCase;
    @Mock
    private SolicitarPagamentoUseCase solicitarPagamentoUseCase;
    @Mock
    private ReporEstoqueUseCase reporEstoqueUseCase;
    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private ProcessarPedidoUseCaseImpl processarPedidoUseCase;

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedido = Pedido.builder()
                .id(UUID.randomUUID())
                .clienteId(1L)
                .valorTotal(BigDecimal.ZERO)
                .statusPedido(null)
                .itens(List.of(
                        ItemPedido.builder()
                                .skuProduto("SKU123")
                                .quantidade(2)
                                .precoUnitario(new BigDecimal("10"))
                                .build()
                ))
                .build();
    }

    @Test
    void deveProcessarPedidoComSucesso() {
        // Arrange
        doNothing().when(validarClienteUseCase).validar(pedido.getClienteId());
        doNothing().when(preencherInformacoesProdutosUseCase).preencher(pedido);
        doNothing().when(calcularValorTotalPedidoUseCase).calcular(pedido);
        doNothing().when(baixarEstoqueUseCase).baixar(pedido);
        doNothing().when(solicitarPagamentoUseCase).solicitar(pedido);

        // Act
        processarPedidoUseCase.executar(pedido);

        // Assert
        verify(validarClienteUseCase).validar(pedido.getClienteId());
        verify(preencherInformacoesProdutosUseCase).preencher(pedido);
        verify(calcularValorTotalPedidoUseCase).calcular(pedido);
        verify(baixarEstoqueUseCase).baixar(pedido);
        verify(solicitarPagamentoUseCase).solicitar(pedido);
        // aceita uma ou mais invocações de save()
        verify(pedidoRepository, atLeastOnce()).save(pedido);

        assertEquals(StatusPedido.FECHADO_COM_SUCESSO, pedido.getStatusPedido());
    }

    @Test
    void deveFecharPedidoComStatusSemEstoqueQuandoEstoqueInsuficiente() {
        // Arrange
        doNothing().when(validarClienteUseCase).validar(pedido.getClienteId());
        doNothing().when(preencherInformacoesProdutosUseCase).preencher(pedido);
        doNothing().when(calcularValorTotalPedidoUseCase).calcular(pedido);
        doThrow(new EstoqueInsuficienteException("Estoque insuficiente"))
                .when(baixarEstoqueUseCase).baixar(pedido);

        // Act
        processarPedidoUseCase.executar(pedido);

        // Assert
        verify(validarClienteUseCase).validar(pedido.getClienteId());
        verify(preencherInformacoesProdutosUseCase).preencher(pedido);
        verify(calcularValorTotalPedidoUseCase).calcular(pedido);
        verify(baixarEstoqueUseCase).baixar(pedido);
        // aceita uma ou mais invocações de save()
        verify(pedidoRepository, atLeastOnce()).save(pedido);

        assertEquals(StatusPedido.FECHADO_SEM_ESTOQUE, pedido.getStatusPedido());

        verify(solicitarPagamentoUseCase, never()).solicitar(any());
    }

    @Test
    void deveFecharPedidoComStatusSemCreditoQuandoPagamentoRecusado() {
        // Arrange
        doNothing().when(validarClienteUseCase).validar(pedido.getClienteId());
        doNothing().when(preencherInformacoesProdutosUseCase).preencher(pedido);
        doNothing().when(calcularValorTotalPedidoUseCase).calcular(pedido);
        doNothing().when(baixarEstoqueUseCase).baixar(pedido);
        doThrow(new PagamentoRecusadoException("Pagamento recusado"))
                .when(solicitarPagamentoUseCase).solicitar(pedido);
        doNothing().when(reporEstoqueUseCase).repor(pedido);

        // Act
        processarPedidoUseCase.executar(pedido);

        // Assert
        verify(validarClienteUseCase).validar(pedido.getClienteId());
        verify(preencherInformacoesProdutosUseCase).preencher(pedido);
        verify(calcularValorTotalPedidoUseCase).calcular(pedido);
        verify(baixarEstoqueUseCase).baixar(pedido);
        verify(solicitarPagamentoUseCase).solicitar(pedido);
        // aceita uma ou mais invocações de save()
        verify(pedidoRepository, atLeastOnce()).save(pedido);

        assertEquals(StatusPedido.FECHADO_SEM_CREDITO, pedido.getStatusPedido());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExiste() {
        // Arrange
        doThrow(new ClienteNaoEncontradoException("Cliente não encontrado"))
                .when(validarClienteUseCase).validar(pedido.getClienteId());

        // Act & Assert
        ClienteNaoEncontradoException ex = assertThrows(
                ClienteNaoEncontradoException.class,
                () -> processarPedidoUseCase.executar(pedido)
        );

        assertEquals("Cliente não encontrado", ex.getMessage());

        verify(preencherInformacoesProdutosUseCase, never()).preencher(any());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExiste() {
        // Arrange
        doNothing().when(validarClienteUseCase).validar(pedido.getClienteId());
        doThrow(new ProdutoNaoEncontradoException("Produto inválido"))
                .when(preencherInformacoesProdutosUseCase).preencher(pedido);

        // Act & Assert
        ProdutoNaoEncontradoException ex = assertThrows(
                ProdutoNaoEncontradoException.class,
                () -> processarPedidoUseCase.executar(pedido)
        );

        assertEquals("Produto inválido", ex.getMessage());

        verify(calcularValorTotalPedidoUseCase, never()).calcular(any());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoPedidoNaoTemItens() {
        // Arrange
        pedido.setItens(List.of());

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> processarPedidoUseCase.executar(pedido)
        );

        assertEquals("Pedido sem itens não pode ser processado.", ex.getMessage());

        verify(validarClienteUseCase, never()).validar(any());
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoPedidoSemItensNulos() {
        // Arrange
        pedido.setItens(null);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> processarPedidoUseCase.executar(pedido)
        );
        assertEquals("Pedido sem itens não pode ser processado.", ex.getMessage());

        // Certifica que nenhum dos usecases nem o repositório foram invocados
        verifyNoInteractions(
                validarClienteUseCase,
                preencherInformacoesProdutosUseCase,
                calcularValorTotalPedidoUseCase,
                baixarEstoqueUseCase,
                solicitarPagamentoUseCase,
                reporEstoqueUseCase,
                pedidoRepository
        );
    }
}

