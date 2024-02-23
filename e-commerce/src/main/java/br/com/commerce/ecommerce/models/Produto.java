package br.com.commerce.ecommerce.models;



import br.com.commerce.ecommerce.enums.Categoria;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "produto")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Produto {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id 
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private double preco_unitario;

    @Column(nullable = false)
    private String imagem;

    @Column(nullable = false, unique = true)
    private int codigo_barra;

    @Column(nullable = false)
    private Categoria categoria;

    } 

