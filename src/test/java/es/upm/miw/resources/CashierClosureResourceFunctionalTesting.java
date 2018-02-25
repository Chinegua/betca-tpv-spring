package es.upm.miw.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import es.upm.miw.dtos.CashierClosureInputDto;
import es.upm.miw.dtos.CashierClosureLastOutputDto;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class CashierClosureResourceFunctionalTesting {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private RestService restService;

    private void createCashier() {
        restService.loginAdmin().restBuilder().path(CashierClosureResource.CASHIER_CLOSURES).post().build();
    }

    private void closeCashier() {
        CashierClosureInputDto cashierClosureDto = new CashierClosureInputDto(new BigDecimal(23), new BigDecimal(10), "test");
        restService.loginAdmin().restBuilder().path(CashierClosureResource.CASHIER_CLOSURES).path(CashierClosureResource.LAST)
                .body(cashierClosureDto).patch().build();
    }

    @Test
    public void testCreateAndCreateCashierClosureException() {
        thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
        this.closeCashier();
    }

    @Test
    public void testCloseCashierClosureException() {
        this.createCashier();
        try {
            this.createCashier();
        } catch (HttpClientErrorException httpError) {
            assertEquals(HttpStatus.BAD_REQUEST, httpError.getStatusCode());
        }
        restService.reLoadTestDB();
    }

    @Test
    public void testGetCashierClosureLast() {
        CashierClosureLastOutputDto cashierClosureLastDto = restService.loginAdmin().restBuilder(new RestBuilder<CashierClosureLastOutputDto>())
                .clazz(CashierClosureLastOutputDto.class).path(CashierClosureResource.CASHIER_CLOSURES).path(CashierClosureResource.LAST).get()
                .build();
        assertTrue(cashierClosureLastDto.isClosed());
    }

}
