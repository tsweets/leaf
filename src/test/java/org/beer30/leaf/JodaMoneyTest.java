package org.beer30.leaf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jodamoney.JodaMoneyModule;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author tsweets
 * Date: 3/28/21 - 4:56 PM
 */

public class JodaMoneyTest {

    @Test
    public void deserializeTest() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        /*SimpleModule module = new SimpleModule("JodaMoneySerializer", new Version(1, 0, 0, null, null, null));
        module.addSerializer(Money.class, new MoneySerializer());
        module.addDeserializer(Money.class, new MoneyDeserializer());
        objectMapper.registerModule(module);
*/
        objectMapper.registerModule(new JodaMoneyModule());
        // Basic Test
        String oneDollar = "USD 1.00";
        Money oneDollarMoney = Money.of(CurrencyUnit.USD, 1.00);
        Assert.assertEquals(oneDollarMoney, Money.parse(oneDollar));
        //  String oneDollarJson = "{\"amount\":\"USD 1.00\"}";
        String oneDollarJson = "{\"amount\":1.00,\"currency\":\"USD\"}";


        String bigDecimalString = objectMapper.writeValueAsString(new BigDecimal(new BigInteger("1"), -7));
        System.out.println(bigDecimalString);
        Assert.assertEquals("1E+7", bigDecimalString);

        // Object Mapper Test
        String dollarAsString = objectMapper.writeValueAsString(oneDollarMoney);
        System.out.println(dollarAsString);
        Assert.assertEquals(oneDollarJson, dollarAsString);

        Money moneyRead = objectMapper.readValue(oneDollarJson, Money.class);
        Assert.assertEquals(oneDollarMoney, moneyRead);


    }
}
