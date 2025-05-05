package br.com.postechfiap.fiap_pedido_service.adapters.clients;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.request.BaixaEstoqueRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "fiap-estoque-service", url = "${service.estoque.url}")
public interface EstoqueClient {

    @PostMapping("/estoque/baixar")
    void baixarEstoque(@RequestBody BaixaEstoqueRequest request);

    @PostMapping("/estoque/repor")
    void reporEstoque(@RequestBody BaixaEstoqueRequest request);
}
