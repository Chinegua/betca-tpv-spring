package es.upm.miw.repositories.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class ProviderRepositoryIT {

    @Autowired
    private ProviderRepository providerRepository;

    @Test
    public void testFindOne() {
        assertEquals("company-p1", this.providerRepository.findOne("provider1").getCompany());
    }

    @Test
    public void testFindMinimumProviderById() {
        assertEquals("provider1", this.providerRepository.findMinimumProviderById("provider1").getId());
        assertEquals("company-p1", this.providerRepository.findMinimumProviderById("provider1").getCompany());
    }

}
