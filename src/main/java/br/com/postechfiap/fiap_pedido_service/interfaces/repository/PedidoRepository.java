package br.com.postechfiap.fiap_pedido_service.interfaces.repository;

import br.com.postechfiap.fiap_pedido_service.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
}
