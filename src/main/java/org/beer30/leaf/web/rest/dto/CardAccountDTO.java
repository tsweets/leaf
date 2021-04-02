package org.beer30.leaf.web.rest.dto;

import lombok.Data;
import org.beer30.leaf.domain.enumeration.CardNetwork;
import org.beer30.leaf.domain.enumeration.CardStatus;
import org.beer30.leaf.domain.enumeration.CardType;
import org.joda.money.Money;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class CardAccountDTO {
    private Long id;
    private Integer envId;

    @NotNull
    private Long externalId;

    @NotNull
    private String cardNumber;

    @NotNull
    private String ddaAccountNumber;

    private CardStatus cardStatus;

    @Size(max = 60)
    private String imprintedName;

    private Money balance;
    private CardType cardType;
    private CardNetwork cardNetwork;

    @NotNull
    private LocalDate dob;

    @NotNull
    private String ssn;

    @NotNull
    private String street1;

    private String street2;

    @NotNull
    private String city;

    @NotNull
    private String state;

    @NotNull
    private String postalCode;

    private String phoneNumber;
    private String email;

}
