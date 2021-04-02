package org.beer30.leaf.service;


import org.beer30.leaf.LeafApplication;
import org.beer30.leaf.TestUtil;
import org.beer30.leaf.domain.CardAccount;
import org.beer30.leaf.domain.Transaction;
import org.beer30.leaf.web.rest.dto.CardAccountDTO;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeafApplication.class)
public class CardProcessorServiceTest {

    @Autowired
    TestService testService;

    @Autowired
    CardProcessorService cardProcessorService;

    CardAccount cardAccount;

    @Before
    public void setup() {
        cardAccount = testService.createCardAccount();
    }

    @Test
    public void generateCardNumber() {
        String cardNumber = cardProcessorService.generateCardNumber("123456", "012");
        Assert.assertNotNull(cardNumber);
        Assert.assertEquals(16, cardNumber.length());
    }

    @Test
    public void createCardAccountTest() {
        CardAccountDTO dto = TestUtil.generateFakeCardAccountDTO();

        CardAccount cardAccount = cardProcessorService.createCardAccount(dto);
        Assert.assertNotNull(cardAccount);
        Assert.assertTrue(cardAccount.getId() > 0);
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
