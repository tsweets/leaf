package org.beer30.leaf.web.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.beer30.leaf.domain.enumeration.CardStatus;
import org.beer30.leaf.service.CardService;
import org.beer30.leaf.service.CardholderService;
import org.beer30.leaf.web.rest.dto.AddCardMSG;
import org.beer30.leaf.web.rest.dto.CardDTO;
import org.beer30.leaf.web.rest.dto.CardholderDTO;
import org.beer30.leaf.web.rest.dto.ReplaceCardMSG;
import org.beer30.leaf.web.rest.util.HeaderUtil;
import org.beer30.leaf.web.rest.util.ProcessorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * @author tsweets
 * Date: 12/24/15
 * Time: 10:23 AM
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class CardProcessor {

    @Autowired
    CardService cardService;

    @Autowired
    CardholderService cardholderService;

    /**
     * POST /cardholders/addCard -> Adds a card to a cardholder
     */
    @RequestMapping(value = "/cardholders/addCard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardDTO> addCardToCardholder(@Valid @RequestBody AddCardMSG addCardMSG) throws URISyntaxException {
        log.debug("REST request to add Card to Cardholder : {}", addCardMSG);
        if (addCardMSG.getCardholderExternalId() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cardholder", "blankexternalid", "External ID cannot be blank")).body(null);
        }

        // Add Logic Here

        // Find Cardholder by External ID
        CardholderDTO foundCardholder = cardholderService.findOneByExternalId(addCardMSG.getEnvId(), addCardMSG.getCardholderExternalId());
        CardDTO cardDTO = createNewCardDTO(addCardMSG.getEnvId(), foundCardholder.getId(), foundCardholder.getFirstName() + " " + foundCardholder.getLastName(), addCardMSG.getNewCardCardnumber(), addCardMSG.getNewCardExternalId());


        CardDTO result = cardService.save(cardDTO);


        return ResponseEntity.created(new URI("/api/cards/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("card", result.getId().toString()))
                .body(result);
    }

    private CardDTO createNewCardDTO(int environmentId, long cardholderId, String cardholderName, String newCardNumber, Long newCardId) {
        CardDTO cardDTO = new CardDTO();
        cardDTO.setEnvId(environmentId);
        cardDTO.setCardStatus(CardStatus.PRE_ACTIVE);
        cardDTO.setBalance(BigDecimal.ZERO);
        cardDTO.setCardholderId(cardholderId);
        if (newCardNumber == null) {
            cardDTO.setCardNumber(ProcessorUtils.createTestCardNumber());
        } else {
            cardDTO.setCardNumber(newCardNumber);
        }

        if (newCardId != null) {
            cardDTO.setExternalId(newCardId);
        }

        cardDTO.setImprintedName(cardholderName.toUpperCase());
        return cardDTO;
    }

    @Operation(summary = "Replace a card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resulting card from the replacement ", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = CardDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Bad input data", content = @Content)})
    @RequestMapping(value = "/cards/replaceCard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardDTO> replaceCard(@Valid @RequestBody ReplaceCardMSG replaceCardMSG) throws URISyntaxException {
        log.debug("REST request to replace Card : {}", replaceCardMSG);
        if (replaceCardMSG.getExternalId() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("card", "invalidcardid", "External ID cannot be blank")).body(null);
        }

        CardDTO oldCard = cardService.findByExternalId(replaceCardMSG.getEnvId(), replaceCardMSG.getExternalId());
        if (oldCard == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("card", "invalidcardid", "Cannot Find Card")).body(null);
        }

        // Lookup Cardholder
        CardholderDTO foundCardholder = cardholderService.findOneByExternalId(replaceCardMSG.getEnvId(), replaceCardMSG.getCardholderExternalId());
        if (foundCardholder == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("cardholder", "invalidcardholderid", "Cannot Find Card Holder")).body(null);
        }
        CardDTO newCard = createNewCardDTO(replaceCardMSG.getEnvId(), foundCardholder.getId(), foundCardholder.getFirstName() + " " + foundCardholder.getLastName(), replaceCardMSG.getNewCardCardnumber(), replaceCardMSG.getNewCardExternalId());

        CardDTO addedCard = cardService.save(newCard);


        return ResponseEntity.created(new URI("/api/card/" + addedCard.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("card", addedCard.getId().toString()))
                .body(addedCard);
    }


    /**
     * GET  /cards/byExternalId/:id -> get the "id" card.
     */
    @RequestMapping(value = "/cards/byExternalId/{envId}/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardDTO> lookupCard(@PathVariable Integer envId, @PathVariable Long id) {
        log.debug("REST request to lookup Card by external ID: {} ENV: {}", id, envId);
        CardDTO cardDTO = cardService.findByExternalId(envId, id);
        return Optional.ofNullable(cardDTO)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                //  .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                .orElse(null);
    }

    /**
     * GET  /cardholders/byExternalId/:id -> get the "id" cardholder.
     */
    @RequestMapping(value = "/cardholders/byExternalId/{envId}/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardholderDTO> lookupCardhlder(@PathVariable Integer envId, @PathVariable Long id) {
        log.debug("REST request to lookup Card Holder by external ID: {} ENV: {}", id, envId);
        CardholderDTO foundCardholder = cardholderService.findOneByExternalId(envId, id);

        return Optional.ofNullable(foundCardholder)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                //  .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                .orElse(null);
    }


}
