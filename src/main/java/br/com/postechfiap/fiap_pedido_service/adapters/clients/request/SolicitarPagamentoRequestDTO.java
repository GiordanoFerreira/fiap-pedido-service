package br.com.postechfiap.fiap_pedido_service.adapters.clients.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SolicitarPagamentoRequestDTO {

    private BigDecimal valor;
    private Long idCliente;
    private UUID idPedido;
    private String skuProduto;
    private PerfilPagamentoRequestDTO perfilPagamento;
}
