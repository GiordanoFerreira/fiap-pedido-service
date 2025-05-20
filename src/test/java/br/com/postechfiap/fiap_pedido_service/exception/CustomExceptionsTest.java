package br.com.postechfiap.fiap_pedido_service.exception;

import br.com.postechfiap.fiap_pedido_service.exception.pedido.ClienteNaoEncontradoException;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.EstoqueInsuficienteException;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.PagamentoRecusadoException;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.ProdutoNaoEncontradoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomExceptionsTest {

    @Test
    void clienteNaoEncontradoException_messageAndType() {
        ClienteNaoEncontradoException ex = new ClienteNaoEncontradoException("erro cliente");
        assertEquals("erro cliente", ex.getMessage());
    }

    @Test
    void estoqueInsuficienteException_messageAndType() {
        EstoqueInsuficienteException ex = new EstoqueInsuficienteException("sem estoque");
        assertEquals("sem estoque", ex.getMessage());
    }

    @Test
    void pagamentoRecusadoException_messageAndType() {
        PagamentoRecusadoException ex = new PagamentoRecusadoException("recusado");
        assertEquals("recusado", ex.getMessage());
    }

    @Test
    void produtoNaoEncontradoException_messageAndType() {
        ProdutoNaoEncontradoException ex = new ProdutoNaoEncontradoException("produto não encontrado");
        assertEquals("produto não encontrado", ex.getMessage());
    }
}
