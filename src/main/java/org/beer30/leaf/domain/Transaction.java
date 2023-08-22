package org.beer30.leaf.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.beer30.leaf.domain.enumeration.TransactionCode;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

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

    @Column(name = "env_id")
    private Integer envId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TransactionCode transactionCode;

    @NotNull
    @Column(name = "date", nullable = false)
    private Instant date;

    /*
        @Column(name = "amount", precision = 10, scale = 2)
        @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount", parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "USD")})
        private Money amount = Money.zero(CurrencyUnit.USD);
    */
    @NotNull
    @Column(length = 3, nullable = false)
    private String amountCurrency;
    @NotNull
    @Column(columnDefinition = "Decimal(19,2) default '0.00'", nullable = false)
    private BigDecimal amountValue;

    @Column(name = "note")
    private String note;

    @Column(name = "card_id")
    private Long cardId;

    public Money getAmount() {
        if (this.amountValue == null) {
            return Money.zero(CurrencyUnit.USD);
        } else {
            return Money.of(CurrencyUnit.of(this.amountCurrency), this.amountValue);
        }
    }

    public void setAmount(Money amount) {
        if (amount != null) {
            this.amountValue = amount.getAmount();
            this.amountCurrency = amount.getCurrencyUnit().getCode();
        }
    }

}
