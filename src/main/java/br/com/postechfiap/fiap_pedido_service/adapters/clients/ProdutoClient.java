package br.com.postechfiap.fiap_pedido_service.adapters.clients;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.dto.ListaProdutoResponseDTO;
import br.com.postechfiap.fiap_pedido_service.adapters.clients.dto.ProdutoResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "fiap-produto-service", url = "${service.produto.url}")
public interface ProdutoClient {

    @GetMapping("/produto")
    ListaProdutoResponseDTO buscarProduto(@RequestParam("query") String query);
}
