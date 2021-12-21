package com.itananina.weblamp.weblamp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "orders_products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="prod_id")
    private Product product;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @Column(name="amount")
    private Integer amount;

    public OrderProduct(Product product, Order order) {
        this.product = product;
        this.order = order;
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "id=" + id +
                ", product=" + product +
                ", amount=" + amount +
                '}';
    }
}
