package org.beer30.leaf.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.beer30.leaf.LeafApplication;
import org.beer30.leaf.TestUtil;
import org.beer30.leaf.domain.enumeration.CardStatus;
import org.beer30.leaf.domain.enumeration.CardType;
import org.beer30.leaf.service.CardProcessorService;
import org.beer30.leaf.web.rest.dto.CardAccountDTO;
import org.beer30.leaf.web.rest.dto.CardStatusUpdateDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    public void createInstantIssueCardAccountTest() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/instant-issue-card"))
                .andExpect(status().isCreated()).andReturn();

        Assert.assertNotNull(result);
        String resultString = result.getResponse().getContentAsString();
        CardAccountDTO resultAccount = objectMapper.readValue(resultString, CardAccountDTO.class);

        Assert.assertEquals(CardType.INSTANT_ISSUE, resultAccount.getCardType());
    }

    @Test
    public void createCardholder() throws Exception {
        CardAccountDTO dto = TestUtil.generateFakeCardAccountDTO(prefix, extension);

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

        // replace card
        // @RequestMapping(value = "/v1/card/replace",
        MvcResult replaceResult = mockMvc.perform(post("/api/v1/card/replace")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(cardNumber))
                .andExpect(status().isCreated()).andReturn();

        Assert.assertNotNull(replaceResult);
        String replaceResultString = replaceResult.getResponse().getContentAsString();
        CardAccountDTO replaceResultAccount = objectMapper.readValue(replaceResultString, CardAccountDTO.class);

        Assert.assertNotNull(replaceResultAccount);
        Assert.assertEquals(resultAccount.getDdaAccountNumber(), replaceResultAccount.getDdaAccountNumber());

        // Update status
        Assert.assertEquals(CardStatus.PRE_ACTIVE, replaceResultAccount.getCardStatus());
        CardStatusUpdateDTO cardStatusUpdateDTO = new CardStatusUpdateDTO();
        cardStatusUpdateDTO.setCardNumber(replaceResultAccount.getCardNumber());
        cardStatusUpdateDTO.setCardStatus(CardStatus.ACTIVE);

        MvcResult updateStatusResult = mockMvc.perform(post("/api/v1/card/update-status")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cardStatusUpdateDTO)))
                .andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(updateStatusResult);
        MvcResult updateResultCardLookup = mockMvc.perform(get("/api/v1/card/find-by-number/{cardNumber}", replaceResultAccount.getCardNumber()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andReturn();

        Assert.assertNotNull(updateResultCardLookup);
        String updateResultCardString = updateResultCardLookup.getResponse().getContentAsString();
        CardAccountDTO updateResultAccount = objectMapper.readValue(updateResultCardString, CardAccountDTO.class);
        Assert.assertNotNull(updateResultAccount);
        Assert.assertEquals(CardStatus.ACTIVE, updateResultAccount.getCardStatus());

        // Update Name
        String originalName = dto.getImprintedName();
        dto.setImprintedName(originalName + "-UPDATED");
        MvcResult updateNameResult = mockMvc.perform(put("/api/v1/card/")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dto)))
                .andExpect(status().isOk()).andReturn();

        Assert.assertNotNull(updateNameResult);
        String updateResultNameString = updateNameResult.getResponse().getContentAsString();
        CardAccountDTO updateNameAccount = objectMapper.readValue(updateResultNameString, CardAccountDTO.class);

        Assert.assertEquals(dto.getImprintedName(), updateNameAccount.getImprintedName());
    }


}
