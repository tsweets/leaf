package org.beer30.leaf.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.beer30.leaf.domain.CardAccount;
import org.beer30.leaf.domain.Transaction;
import org.beer30.leaf.domain.util.CardProcessorUtils;
import org.beer30.leaf.service.CardProcessorService;
import org.beer30.leaf.web.rest.dto.CardAccountDTO;
import org.beer30.leaf.web.rest.dto.CardStatusUpdateDTO;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    CardProcessorService cardProcessorService;

    @RequestMapping(value = "/v1/card/generate-number/{prefix}/{extension}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> generateCardNumber(@PathVariable String prefix, @PathVariable String extension) {
        log.debug("REST request to generate card number: Prefix {}  Extension {}", prefix, extension);
        String cardNumber = CardProcessorUtils.generateCardNumber(prefix, extension);
        log.info("Generated Card Number: {}", cardNumber);

        return Optional.ofNullable(cardNumber)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                //  .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                .orElse(null);
    }

    @RequestMapping(value = "/v1/card",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardAccount> updateCardAccount(@Valid @RequestBody CardAccountDTO cardAccountDTO) {
        log.debug("REST request to update card account: {}", cardAccountDTO);

        CardAccount cardAccount = cardProcessorService.updateCardAccount(cardAccountDTO);

        return Optional.ofNullable(cardAccount)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }


    @RequestMapping(value = "/v1/card",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardAccount> createCardAccount(@RequestBody CardAccountDTO cardAccountDTO) {
        log.debug("REST request to create card account: {}", cardAccountDTO);

        CardAccount cardAccount = cardProcessorService.createCardAccount(cardAccountDTO);

        return Optional.ofNullable(cardAccount)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @RequestMapping(value = "/v1/virtual-card",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardAccount> createVirtualCardAccount(@RequestBody CardAccountDTO cardAccountDTO) {
        log.debug("REST request to create virtual card account: {}", cardAccountDTO);

        CardAccount cardAccount = cardProcessorService.createVirtualCardAccount(cardAccountDTO);

        return Optional.ofNullable(cardAccount)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    @RequestMapping(value = "/v1/instant-issue-card",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardAccount> createInstantIssueCardAccount() {
        log.debug("REST request to create instant issue card account: {}");

        CardAccount cardAccount = cardProcessorService.createInstantIssueCardAccount();

        return Optional.ofNullable(cardAccount)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }


    // MvcResult updateStatusResult = mockMvc.perform(post("/api/v1/card/update-status")
    @RequestMapping(value = "/v1/card/update-status",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardAccount> updateCardAccountStatus(@Valid @RequestBody CardStatusUpdateDTO dto) {
        log.debug("REST request to update card account status: {}", dto);

        CardAccount card = cardProcessorService.accountInquiry(dto.getCardNumber());
        if (card == null) {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        CardAccount updatedCard = cardProcessorService.updateCardStatus(card.getCardNumber(), dto.getCardStatus());

        return Optional.ofNullable(updatedCard)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }


    @RequestMapping(value = "/v1/card/replace",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardAccount> replaceCardAccount(@Valid @RequestBody String cardNumber) {
        log.debug("REST request to replace card account: {}", cardNumber);

        CardAccount oldCard = cardProcessorService.accountInquiry(cardNumber);
        CardAccount replacementAccount = cardProcessorService.replaceCard(oldCard);

        return Optional.ofNullable(replacementAccount)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }


    @RequestMapping(value = "/v1/card/find-by-number/{cardNumber}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardAccount> accountInquiry(@PathVariable String cardNumber) {
        log.debug("REST request Account Inquiry: {}", cardNumber);

        CardAccount cardAccount = cardProcessorService.accountInquiry(cardNumber);

        return Optional.ofNullable(cardAccount)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

    //TODO should return Transaction instead
    @RequestMapping(value = "/v1/card/load/{cardNumber}",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardAccount> load(@Valid @RequestBody Money amount, @PathVariable String cardNumber) {
        log.debug("REST request to load card account: {} - with {}", cardNumber, amount);
        CardAccount cardAccount = cardProcessorService.accountInquiry(cardNumber);
        Transaction transaction
                = cardProcessorService.load(cardNumber, amount, "lead Load Transaction");

        return Optional.ofNullable(cardAccount)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

    }

}
