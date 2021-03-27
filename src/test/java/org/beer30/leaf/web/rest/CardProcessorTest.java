package org.beer30.leaf.web.rest;


import org.beer30.leaf.LeafApplication;
import org.beer30.leaf.TestUtil;
import org.beer30.leaf.domain.CardAccount;
import org.beer30.leaf.service.CardProcessorService;
import org.beer30.leaf.service.TestService;
import org.beer30.leaf.web.rest.dto.CardAccountDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LeafApplication.class)
@AutoConfigureMockMvc
public class CardProcessorTest {

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
}
