package com.angeya.pig.pojo;

/**
 * @ Decs:
 * @ Author: angeya
 * @ Date: 2021/3/28
 */

public class UserInfo {

    private String name;

    private Short age;

    private Byte gender;

    private String city;

    private String university;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getAge() {
        return age;
    }

    public void setAge(Short age) {
        this.age = age;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", city='" + city + '\'' +
                ", university='" + university + '\'' +
                '}';
    }
}
