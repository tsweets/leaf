package org.beer30.leaf.domain.util;

import org.beer30.leaf.LeafApplication;
import org.beer30.leaf.domain.enumeration.CardNetwork;
import org.beer30.leaf.domain.enumeration.CardType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeafApplication.class)
public class CardProcessorUtilsTest {
    //                     1-6 = Prefix
    //                     7-9 = Bin Ext
    //                     1234567890123456
    String VALID_MC_IIB = "5200000013081394";
    String VALID_VISA_IIB = "4700000016606508";
    String VALID_MC_PERSO = "5200000023081394";
    String VALID_VISA_PERSO = "4700000026606508";


    @Autowired
    CardProcessorUtils cardProcessorUtils;

    @Test
    public void getCardNetworkTest() {
        Assert.assertEquals(CardNetwork.MASTERCARD, CardProcessorUtils.getCardNetwork(VALID_MC_IIB));
        Assert.assertEquals(CardNetwork.VISA, CardProcessorUtils.getCardNetwork(VALID_VISA_IIB));
    }

    @Test
    public void getCardTypeTest() {
        Assert.assertEquals(CardType.INSTANT_ISSUE, cardProcessorUtils.getCardType(VALID_MC_IIB));
        Assert.assertEquals(CardType.INSTANT_ISSUE, cardProcessorUtils.getCardType(VALID_VISA_IIB));
        Assert.assertEquals(CardType.PERSONALIZED, cardProcessorUtils.getCardType(VALID_VISA_PERSO));
        Assert.assertEquals(CardType.PERSONALIZED, cardProcessorUtils.getCardType(VALID_MC_PERSO));

    }

}
