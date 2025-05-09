package br.com.postechfiap.fiap_pedido_service.adapters.clients;

import br.com.postechfiap.fiap_pedido_service.adapters.clients.request.SolicitarPagamentoRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "fiap-pagamento-service", url = "${service.pagamento.url}")
public interface PagamentoClient {

    @PostMapping("/pagamentos/compra")
    void criarPagamento(@RequestBody SolicitarPagamentoRequestDTO request);
}
