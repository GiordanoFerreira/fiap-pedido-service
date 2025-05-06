package br.com.postechfiap.fiap_pedido_service.usecase;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.ClienteClient;
import br.com.postechfiap.fiap_pedido_service.exception.pedido.ClienteNaoEncontradoException;
import br.com.postechfiap.fiap_pedido_service.interfaces.usecases.ValidarClienteUseCase;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidarClienteUseCaseImpl implements ValidarClienteUseCase {

    private final ClienteClient clienteClient;

    @Override
    public void validar(Long clienteId) {

        try {
            clienteClient.buscarClientePorId(clienteId);
        } catch (FeignException.NotFound e) {
            throw new ClienteNaoEncontradoException("Cliente com ID: " + clienteId + " não encontrado.");
        }
    }
}
