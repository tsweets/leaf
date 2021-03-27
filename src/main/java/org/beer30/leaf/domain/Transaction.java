package org.beer30.leaf.domain;

import lombok.Data;
import org.beer30.leaf.domain.enumeration.TransactionType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A Transaction.
 */
@Data
@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "env_id", nullable = false)
    private Integer envId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionType type;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "note")
    private String note;

    @Column(name = "card_id")
    private Long cardId;


}
