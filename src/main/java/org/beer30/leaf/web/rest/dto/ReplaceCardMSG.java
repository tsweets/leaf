package org.beer30.leaf.web.rest.dto;


import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Card entity.
 */
public class ReplaceCardMSG implements Serializable {

    private Long id; // Request ID

    private Integer envId;

    private Long externalId; // Skyline ID   (Old Card)
    private Long newCardExternalId; // Skyline ID (New Card - Instant Issue)
    private String newCardCardnumber; // Skyline Number (New Card - Instant Issue)


    private Long cardholderExternalId; //Skyline ID

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
        ReplaceCardMSG that = (ReplaceCardMSG) o;
        return Objects.equals(id, that.id) && Objects.equals(envId, that.envId) && Objects.equals(externalId, that.externalId) && Objects.equals(cardholderExternalId, that.cardholderExternalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, envId, externalId, cardholderExternalId);
    }

    @Override
    public String toString() {
        return "ReplaceCardMSG{" +
                "id=" + id +
                ", envId=" + envId +
                ", externalId=" + externalId +
                ", newCardExternalId=" + newCardExternalId +
                ", newCardCardnumber=" + newCardCardnumber +
                ", cardholderExternalId=" + cardholderExternalId +
                '}';
    }
}
