package br.com.postechfiap.fiap_pedido_service.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@Builder(toBuilder = true)
@Entity
@Table(name = Pedido.TABLE_NAME)
public class Pedido extends BaseEntity<UUID>{

    public static final String TABLE_NAME = "pedidos";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Long clienteId;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens;

    @Enumerated(EnumType.STRING)
    private StatusPedido statusPedido;

    private BigDecimal valorTotal;

    @Embedded
    private DadosPagamento dadosPagamento;

    public void calcularEAtualizarValorTotal() {
        if (itens == null || itens.isEmpty()) {
            this.valorTotal = BigDecimal.ZERO;
        } else {
            this.valorTotal = itens.stream()
                    .map(ItemPedido::calcularSubtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }
}
