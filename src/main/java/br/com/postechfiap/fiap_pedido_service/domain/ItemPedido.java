package br.com.postechfiap.fiap_pedido_service.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@Builder(toBuilder = true)
@Entity
@Table(name = ItemPedido.TABLE_NAME)
public class ItemPedido {

    public static final String TABLE_NAME = "itens_pedido";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String skuProduto;

    private Integer quantidade;

    private BigDecimal precoUnitario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    @JsonIgnore
    private Pedido pedido;

    public BigDecimal calcularSubtotal() {
        return precoUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}
