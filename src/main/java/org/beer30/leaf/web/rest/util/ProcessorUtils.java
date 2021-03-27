package org.beer30.leaf.web.rest.util;


import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author tsweets
 * Date: 12/24/15
 * Time: 12:57 PM
 */
public class ProcessorUtils {
    public static String TEST_BIN = "123123";


    public static String createTestCardNumber() {
        StringBuffer PREFIX = new StringBuffer(TEST_BIN);
        return PREFIX.append(randomNumberString(10)).toString();
        //return "5105105105105100";
    }

    public static String randomNumberString(int length) {
        char[] chars = "123456789".toCharArray();
        return RandomStringUtils.random(length, 0, 8, false, false, chars);
    }
}
