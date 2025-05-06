package br.com.postechfiap.fiap_pedido_service.adapters.clients.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private LocalDateTime dataNascimento;
    private String cpf;
    private String email;
    private EnderecoResponseDTO endereco;
}
