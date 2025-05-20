package br.com.postechfiap.fiap_pedido_service.exception;

import br.com.postechfiap.fiap_pedido_service.exception.pedido.ClienteNaoEncontradoException;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.EstoqueInsuficienteException;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.PagamentoRecusadoException;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.ProdutoNaoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleClienteNaoEncontrado() {
        var ex = new ClienteNaoEncontradoException("Cliente 123 não existe");

        ResponseEntity<ApiErrorResponse> resp = handler.handleClienteNaoEncontrado(ex);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        ApiErrorResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals(404, body.status());
        assertEquals("NOT_FOUND", body.httpError());
        assertEquals(List.of("Cliente 123 não existe"), body.message());
    }

    @Test
    void handleProdutoNaoEncontrado() {
        var ex = new ProdutoNaoEncontradoException("SKU XYZ não existe");

        ResponseEntity<ApiErrorResponse> resp = handler.handleProdutoNaoEncontrado(ex);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        ApiErrorResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals(404, body.status());
        assertEquals("NOT_FOUND", body.httpError());
        assertEquals(List.of("SKU XYZ não existe"), body.message());
    }

    @Test
    void handleEstoqueInsuficiente() {
        var ex = new EstoqueInsuficienteException("Sem estoque para ABC");

        ResponseEntity<ApiErrorResponse> resp = handler.handleEstoqueInsuficiente(ex);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        ApiErrorResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("BAD_REQUEST", body.httpError());
        assertEquals(List.of("Sem estoque para ABC"), body.message());
    }

    @Test
    void handlePagamentoRecusado() {
        var ex = new PagamentoRecusadoException("Cartão recusado");

        ResponseEntity<ApiErrorResponse> resp = handler.handlePagamentoRecusado(ex);

        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        ApiErrorResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("BAD_REQUEST", body.httpError());
        assertEquals(List.of("Cartão recusado"), body.message());
    }

    @Test
    void handleOutros() {
        var ex = new RuntimeException("erro genérico");

        ResponseEntity<ApiErrorResponse> resp = handler.handleOutros(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        ApiErrorResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals(500, body.status());
        assertEquals("INTERNAL_SERVER_ERROR", body.httpError());
        // message() retorna uma lista com uma única string
        assertEquals(1, body.message().size());
        assertTrue(body.message().get(0).contains("Erro inesperado: erro genérico"));
    }
}
