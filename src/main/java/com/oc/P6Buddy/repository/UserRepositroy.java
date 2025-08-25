package com.oc.P6Buddy.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.oc.P6Buddy.model.User;


@Repository
public interface UserRepositroy extends CrudRepository<User,Integer> {
}
