package br.com.postechfiap.fiap_pedido_service.controller;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.ClienteClient;
import br.com.postechfiap.fiap_pedido_service.adapters.clients.dto.ClienteResponseDTO;
import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.interfaces.usecases.ListarPedidosUseCase;
import br.com.postechfiap.fiap_pedido_service.interfaces.usecases.ValidarClienteUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/pedidos")
@RequiredArgsConstructor
@Validated
public class PedidoController {

    private final ListarPedidosUseCase listarPedidosUseCase;
    private final ValidarClienteUseCase validarClienteUseCase;
    private final ClienteClient clienteClient;

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedido() {
        List<Pedido> pedidos = listarPedidosUseCase.executar();

        return ResponseEntity
                .ok()
                .body(pedidos);
    }

    @GetMapping("/validar-cliente/{id}")
    public ResponseEntity<ClienteResponseDTO> validarCliente(@PathVariable Long id) {
        validarClienteUseCase.validar(id);
        System.out.println("Cliente Validado!!!");
        return ResponseEntity.ok(clienteClient.buscarClientePorId(id));
    }
}
