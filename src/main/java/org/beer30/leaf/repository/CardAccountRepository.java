package org.beer30.leaf.repository;

import org.beer30.leaf.domain.CardAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardAccountRepository extends JpaRepository<CardAccount, Long> {

    CardAccount findCardAccountByCardNumber(String cardNumber);
}
