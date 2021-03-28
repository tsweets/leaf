package org.beer30.leaf.web.rest.dto;

import lombok.Data;
import org.beer30.leaf.domain.enumeration.CardNetwork;
import org.beer30.leaf.domain.enumeration.CardStatus;
import org.beer30.leaf.domain.enumeration.CardType;
import org.joda.money.Money;
import org.springdoc.core.converters.models.MonetaryAmount;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CardAccountDTO {

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

    private Money balance;

    private CardType cardType;

    private CardNetwork cardNetwork;

    private LocalDate dob;

    private String ssn;

    private String street1;

    private String street2;

    private String city;

    private String state;

    private String postalCode;

    private String phoneNumber;

    private String email;

}
