package org.beer30.leaf.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beer30.leaf.LeafApplication;
import org.beer30.leaf.TestUtil;
import org.beer30.leaf.service.CardProcessorService;
import org.beer30.leaf.web.rest.dto.CardAccountDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = LeafApplication.class)
@AutoConfigureMockMvc
public class CardProcessorTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Value("${leaf.processor.prefix}")
    private String prefix;
    @Value("${leaf.processor.personlaized.extension}")
    private String extension;

    @Autowired
    CardProcessorService cardProcessorService;


    @Test
    public void getCardNumber() throws Exception {


        MvcResult result = mockMvc.perform(get("/api/v1/card/generate-number/{prefix}/{extension}", prefix, extension))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        Assert.assertNotNull(result);

             /*   .andExpect(jsonPath("$.id").value(card.getId().intValue()))
                .andExpect(jsonPath("$.envId").value(DEFAULT_ENV_ID))
                .andExpect(jsonPath("$.externalId").value(DEFAULT_EXTERNAL_ID.intValue()))
                .andExpect(jsonPath("$.cardNumber").value(DEFAULT_CARD_NUMBER))
                .andExpect(jsonPath("$.ddaAccountNumber").value(DEFAULT_DDA_ACCOUNT_NUMBER))
                .andExpect(jsonPath("$.cardStatus").value(DEFAULT_CARD_STATUS.toString()))
                .andExpect(jsonPath("$.imprintedName").value(DEFAULT_IMPRINTED_NAME))
                .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()));*/
    }

    @Test
    public void createCardholder() throws Exception {
        CardAccountDTO dto = TestUtil.generateFakeCardAccountDTO();
        dto.setCardNumber(cardProcessorService.generateCardNumber(prefix, extension));

        MvcResult result = mockMvc.perform(post("/api/v1/card")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto)))
                .andExpect(status().isCreated()).andReturn();

        Assert.assertNotNull(result);
        String resultString = result.getResponse().getContentAsString();
        CardAccountDTO resultAccount = objectMapper.readValue(resultString, CardAccountDTO.class);

        Assert.assertNotNull(resultAccount);
        String cardNumber = resultAccount.getCardNumber();
        Assert.assertEquals(dto.getCardNumber(), cardNumber);


        // Test Get Card by Number

        MvcResult resultCardLookup = mockMvc.perform(get("/api/v1/card/find-by-number/{cardNumber}", cardNumber))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        Assert.assertNotNull(resultCardLookup);

    }

}
