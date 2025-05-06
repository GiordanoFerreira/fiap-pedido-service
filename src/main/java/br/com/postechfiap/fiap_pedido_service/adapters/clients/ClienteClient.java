package br.com.postechfiap.fiap_pedido_service.adapters.clients;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.dto.ClienteResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "fiap-cliente-service", url = "${service.cliente.url}")
public interface ClienteClient {

    @GetMapping("/clientes/buscar/{id}")
    ClienteResponseDTO buscarClientePorId(@PathVariable("id") Long id);
}
