package br.com.postechfiap.fiap_pedido_service.adapters.clients.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProdutoResponseDTO {

    private Long id;
    private String sku;
    private String nome;
    private BigDecimal preco;
}
