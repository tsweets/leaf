package org.beer30.leaf.domain;


import lombok.Data;
import org.beer30.leaf.domain.enumeration.CardNetwork;
import org.beer30.leaf.domain.enumeration.CardStatus;
import org.beer30.leaf.domain.enumeration.CardType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "card_account")
public class CardAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "env_id")
    private Integer envId;

    // This is the Card's External ID
    @NotNull
    @Column(name = "external_id")
    private Long externalId;

    @NotNull
    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @NotNull
    @Column(name = "dda_account_number")
    private String ddaAccountNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "card_status", nullable = false)
    private CardStatus cardStatus;

    @NotNull
    @Size(max = 60)
    @Column(name = "imprinted_name", length = 60)
    private String imprintedName;

    @NotNull
    @Column(name = "balance", precision = 10, scale = 2)
    private BigDecimal balance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "card_network", nullable = false)
    private CardNetwork cardNetwork;

    @NotNull
    @Column(name = "dob")
    private LocalDate dob;

    @NotNull
    @Column(name = "ssn")
    private String ssn;

    @Column(name = "street1")
    private String street1;

    @Column(name = "street2")
    private String street2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

}
