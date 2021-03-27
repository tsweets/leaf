package org.beer30.leaf.service;

import lombok.extern.slf4j.Slf4j;
import org.beer30.leaf.TestUtil;
import org.beer30.leaf.domain.CardAccount;
import org.beer30.leaf.web.rest.dto.CardAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestService {

    @Autowired
    CardProcessorService cardProcessorService;

    public CardAccount createCardAccount() {
        CardAccountDTO dto = TestUtil.generateFakeCardAccountDTO();
        CardAccount cardAccount = cardProcessorService.createCardAccount(dto);
        log.info("Created Fake Account: ID {} - Card Number {}", cardAccount.getId(), cardAccount.getCardNumber());

        return cardAccount;
    }
}
