package br.com.postechfiap.fiap_pedido_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DadosPagamento {

    @Column(name = "numero_cartao")
    private String numeroCartao;

    @Column(name = "codigo_seguranca_cartao")
    private String codigoSegurancaCartao;

    @Column(name = "nome_titular_cartao")
    private String nomeTitularCartao;

    @Column(name = "data_validade")
    private LocalDate dataValidade;
}
