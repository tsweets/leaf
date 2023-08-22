package org.beer30.leaf.web.rest.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.beer30.leaf.domain.enumeration.CardStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 *
 */
public class CardDTO implements Serializable {

    private Long id;

    private Integer envId;

    private Long externalId;

    @NotNull
    private String cardNumber;

    private String ddaAccountNumber;

    @NotNull
    private CardStatus cardStatus;

    @Size(max = 60)
    private String imprintedName;

    private BigDecimal balance;

    private Long cardholderId;

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

    public Long getExternalId() {
        return externalId;
    }

    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getDdaAccountNumber() {
        return ddaAccountNumber;
    }

    public void setDdaAccountNumber(String ddaAccountNumber) {
        this.ddaAccountNumber = ddaAccountNumber;
    }

    public CardStatus getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(CardStatus cardStatus) {
        this.cardStatus = cardStatus;
    }

    public String getImprintedName() {
        return imprintedName;
    }

    public void setImprintedName(String imprintedName) {
        this.imprintedName = imprintedName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getCardholderId() {
        return cardholderId;
    }

    public void setCardholderId(Long cardholderId) {
        this.cardholderId = cardholderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CardDTO cardDTO = (CardDTO) o;

        return Objects.equals(id, cardDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CardDTO{" +
                "id=" + id +
                ", envId='" + envId + "'" +
                ", externalId='" + externalId + "'" +
                ", cardNumber='" + cardNumber + "'" +
                ", ddaAccountNumber='" + ddaAccountNumber + "'" +
                ", cardStatus='" + cardStatus + "'" +
                ", imprintedName='" + imprintedName + "'" +
                ", balance='" + balance + "'" +
                '}';
    }
}
