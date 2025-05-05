package br.com.postechfiap.fiap_pedido_service.exception.pedido;

public class EstoqueInsuficienteException extends RuntimeException {
    public EstoqueInsuficienteException(String message) {
        super(message);
    }
}
