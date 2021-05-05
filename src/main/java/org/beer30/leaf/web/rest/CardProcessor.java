package org.beer30.leaf.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.beer30.leaf.domain.CardAccount;
import org.beer30.leaf.service.CardProcessorService;
import org.beer30.leaf.web.rest.dto.CardAccountDTO;
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
        String cardNumber = cardProcessorService.generateCardNumber(prefix, extension);

        return Optional.ofNullable(cardNumber)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                //  .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
                .orElse(null);
    }

    @RequestMapping(value = "/v1/card",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardAccount> createCardAccount(@Valid @RequestBody CardAccountDTO cardAccountDTO) {
        log.debug("REST request to create card account: {}", cardAccountDTO);

        CardAccount cardAccount = cardProcessorService.createCardAccount(cardAccountDTO);

        return Optional.ofNullable(cardAccount)
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

}
