package org.beer30.leaf.repository;

import org.beer30.leaf.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByCardId(Long cardId);
}
