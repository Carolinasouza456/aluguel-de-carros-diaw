package br.pucminas.aluguel.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "contrato")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    // quem fica como dono do automovel neste contrato
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_proprietario", nullable = false, length = 20)
    private TipoProprietario tipoProprietario;

    @Column(name = "data_contrato", nullable = false)
    private LocalDate dataContrato = LocalDate.now();

    @OneToOne(mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true)
    private ContratoCredito contratoCredito;

    public Contrato() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public TipoProprietario getTipoProprietario() { return tipoProprietario; }
    public void setTipoProprietario(TipoProprietario tipoProprietario) { this.tipoProprietario = tipoProprietario; }
    public LocalDate getDataContrato() { return dataContrato; }
    public void setDataContrato(LocalDate dataContrato) { this.dataContrato = dataContrato; }
    public ContratoCredito getContratoCredito() { return contratoCredito; }
    public void setContratoCredito(ContratoCredito contratoCredito) { this.contratoCredito = contratoCredito; }
}
