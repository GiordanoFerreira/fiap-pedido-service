package br.com.postechfiap.fiap_pedido_service.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}
