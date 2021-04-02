package org.beer30.leaf.domain.util;

import org.apache.commons.lang3.StringUtils;
import org.beer30.leaf.domain.enumeration.CardNetwork;
import org.beer30.leaf.domain.enumeration.CardType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CardProcessorUtils {

    @Value("${leaf.processor.instant-issue.extension}")
    String IIB_BIN_EXT;
    @Value("${leaf.processor.personlaized.extension}")
    String PERSO_BIN_EXT;

    static public CardNetwork getCardNetwork(String cardNumber) {
        assert (cardNumber.length() == 16) : "Not a valid card number";
        String firstNumber = StringUtils.left(cardNumber, 1);
        if (firstNumber.equalsIgnoreCase("4")) {
            return CardNetwork.VISA;
        }

        if (firstNumber.equalsIgnoreCase("5")) {
            return CardNetwork.MASTERCARD;
        }

        return CardNetwork.UNKNOWN;
    }

    public CardType getCardType(String cardNumber) {
        assert (cardNumber.length() == 16) : "Not a valid card number";
        String binExt = StringUtils.substring(cardNumber, 6, 9);

        if (binExt.equalsIgnoreCase(IIB_BIN_EXT)) {
            return CardType.INSTANT_ISSUE;
        }

        if (binExt.equalsIgnoreCase(PERSO_BIN_EXT)) {
            return CardType.PERSONALIZED;
        }

        return null;
    }
}
