package org.beer30.leaf.web.rest.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Card entity.
 */
public class AddCardMSG implements Serializable {

    private Long id; // Request ID

    private Integer envId;

    private Long newCardExternalId; // Skyline ID (New Card - Instant Issue)
    private String newCardCardnumber; // Skyline Number (New Card - Instant Issue)

    private Long cardholderExternalId; //Skyline ID

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

    public Long getNewCardExternalId() {
        return newCardExternalId;
    }

    public void setNewCardExternalId(Long newCardExternalId) {
        this.newCardExternalId = newCardExternalId;
    }

    public String getNewCardCardnumber() {
        return newCardCardnumber;
    }

    public void setNewCardCardnumber(String newCardCardnumber) {
        this.newCardCardnumber = newCardCardnumber;
    }

    public Long getCardholderExternalId() {
        return cardholderExternalId;
    }

    public void setCardholderExternalId(Long cardholderExternalId) {
        this.cardholderExternalId = cardholderExternalId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddCardMSG that = (AddCardMSG) o;
        return Objects.equals(id, that.id) && Objects.equals(envId, that.envId) && Objects.equals(newCardExternalId, that.newCardExternalId) && Objects.equals(newCardCardnumber, that.newCardCardnumber) && Objects.equals(cardholderExternalId, that.cardholderExternalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, envId, newCardExternalId, newCardCardnumber, cardholderExternalId);
    }

    @Override
    public String toString() {
        return "AddCardMSG{" +
                "id=" + id +
                ", envId=" + envId +
                ", newCardExternalId=" + newCardExternalId +
                ", newCardCardnumber='" + newCardCardnumber + '\'' +
                ", cardholderExternalId=" + cardholderExternalId +
                '}';
    }
}
