package com.angeya.pig.dao;

import com.angeya.pig.pojo.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @ Decs:
 * @ Author: angeya
 * @ Date: 2021/3/29
 */

@Repository
public interface UniversityRepository extends JpaRepository<University, Integer> {
}
