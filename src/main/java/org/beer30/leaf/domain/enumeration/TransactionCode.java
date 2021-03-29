package org.beer30.leaf.domain.enumeration;

/**
 * @author tsweets
 * Date: 3/28/21 - 2:23 PM
 */

public enum TransactionCode {
    ACH_ADD_FUNDS("0114"), BATCH_ADD_FUNDS("0132"), ACH_REMOVE_FUNDS("0140"), CARD_TO_CARD("0159");


    final String value;

    TransactionCode(String value) {
        this.value = value;
    }

    public static TransactionCode findByCode(String code) {
        for (TransactionCode txCode : values()) {
            if (txCode.name().equalsIgnoreCase(code)) {
                return txCode;
            }
        }
        return null;
    }

    public static TransactionCode findByDescription(String name) {
        for (TransactionCode txCode : values()) {
            if (txCode.value.equalsIgnoreCase(name)) {
                return txCode;
            }
        }
        return null;
    }

    public String getDescription() {
        return this.value;
    }

    public String getName() {
        return this.name();
    }
}
