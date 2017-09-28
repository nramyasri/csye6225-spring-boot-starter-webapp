package com.csye6225.demo;

import com.csye6225.demo.pojo.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByName(String username);

}

