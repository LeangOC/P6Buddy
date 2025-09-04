// com.oc.P6Buddy.repository.AccountRepository.java
package com.oc.P6Buddy.repository;

import com.oc.P6Buddy.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
