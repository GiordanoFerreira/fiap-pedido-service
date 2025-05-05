package br.com.postechfiap.fiap_pedido_service.exception.pedido;

public class ProdutoNaoEncontradoException extends RuntimeException {
    public ProdutoNaoEncontradoException(String message) {
        super(message);
    }
}
