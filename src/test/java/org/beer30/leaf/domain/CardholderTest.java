package org.beer30.leaf.domain;

import org.beer30.leaf.repository.CardholderRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
class CardholderTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CardholderRepository cardholderRepository;

    @Test
    public void whenFindById_thenReturnCardholder() {
        // given
        Cardholder testCardholder = new Cardholder();
        testCardholder.setEnvId(1);
        testCardholder.setFirstName("Test");
        testCardholder.setLastName("Cardholder");
        testCardholder.setExternalId(1234567l);

        Cardholder savedCardholder = entityManager.persist(testCardholder);
        Assert.assertNotNull(savedCardholder);
        entityManager.flush();


        // when
        Cardholder found = cardholderRepository.findById(savedCardholder.getId()).orElse(null);
        Assert.assertNotNull(found);

        // then
/*
        assertThat(found.getName())
                .isEqualTo(alex.getName());
*/
    }

}
