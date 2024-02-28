package msc.pedido.pedido.model;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import msc.pedido.pedido.enums.StatusPedido;

@Getter
@Setter
@Entity
@Table(name = "pedidos")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "pedido_id")
public class Pedidos {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pedido_id;
    
    @Column(nullable = false)
    private long user_id;

    @Column(nullable = false)
    private long product_id;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false)
    private String endereco_entrega;

    @Column(nullable = false)
    private String numero_telefone_entrega;

    @Column(nullable = false)
    private double frete;

    @Column(nullable = false)
    private double total_pedido;

    private Timestamp data_pedido;
    
    @Enumerated(EnumType.STRING)
    private StatusPedido status_pedido;
}
