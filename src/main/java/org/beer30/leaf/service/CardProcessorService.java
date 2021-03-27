package org.beer30.leaf.service;

import lombok.extern.slf4j.Slf4j;
import org.beer30.leaf.domain.CardAccount;
import org.beer30.leaf.repository.CardAccountRepository;
import org.beer30.leaf.web.rest.dto.CardAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;

@Slf4j
@Transactional
@Service
public class CardProcessorService {

    @Autowired
    CardAccountRepository cardAccountRepository;

    //Account Inquiry (Given Card Number return Card Account)
    public CardAccount accountInquiry(String cardNumber) {
        log.info("Card Account Inquiry for: {}", cardNumber);
        assert (cardNumber.length() == 16) : "CardNumber should be 16 digits long";

        CardAccount cardAccount = cardAccountRepository.findCardAccountByCardNumber(cardNumber);
        log.info("Found Card Account: {}", cardAccount);

        return cardAccount;
    }

    // Add a Cardholder
    public CardAccount createCardAccount(CardAccountDTO dto) {
        log.info("Creating Card Account: {}", dto);
        CardAccount cardAccount = new CardAccount();
        cardAccount.setEnvId(dto.getEnvId());
        cardAccount.setExternalId(dto.getExternalId());
        cardAccount.setCardNumber(dto.getCardNumber());
        cardAccount.setDdaAccountNumber(dto.getDdaAccountNumber());
        cardAccount.setCardStatus(dto.getCardStatus());
        cardAccount.setImprintedName(dto.getImprintedName());
        cardAccount.setBalance(new BigDecimal(BigInteger.ZERO, 2));
        cardAccount.setCardType(dto.getCardType());
        cardAccount.setCardNetwork(dto.getCardNetwork());
        cardAccount.setDob(dto.getDob());
        cardAccount.setSsn(dto.getSsn());
        cardAccount.setStreet1(dto.getStreet1());
        cardAccount.setStreet2(dto.getStreet2());
        cardAccount.setCity(dto.getCity());
        cardAccount.setState(dto.getState());
        cardAccount.setPostalCode(dto.getPostalCode());
        cardAccount.setPhoneNumber(dto.getPhoneNumber());
        cardAccount.setEmail(dto.getPhoneNumber());

        CardAccount savedAccount = cardAccountRepository.save(cardAccount);
        if (savedAccount != null) {
            log.info("Saved Card Account with ID: {}", savedAccount.getId());
        }

        return savedAccount;
    }
}
