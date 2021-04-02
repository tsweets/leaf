package org.beer30.leaf.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.beer30.leaf.domain.CardAccount;
import org.beer30.leaf.domain.Transaction;
import org.beer30.leaf.domain.enumeration.CardStatus;
import org.beer30.leaf.domain.enumeration.TransactionCode;
import org.beer30.leaf.domain.util.CardProcessorUtils;
import org.beer30.leaf.repository.CardAccountRepository;
import org.beer30.leaf.repository.TransactionRepository;
import org.beer30.leaf.web.rest.dto.CardAccountDTO;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Transactional
@Service
public class CardProcessorService {

    @Autowired
    CardAccountRepository cardAccountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    CardProcessorUtils cardProcessorUtils;


    public String generateCardNumber(String prefix, String extension) {
        StringBuffer cardNumber = new StringBuffer(prefix);
        cardNumber.append(extension);
        // Need 7 more digits
        String cardRestDigits = RandomStringUtils.randomNumeric(7);
        cardNumber.append(cardRestDigits);

        log.info("Generated Card Number: {}", cardNumber);
        return cardNumber.toString();
    }

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
        cardAccount.setBalance(Money.zero(CurrencyUnit.USD));
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

        cardAccount.setCardNetwork(CardProcessorUtils.getCardNetwork(cardAccount.getCardNumber()));
        cardAccount.setCardStatus(CardStatus.PRE_ACTIVE);
        cardAccount.setCardType(cardProcessorUtils.getCardType(cardAccount.getCardNumber()));

        CardAccount savedAccount = cardAccountRepository.save(cardAccount);
        if (savedAccount != null) {
            log.info("Saved Card Account with ID: {}", savedAccount.getId());
        }

        return savedAccount;
    }

    public CardAccount prepaidAdjustment(String cardNumber, Money adjustmentAmount) {
        CardAccount cardAccount = this.accountInquiry(cardNumber);
        if (cardAccount != null) {
            log.info("Adjusting Card balance starting: {}  Adjustment amt: {}", cardAccount.getBalance(), adjustmentAmount);
            cardAccount.setBalance(cardAccount.getBalance().plus(adjustmentAmount));
            log.info("New Balance: {}", cardAccount.getBalance());
        }
        CardAccount savedAccount = cardAccountRepository.save(cardAccount);
        return savedAccount;
    }

    public List<Transaction> getTransactionHistory(String cardNumber) {
        CardAccount cardAccount = this.accountInquiry(cardNumber);
        List<Transaction> transactionList = new ArrayList<>();

        if (cardAccount != null) {
            log.info("Looking for Transactions for Card: {}", cardAccount.getId());
            transactionList = transactionRepository.findAllByCardId(cardAccount.getId());
        } else {
            return null;
        }

        return transactionList;
    }

    public Transaction purchase(String cardNumber, Money purchaseAmount, String description) {
        CardAccount cardAccount = this.accountInquiry(cardNumber);
        if (cardAccount == null) {
            return null;
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionCode(TransactionCode.ACH_REMOVE_FUNDS);
        transaction.setCardId(cardAccount.getId());
        transaction.setAmount(purchaseAmount);
        transaction.setDate(Instant.now());
        transaction.setNote(description);

        Transaction transactionSaved = transactionRepository.save(transaction);

        cardAccount.setBalance(cardAccount.getBalance().minus(purchaseAmount));
        cardAccountRepository.save(cardAccount);

        return transactionSaved;
    }
}
