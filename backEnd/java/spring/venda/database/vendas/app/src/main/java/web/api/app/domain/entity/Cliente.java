package web.api.app.domain.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table (name = "Cliente")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nome", length = 100)
    private String nome;

    /*Linkando os pedidos para os clientes*/
    @OneToMany (mappedBy = "cliente", fetch = FetchType.LAZY)
    private Set<Pedido> pedidos;
    public Set<Pedido> getPedidos() {return pedidos;}
    public void setPedidos(Set<Pedido> pedidos) {this.pedidos = pedidos;}
    /*====================================*/

    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public Cliente(){}
    public Cliente(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }
    public Cliente(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}