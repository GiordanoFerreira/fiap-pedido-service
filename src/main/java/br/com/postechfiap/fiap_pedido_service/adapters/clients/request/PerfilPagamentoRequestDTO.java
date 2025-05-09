package br.com.postechfiap.fiap_pedido_service.adapters.clients.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PerfilPagamentoRequestDTO {

    private String numeroCartao;
    private String codigoSegurancaCartao;
    private String nomeTitularCartao;
    private LocalDate dataValidade;
}
