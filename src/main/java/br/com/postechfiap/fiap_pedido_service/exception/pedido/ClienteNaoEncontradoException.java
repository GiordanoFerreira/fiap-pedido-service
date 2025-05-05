package br.com.postechfiap.fiap_pedido_service.exception.pedido;

public class ClienteNaoEncontradoException extends RuntimeException {
  public ClienteNaoEncontradoException(String message) {
    super(message);
  }
}
