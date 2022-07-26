package com.angeya.pig.dao;

import com.angeya.pig.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @ Decs:
 * @ Author: angeya
 * @ Date: 2021/3/29
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
