package com.angeya.pig.dao;
import com.angeya.pig.pojo.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @ Decs: 城市
 * @ Author: angeya
 * @ Date: 2021/3/29
 */
@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
}
