package com.itananina.weblamp.weblamp.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="status")
    private String status;

    @Column(name="total")
    private int total;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "order",cascade=CascadeType.PERSIST, orphanRemoval=true)
    private List<OrderProduct> orderProducts;

    public Order(String status, User user) {
        this.status = status;
        this.user = user;
    }

    public List<OrderProduct> getOrderProducts() {
        if(orderProducts==null) {
            orderProducts = new ArrayList<>();
        }
        return orderProducts;
    }
}
