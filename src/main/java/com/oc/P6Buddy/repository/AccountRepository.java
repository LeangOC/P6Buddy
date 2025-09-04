// com.oc.P6Buddy.repository.AccountRepository.java
package com.oc.P6Buddy.repository;

import com.oc.P6Buddy.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUserId(Integer userId);
}
