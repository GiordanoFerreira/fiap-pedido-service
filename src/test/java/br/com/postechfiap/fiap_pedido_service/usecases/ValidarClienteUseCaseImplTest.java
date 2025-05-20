package br.com.postechfiap.fiap_pedido_service.usecases;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.ClienteClient;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.ClienteNaoEncontradoException;
import br.com.postechfiap.fiap_pedido_service.usecase.ValidarClienteUseCaseImpl;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidarClienteUseCaseImplTest {

    @Mock
    private ClienteClient clienteClient;

    @InjectMocks
    private ValidarClienteUseCaseImpl useCase;

    @Test
    void deveValidarClienteComSucesso() {
        // Arrange
        when(clienteClient.buscarClientePorId(100L))
                .thenReturn(null); // ou um DTO “dummy” qualquer

        // Act
        useCase.validar(100L);

        // Assert
        verify(clienteClient).buscarClientePorId(100L);
    }

    @Test
    void deveLancarClienteNaoEncontradoQuandoFeignNotFound() {
        // Arrange
        Request dummyRequest = Request.create(
                Request.HttpMethod.GET,
                "/clientes/200",
                Collections.emptyMap(),
                new byte[0],
                null
        );
        FeignException.NotFound fe = new FeignException.NotFound(
                "404 Not Found",          // message
                dummyRequest,             // request
                new byte[0],              // body
                Collections.emptyMap()    // headers
        );
        doThrow(fe).when(clienteClient).buscarClientePorId(200L);

        // Act & Assert
        ClienteNaoEncontradoException ex = assertThrows(
                ClienteNaoEncontradoException.class,
                () -> useCase.validar(200L)
        );
        assertTrue(ex.getMessage().contains("200"));
    }
}
