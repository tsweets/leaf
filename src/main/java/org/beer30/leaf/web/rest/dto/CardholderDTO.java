package org.beer30.leaf.web.rest.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


/**
 * A DTO for the Cardholder entity.
 */
public class CardholderDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer envId;

    @NotNull
    private Long externalId;

    @NotNull
    private String firstName;

    private String middleName;

    @NotNull
    private String lastName;

    private LocalDate dob;

    private String ssn;

    private String homeStreet1;

    private String homeStreet2;

    private String homeCity;

    private String homeState;

    private String homePostalCode;

    private String shipStreet1;

    private String shipStreet2;

    private String shipCity;

    private String shipState;

    private String shipPostalCode;

    private String phoneNumber;

    private String email;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public String getHomeStreet1() {
        return homeStreet1;
    }

    public void setHomeStreet1(String homeStreet1) {
        this.homeStreet1 = homeStreet1;
    }

    public String getHomeStreet2() {
        return homeStreet2;
    }

    public void setHomeStreet2(String homeStreet2) {
        this.homeStreet2 = homeStreet2;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public String getHomeState() {
        return homeState;
    }

    public void setHomeState(String homeState) {
        this.homeState = homeState;
    }

    public String getHomePostalCode() {
        return homePostalCode;
    }

    public void setHomePostalCode(String homePostalCode) {
        this.homePostalCode = homePostalCode;
    }

    public String getShipStreet1() {
        return shipStreet1;
    }

    public void setShipStreet1(String shipStreet1) {
        this.shipStreet1 = shipStreet1;
    }

    public String getShipStreet2() {
        return shipStreet2;
    }

    public void setShipStreet2(String shipStreet2) {
        this.shipStreet2 = shipStreet2;
    }

    public String getShipCity() {
        return shipCity;
    }

    public void setShipCity(String shipCity) {
        this.shipCity = shipCity;
    }

    public String getShipState() {
        return shipState;
    }

    public void setShipState(String shipState) {
        this.shipState = shipState;
    }

    public String getShipPostalCode() {
        return shipPostalCode;
    }

    public void setShipPostalCode(String shipPostalCode) {
        this.shipPostalCode = shipPostalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CardholderDTO cardholderDTO = (CardholderDTO) o;

        return Objects.equals(id, cardholderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CardholderDTO{" +
                "id=" + id +
                ", envId='" + envId + "'" +
                ", externalId='" + externalId + "'" +
                ", firstName='" + firstName + "'" +
                ", middleName='" + middleName + "'" +
                ", lastName='" + lastName + "'" +
                ", dob='" + dob + "'" +
                ", ssn='" + ssn + "'" +
                ", homeStreet1='" + homeStreet1 + "'" +
                ", homeStreet2='" + homeStreet2 + "'" +
                ", homeCity='" + homeCity + "'" +
                ", homeState='" + homeState + "'" +
                ", homePostalCode='" + homePostalCode + "'" +
                ", shipStreet1='" + shipStreet1 + "'" +
                ", shipStreet2='" + shipStreet2 + "'" +
                ", shipCity='" + shipCity + "'" +
                ", shipState='" + shipState + "'" +
                ", shipPostalCode='" + shipPostalCode + "'" +
                ", phoneNumber='" + phoneNumber + "'" +
                ", email='" + email + "'" +
                '}';
    }
}
