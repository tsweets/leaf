package org.beer30.leaf.web.rest.dto;


import jakarta.validation.constraints.NotNull;
import org.beer30.leaf.domain.enumeration.TransactionCode;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the Transaction entity.
 */
public class TransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer envId;

    @NotNull
    private TransactionCode transactionCode;

    @NotNull
    private Instant date;

    private Money amount = Money.zero(CurrencyUnit.USD);

    private String note;

    private Long cardId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEnvId() {
        return envId;
    }

    public void setEnvId(Integer envId) {
        this.envId = envId;
    }

    public TransactionCode getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(TransactionCode code) {
        this.transactionCode = code;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Money getAmount() {
        return amount;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransactionDTO transactionDTO = (TransactionDTO) o;

        return Objects.equals(id, transactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "id=" + id +
                ", envId='" + envId + "'" +
                ", code='" + transactionCode + "'" +
                ", date='" + date + "'" +
                ", amount='" + amount + "'" +
                ", note='" + note + "'" +
                '}';
    }
}
