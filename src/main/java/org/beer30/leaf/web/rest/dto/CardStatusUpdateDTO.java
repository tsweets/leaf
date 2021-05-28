package org.beer30.leaf.web.rest.dto;

import org.beer30.leaf.domain.enumeration.CardStatus;

public class CardStatusUpdateDTO {
    private String cardNumber;
    private CardStatus cardStatus;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public CardStatus getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(CardStatus cardStatus) {
        this.cardStatus = cardStatus;
    }

    @Override
    public String toString() {
        return "CardStatusUpdateDTO{" +
                "cardNumber='" + cardNumber + '\'' +
                ", cardStatus=" + cardStatus +
                '}';
    }
}
