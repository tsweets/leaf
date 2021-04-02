package org.beer30.leaf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jodamoney.JodaMoneyModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.javafaker.Faker;
import org.beer30.leaf.domain.enumeration.CardNetwork;
import org.beer30.leaf.domain.enumeration.CardStatus;
import org.beer30.leaf.domain.enumeration.CardType;
import org.beer30.leaf.domain.util.JSR310DateTimeSerializer;
import org.beer30.leaf.domain.util.JSR310LocalDateDeserializer;
import org.beer30.leaf.web.rest.dto.CardAccountDTO;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.*;

/**
 * Utility class for testing REST controllers.
 */
public class TestUtil {

    /**
     * MediaType for JSON UTF8
     */
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

    /**
     * Convert an object to JSON byte array.
     *
     * @param object the object to convert
     * @return the JSON byte array
     * @throws IOException
     */
    public static byte[] convertObjectToJsonBytes(Object object)
            throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(OffsetDateTime.class, JSR310DateTimeSerializer.INSTANCE);
        module.addSerializer(ZonedDateTime.class, JSR310DateTimeSerializer.INSTANCE);
        module.addSerializer(LocalDateTime.class, JSR310DateTimeSerializer.INSTANCE);
        module.addSerializer(Instant.class, JSR310DateTimeSerializer.INSTANCE);
        module.addDeserializer(LocalDate.class, JSR310LocalDateDeserializer.INSTANCE);
        mapper.registerModule(module);

        mapper.registerModule(new JodaMoneyModule());
        byte[] valueAsBytes = mapper.writeValueAsBytes(object);
        System.out.println("Raw JSON: " + mapper.writeValueAsString(object));
        return valueAsBytes;
    }

    /**
     * Create a byte array with a specific size filled with specified data.
     *
     * @param size the size of the byte array
     * @param data the data to put in the byte array
     */
    public static byte[] createByteArray(int size, String data) {
        byte[] byteArray = new byte[size];
        for (int i = 0; i < size; i++) {
            byteArray[i] = Byte.parseByte(data, 2);
        }
        return byteArray;
    }

    public static CardAccountDTO generateFakeCardAccountDTO() {
        Faker faker = new Faker();
        CardAccountDTO dto = new CardAccountDTO();
        dto.setEnvId(1);
        dto.setExternalId(faker.number().randomNumber(10, true));
        dto.setCardNumber(faker.number().digits(16));
        dto.setDdaAccountNumber(faker.number().digits(10));
        dto.setCardStatus(CardStatus.PRE_ACTIVE);
        dto.setImprintedName(faker.name().fullName());
        dto.setCardType(CardType.PERSONALIZED);
        dto.setCardNetwork(CardNetwork.VISA);
        dto.setDob(LocalDate.ofInstant(faker.date().birthday(18, 99).toInstant(), ZoneId.systemDefault()));
        dto.setSsn(faker.idNumber().ssnValid());
        dto.setStreet1(faker.address().streetAddress());
        dto.setCity(faker.address().city());
        dto.setState(faker.address().state());
        dto.setPostalCode(faker.address().zipCode());
        dto.setPhoneNumber(faker.phoneNumber().phoneNumber());
        dto.setEmail(faker.internet().emailAddress());

        return dto;
    }
}
