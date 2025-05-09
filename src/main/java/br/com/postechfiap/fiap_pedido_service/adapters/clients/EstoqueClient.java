package br.com.postechfiap.fiap_pedido_service.adapters.clients;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.request.BaixaEstoqueRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "fiap-estoque-service", url = "${service.estoque.url}")
public interface EstoqueClient {

    @PutMapping("/estoque/reduzir/{sku}")
    void baixarEstoque(@PathVariable("sku") String sku, @RequestBody BaixaEstoqueRequest request);

    @PutMapping("/estoque/adicionar/{sku}")
    void reporEstoque(@PathVariable("sku") String sku, @RequestBody BaixaEstoqueRequest request);
}
