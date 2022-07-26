package com.angeya.pig.pojo;

import javax.persistence.*;

/**
 * @ Decs:
 * @ Author: angeya
 * @ Date: 2021/3/29
 */

@Entity
@Table(name = "university")
public class University {
    public University() {}

    public University(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
