package br.com.postechfiap.fiap_pedido_service.usecases;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.PagamentoClient;
import br.com.postechfiap.fiap_pedido_service.adapters.clients.request.PerfilPagamentoRequestDTO;
import br.com.postechfiap.fiap_pedido_service.adapters.clients.request.SolicitarPagamentoRequestDTO;
import br.com.postechfiap.fiap_pedido_service.domain.DadosPagamento;
import br.com.postechfiap.fiap_pedido_service.domain.ItemPedido;
import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.PagamentoRecusadoException;
import br.com.postechfiap.fiap_pedido_service.usecase.SolicitarPagamentoUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitarPagamentoUseCaseImplTest {

    @Mock
    private PagamentoClient pagamentoClient;

    @InjectMocks
    private SolicitarPagamentoUseCaseImpl useCase;

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        DadosPagamento dados = DadosPagamento.builder()
                .numeroCartao("4111111111111111")
                .codigoSegurancaCartao("123")
                .nomeTitularCartao("Fulano Silva")
                .dataValidade(LocalDate.of(2030, 12, 31))
                .build();

        ItemPedido item = ItemPedido.builder()
                .skuProduto("SKU123")
                .quantidade(2)
                .precoUnitario(new BigDecimal("50.00"))
                .build();

        pedido = Pedido.builder()
                .id(UUID.randomUUID())
                .clienteId(42L)
                .valorTotal(new BigDecimal("100.00"))
                .dadosPagamento(dados)
                .itens(List.of(item))
                .build();
    }

    @Test
    void deveSolicitarPagamentoComSucesso() {
        // Arrange
        doNothing().when(pagamentoClient).criarPagamento(any(SolicitarPagamentoRequestDTO.class));

        // Act
        useCase.solicitar(pedido);

        // Assert
        ArgumentCaptor<SolicitarPagamentoRequestDTO> captor = ArgumentCaptor.forClass(SolicitarPagamentoRequestDTO.class);
        verify(pagamentoClient).criarPagamento(captor.capture());

        SolicitarPagamentoRequestDTO req = captor.getValue();
        assertEquals(pedido.getValorTotal(), req.getValor());
        assertEquals(pedido.getClienteId(), req.getIdCliente());
        assertEquals(pedido.getId(), req.getIdPedido());
        assertEquals("SKU123", req.getSkuProduto());

        PerfilPagamentoRequestDTO perfil = req.getPerfilPagamento();
        assertEquals(pedido.getDadosPagamento().getNumeroCartao(), perfil.getNumeroCartao());
        assertEquals(pedido.getDadosPagamento().getCodigoSegurancaCartao(), perfil.getCodigoSegurancaCartao());
        assertEquals(pedido.getDadosPagamento().getNomeTitularCartao(), perfil.getNomeTitularCartao());
        assertEquals(pedido.getDadosPagamento().getDataValidade(), perfil.getDataValidade());
    }

    @Test
    void deveLancarPagamentoRecusadoQuandoClienteNegado() {
        // Arrange
        doThrow(new RuntimeException("error"))
                .when(pagamentoClient).criarPagamento(any(SolicitarPagamentoRequestDTO.class));

        // Act & Assert
        PagamentoRecusadoException ex = assertThrows(
                PagamentoRecusadoException.class,
                () -> useCase.solicitar(pedido)
        );
        assertTrue(ex.getMessage().contains(pedido.getId().toString()));
    }

    @Test
    void deveLancarPagamentoRecusadoQuandoSemItens() {
        // Arrange
        pedido.setItens(List.of());

        // Act & Assert
        PagamentoRecusadoException ex = assertThrows(
                PagamentoRecusadoException.class,
                () -> useCase.solicitar(pedido)
        );
        // a mensagem inclui o ID do pedido
        assertTrue(ex.getMessage().contains(pedido.getId().toString()));

        // garante que não chamou o client
        verifyNoInteractions(pagamentoClient);
    }
}
