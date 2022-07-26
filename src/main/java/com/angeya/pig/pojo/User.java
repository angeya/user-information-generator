package com.angeya.pig.pojo;

import javax.persistence.*;

/**
 * @ Decs:
 * @ Author: angeya
 * @ Date: 2021/3/29
 */

@Entity
@Table(name = "user")
public class User {

    public User() {}
    public User(String name, Byte gender, Short age, Integer universityId, Integer cityId) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.universityId = universityId;
        this.cityId = cityId;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "gender")
    private Byte gender;
    @Column(name = "age")
    private Short age;
    @Column(name = "university_id")
    private Integer universityId;
    @Column(name = "city_id")
    private Integer cityId;

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

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Short getAge() {
        return age;
    }

    public void setAge(Short age) {
        this.age = age;
    }

    public Integer getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Integer universityId) {
        this.universityId = universityId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }
}
