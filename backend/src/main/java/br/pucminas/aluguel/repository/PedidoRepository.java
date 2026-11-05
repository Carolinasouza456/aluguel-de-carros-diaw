package br.pucminas.aluguel.repository;

import br.pucminas.aluguel.model.Pedido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByClienteIdOrderByDataCriacaoDesc(Long clienteId);

    List<Pedido> findAllByOrderByDataCriacaoDesc();

    List<Pedido> findByClienteId(Long clienteId);
}
