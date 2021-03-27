package org.beer30.leaf.domain;

import lombok.Data;
import org.beer30.leaf.domain.enumeration.CardNetwork;
import org.beer30.leaf.domain.enumeration.CardStatus;
import org.beer30.leaf.domain.enumeration.CardType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A Card.
 */
@Data
@Entity
@Table(name = "card")
public class Card implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "env_id")
    private Integer envId;

    @Column(name = "external_id")
    private Long externalId;

    @NotNull
    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "dda_account_number")
    private String ddaAccountNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "card_status", nullable = false)
    private CardStatus cardStatus;

    @Size(max = 60)
    @Column(name = "imprinted_name", length = 60)
    private String imprintedName;

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

    @ManyToOne
    @JoinColumn(name = "cardholder_id")
    private Cardholder cardholder;


}
