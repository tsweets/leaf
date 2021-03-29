package org.beer30.leaf.domain;

import lombok.Data;
import org.beer30.leaf.domain.enumeration.TransactionCode;
import org.hibernate.annotations.Type;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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

    @Column(name = "env_id")
    private Integer envId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionCode transactionCode;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    @Column(name = "amount", precision = 10, scale = 2)
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount", parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "USD")})
    private Money amount = Money.zero(CurrencyUnit.USD);

    @Column(name = "note")
    private String note;

    @Column(name = "card_id")
    private Long cardId;


}
