package org.beer30.leaf.service;


import org.beer30.leaf.LeafApplication;
import org.beer30.leaf.TestUtil;
import org.beer30.leaf.domain.CardAccount;
import org.beer30.leaf.domain.Transaction;
import org.beer30.leaf.domain.enumeration.CardStatus;
import org.beer30.leaf.domain.enumeration.CardType;
import org.beer30.leaf.domain.util.CardProcessorUtils;
import org.beer30.leaf.repository.CardAccountRepository;
import org.beer30.leaf.web.rest.dto.CardAccountDTO;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeafApplication.class)
public class CardProcessorServiceTest {

    @Autowired
    TestService testService;

    @Autowired
    CardProcessorService cardProcessorService;

    @Autowired
    CardAccountRepository cardAccountRepository;

    @Value("${leaf.processor.prefix}")
    private String prefix;
    @Value("${leaf.processor.personlaized.extension}")
    private String extension;

    CardAccount cardAccount;

    @Before
    public void setup() {
        String cardNumber = CardProcessorUtils.generateCardNumber(prefix, extension);
        cardAccount = testService.createCardAccount(cardNumber);
    }

    @Test
    public void testUpdateStatus() {
        String cardNumber = CardProcessorUtils.generateCardNumber(prefix, extension);
        cardAccount = testService.createCardAccount(cardNumber);

        Assert.assertEquals(CardStatus.PRE_ACTIVE, cardAccount.getCardStatus());

        cardProcessorService.updateCardStatus(cardNumber, CardStatus.PURGED);
        CardAccount cardLookup = cardProcessorService.accountInquiry(cardNumber);
        Assert.assertEquals(CardStatus.PURGED, cardLookup.getCardStatus());


    }

    @Test
    public void replaceCard() {
        // given a card number
        String oldCardNumber = CardProcessorUtils.generateCardNumber(prefix, extension);
        CardAccount oldCard = testService.createCardAccount(oldCardNumber);
        Assert.assertEquals(oldCard.getCardStatus(), CardStatus.PRE_ACTIVE);

        CardAccount replacementCard = cardProcessorService.replaceCard(oldCard);
        Assert.assertNotNull(replacementCard);
        Assert.assertEquals(oldCard.getImprintedName(), replacementCard.getImprintedName());
    }


    @Test
    public void generateCardNumber() {
        String cardNumber = CardProcessorUtils.generateCardNumber("123456", "012");
        Assert.assertNotNull(cardNumber);
        Assert.assertEquals(16, cardNumber.length());
    }

    @Test
    public void createCardAccountTest() {
        CardAccountDTO dto = TestUtil.generateFakeCardAccountDTO(prefix, extension);

        CardAccount cardAccount = cardProcessorService.createCardAccount(dto);
        Assert.assertNotNull(cardAccount);
        Assert.assertTrue(cardAccount.getId() > 0);
        Assert.assertEquals(CardType.PERSONALIZED, cardAccount.getCardType());
    }

    @Test
    public void updateCardAccountTest() {
        CardAccountDTO original = TestUtil.generateFakeCardAccountDTO(prefix, extension);

        CardAccount cardAccount = cardProcessorService.createCardAccount(original);
        Assert.assertNotNull(cardAccount);
        Assert.assertTrue(cardAccount.getId() > 0);
        Assert.assertEquals(CardType.PERSONALIZED, cardAccount.getCardType());
        String cardNumber = cardAccount.getCardNumber();

        // Name Update
        String updatedName = original.getImprintedName() + "-UPDATED";

        // Address Update
        String updatedStreet1 = original.getStreet1() + "-UPDATED";
        String updatedStreet2 = original.getStreet2() + "-UPDATED";
        String updatedCity = original.getCity() + "-UPDATED";
        String updatedState = original.getState() + "-UPDATED";
        String updatedZip = original.getPostalCode() + "-UPDATED";

        // Census Info
        String updatedEmail = original.getEmail() + "-UPDATED";
        String updatedPhone = "5551230000";
        String updatedSSN = "010-10-1010";
        LocalDate updatedDOB = original.getDob().plusYears(10);

        CardAccountDTO updatedCardAccount = new CardAccountDTO();
        updatedCardAccount.setCardNumber(cardNumber);
        updatedCardAccount.setImprintedName(updatedName);
        updatedCardAccount.setStreet1(updatedStreet1);
        updatedCardAccount.setStreet2(updatedStreet2);
        updatedCardAccount.setCity(updatedCity);
        updatedCardAccount.setState(updatedState);
        updatedCardAccount.setPostalCode(updatedZip);
        updatedCardAccount.setEmail(updatedEmail);
        updatedCardAccount.setPhoneNumber(updatedPhone);
        updatedCardAccount.setSsn(updatedSSN);
        updatedCardAccount.setDob(updatedDOB);
        updatedCardAccount.setExternalId(original.getExternalId());

        CardAccount cardAccountSaved = cardProcessorService.updateCardAccount(updatedCardAccount);
        Assert.assertNotNull(cardAccountSaved);

        CardAccount updated = cardProcessorService.accountInquiry(cardNumber);
        Assert.assertEquals(cardNumber, updated.getCardNumber());
        Assert.assertEquals(updatedName, updated.getImprintedName());
        Assert.assertEquals(updatedStreet1, updated.getStreet1());
        Assert.assertEquals(updatedStreet2, updated.getStreet2());
        Assert.assertEquals(updatedCity, updated.getCity());
        Assert.assertEquals(updatedState, updated.getState());
        Assert.assertEquals(updatedZip, updated.getPostalCode());
        Assert.assertEquals(updatedEmail, updated.getEmail());
        Assert.assertEquals(updatedPhone, updated.getPhoneNumber());
        Assert.assertEquals(updatedSSN, updated.getSsn());
        Assert.assertEquals(updatedDOB, updated.getDob());

    }

    @Test
    public void createInstantIssueCardAccountTest() {
        CardAccount cardAccount = cardProcessorService.createInstantIssueCardAccount();
        Assert.assertNotNull(cardAccount);
        Assert.assertTrue(cardAccount.getId() > 0);
        Assert.assertEquals(CardType.INSTANT_ISSUE, cardAccount.getCardType());
    }

    //Account Inquiry (Given Card Number return Card Account)
    @Test
    public void testAccountInquiry() {
        CardAccount cardAccountFound = cardProcessorService.accountInquiry(cardAccount.getCardNumber());
        Assert.assertNotNull(cardAccountFound);
        Assert.assertEquals(cardAccount, cardAccountFound);
    }

    //Prepaid Adjustment (CardNumber, Amount - From PM Acct - Used for Fee)
    @Test
    public void testPrepaidAdjustment() {
        Money startingBlance = cardAccount.getBalance();
        cardProcessorService.prepaidAdjustment(cardAccount.getCardNumber(), Money.of(CurrencyUnit.USD, 10.00));
        Assert.assertEquals(startingBlance.plus(10.00), cardProcessorService.accountInquiry(cardAccount.getCardNumber()).getBalance());

        cardProcessorService.prepaidAdjustment(cardAccount.getCardNumber(), Money.of(CurrencyUnit.USD, -10.00));
        Assert.assertEquals(startingBlance, cardProcessorService.accountInquiry(cardAccount.getCardNumber()).getBalance());
    }

    // Tx History Detail
    @Test
    public void testTxDetail() {
        Money PURCHASE_AMT = Money.of(CurrencyUnit.USD, 50.11);
        Money startingBalance = cardProcessorService.accountInquiry(cardAccount.getCardNumber()).getBalance();
        List<Transaction> transactionList = cardProcessorService.getTransactionHistory(cardAccount.getCardNumber());
        Assert.assertNotNull(transactionList);

        // Add a transaction
        Transaction transaction = cardProcessorService.purchase(cardAccount.getCardNumber(), PURCHASE_AMT, "Some Purchase");
        Assert.assertNotNull(transaction);

        // Check Balance
        Money newBalance = cardProcessorService.accountInquiry(cardAccount.getCardNumber()).getBalance();
        Assert.assertEquals(startingBalance.minus(PURCHASE_AMT), newBalance);

        // Get Transaction Details
        List<Transaction> transactionListUpdated = cardProcessorService.getTransactionHistory(cardAccount.getCardNumber());
        Assert.assertNotNull(transactionListUpdated);
        Assert.assertTrue(transactionListUpdated.size() >= 1);

        // Find our Transaction
        Boolean found = Boolean.FALSE;
        for (Transaction transactionFound : transactionListUpdated) {
            if (transactionFound.getId() == transaction.getId()) {
                found = true;
            }
        }

        Assert.assertTrue(found);

    }
    //Card to Card Transfer (Source CN, Dest CN, Amount)
   /* @Test
    public void testCardToCardTr*/
}
