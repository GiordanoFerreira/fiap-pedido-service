package br.com.postechfiap.fiap_pedido_service.controller;

import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import br.com.postechfiap.fiap_pedido_service.interfaces.usecases.ListarPedidosUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/pedidos")
@RequiredArgsConstructor
@Validated
public class PedidoController {

    private final ListarPedidosUseCase listarPedidosUseCase;

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedido() {
        List<Pedido> pedidos = listarPedidosUseCase.executar();

        return ResponseEntity
                .ok()
                .body(pedidos);
    }
}
