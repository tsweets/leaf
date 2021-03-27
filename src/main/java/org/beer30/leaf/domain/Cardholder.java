package org.beer30.leaf.domain;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Cardholder.
 */
@Data
@Entity
@Table(name = "cardholder")
public class Cardholder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "ssn")
    private String ssn;

    @Column(name = "home_street1")
    private String homeStreet1;

    @Column(name = "home_street2")
    private String homeStreet2;

    @Column(name = "home_city")
    private String homeCity;

    @Column(name = "home_state")
    private String homeState;

    @Column(name = "home_postal_code")
    private String homePostalCode;

    @Column(name = "ship_street1")
    private String shipStreet1;

    @Column(name = "ship_street2")
    private String shipStreet2;

    @Column(name = "ship_city")
    private String shipCity;

    @Column(name = "ship_state")
    private String shipState;

    @Column(name = "ship_postal_code")
    private String shipPostalCode;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;


}
