package org.beer30.leaf.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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


    public CardAccount closeCard(CardAccount cardAccount) {
        log.info("Request to Close Card: {} ", cardAccount);
        cardAccount.setCardStatus(CardStatus.CLOSED);
        CardAccount savedCard = cardAccountRepository.save(cardAccount);

        return savedCard;
    }

    public CardAccount replaceCard(CardAccount oldCard) {
        log.info("Request to replace card: {}", oldCard);
        // Start Replace Card
        // Change state of old card
        oldCard = this.closeCard(oldCard);

        String prefix = StringUtils.left(oldCard.getCardNumber(), 6);
        String extension = StringUtils.substring(oldCard.getCardNumber(), 6, 9);


        // return new card
        String newCardNumber = CardProcessorUtils.generateCardNumber(prefix, extension);
        log.info("Generated New Card Number: {}", newCardNumber);
        CardAccountDTO dto = new CardAccountDTO();
        dto.setEnvId(1);
        dto.setExternalId(oldCard.getExternalId());
        dto.setCardNumber(newCardNumber);
        dto.setDdaAccountNumber(oldCard.getDdaAccountNumber());
        dto.setCardStatus(CardStatus.PRE_ACTIVE);
        dto.setImprintedName(oldCard.getImprintedName());
        dto.setCardType(oldCard.getCardType());
        dto.setCardNetwork(oldCard.getCardNetwork());
        dto.setDob(oldCard.getDob());
        dto.setSsn(oldCard.getSsn());
        dto.setStreet1(oldCard.getStreet1());
        dto.setStreet2(oldCard.getStreet2());
        dto.setCity(oldCard.getCity());
        dto.setState(oldCard.getState());
        dto.setPostalCode(oldCard.getPostalCode());
        dto.setPhoneNumber(oldCard.getPhoneNumber());
        dto.setEmail(oldCard.getEmail());
        CardAccount replacementCard = this.createCardAccount(dto);


        // Move Balance
        Money balance = oldCard.getBalance();
        oldCard.setBalance(Money.zero(CurrencyUnit.USD));
        replacementCard.setBalance(balance);
        cardAccountRepository.save(oldCard);
        replacementCard = cardAccountRepository.save(replacementCard);

        log.info("Created Replacement Card: {}", replacementCard);
        return replacementCard;
    }



    //Account Inquiry (Given Card Number return Card Account)
    public CardAccount accountInquiry(String cardNumber) {
        log.info("Card Account Inquiry for: {}", cardNumber);
        assert (cardNumber.length() == 16) : "CardNumber should be 16 digits long";

        CardAccount cardAccount = cardAccountRepository.findCardAccountByCardNumber(cardNumber);
        log.info("Found Card Account: {}", cardAccount);

        return cardAccount;
    }

    public CardAccount createInstantIssueCardAccount() {
        log.info("Creating Instant Issue Card Account: {}");

        // Generate New Number
        String cardNumber = CardProcessorUtils.generateCardNumber(cardProcessorUtils.getPrefix(), cardProcessorUtils.getInstantIssueExtension());
        log.info("Generated Card Number: {}", cardNumber);
        CardAccount cardAccount = new CardAccount();
        cardAccount.setCardNumber(cardNumber);
        cardAccount.setDdaAccountNumber(cardProcessorUtils.generateDDA(cardNumber));
        cardAccount.setCardStatus(CardStatus.PRE_ACTIVE);
        cardAccount.setImprintedName("INSTANT ISSUE BRANDED");
        cardAccount.setBalance(Money.zero(CurrencyUnit.USD));
        cardAccount.setDob(java.time.LocalDate.of(0, 1, 1));
        cardAccount.setSsn("000000000");
        cardAccount.setExternalId(0l); // Probably should be null but I have a constraint

        cardAccount.setCardNetwork(CardProcessorUtils.getCardNetwork(cardAccount.getCardNumber()));
        cardAccount.setCardType(cardProcessorUtils.getCardType(cardAccount.getCardNumber()));

        CardAccount savedAccount = cardAccountRepository.save(cardAccount);
        if (savedAccount != null) {
            log.info("Saved Card Account with ID: {}", savedAccount.getId());
        }

        return savedAccount;
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

    public CardAccount updateCardStatus(String cardNumber, CardStatus newStatus) {
        log.info("Update Card Status for: {}  - to {}", cardNumber, newStatus);
        CardAccount cardAccount = this.accountInquiry(cardNumber);

        cardAccount.setCardStatus(newStatus);
        return cardAccountRepository.save(cardAccount);
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
