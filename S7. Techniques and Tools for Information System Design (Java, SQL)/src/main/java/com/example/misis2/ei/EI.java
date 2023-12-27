package com.example.misis2.ei;

import jakarta.persistence.*;

@Entity
@Table(name = "ei")
public class EI {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_ei;
    private String name;

    protected EI() {}

    public EI(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id_ei;
    }

    public String getName() {
        return name;
    }
}
