package es.upm.miw.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

import es.upm.miw.controllers.TicketController;
import es.upm.miw.dtos.HistoricalProductOutPutDto;
import es.upm.miw.dtos.IncomeComparision;
import es.upm.miw.dtos.NumProductsSoldDto;
import es.upm.miw.dtos.ShoppingDto;
import es.upm.miw.dtos.TicketCreationInputDto;
import es.upm.miw.dtos.TicketSearchOutputDto;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:test.properties")
public class TicketResourceFunctionalTesting {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Autowired
	private RestService restService;

	@Autowired
	private TicketController ticketController;

	@Test
	public void testCreateTicket() {
		TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(new BigDecimal("1"),
				new BigDecimal("1"), new BigDecimal("1"), new ArrayList<ShoppingDto>(), "Nota asociada al ticket");
		ticketCreationInputDto.getShoppingCart().add(new ShoppingDto("1", "various", new BigDecimal("100"), 1,
				new BigDecimal("50.00"), new BigDecimal("50"), false));
		ticketCreationInputDto.getShoppingCart().add(new ShoppingDto("1", "various", new BigDecimal("100"), 2,
				new BigDecimal("50.00"), new BigDecimal("100"), true));
		ticketCreationInputDto.getShoppingCart().add(new ShoppingDto("article2", "descrip-a2", new BigDecimal("10"),
				100, new BigDecimal("0"), new BigDecimal("1000"), true));
		restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).path(TicketResource.TICKETS)
				.body(ticketCreationInputDto).clazz(byte[].class).post().build();
	}

	@Test
	public void testCreateTicketCashNegativeException() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(new BigDecimal("-1"),
				new BigDecimal("1"), new BigDecimal("1"), new ArrayList<ShoppingDto>(), "Nota asociada al ticket");
		restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).path(TicketResource.TICKETS)
				.body(ticketCreationInputDto).clazz(byte[].class).post().build();
	}

	@Test
	public void testCreateTicketCardNegativeException() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(new BigDecimal("1"),
				new BigDecimal("-1"), new BigDecimal("1"), new ArrayList<ShoppingDto>(), "Nota asociada al ticket");
		restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).path(TicketResource.TICKETS)
				.body(ticketCreationInputDto).clazz(byte[].class).post().build();
	}

	@Test
	public void testCreateTicketVoucherNegativeException() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(new BigDecimal("1"),
				new BigDecimal("1"), new BigDecimal("-1"), new ArrayList<ShoppingDto>(), "Nota asociada al ticket");
		restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).path(TicketResource.TICKETS)
				.body(ticketCreationInputDto).clazz(byte[].class).post().build();
	}

	@Test
	public void testCreateTicketShoppingNullException() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(new BigDecimal("1"),
				new BigDecimal("1"), new BigDecimal("1"), null, "Nota asociada al ticket");
		restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).path(TicketResource.TICKETS)
				.body(ticketCreationInputDto).clazz(byte[].class).post().build();
	}

	@Test
	public void testCreateTicketShoppingEmptyException() {
		thrown.expect(new HttpMatcher(HttpStatus.BAD_REQUEST));
		TicketCreationInputDto ticketCreationInputDto = new TicketCreationInputDto(new BigDecimal("1"),
				new BigDecimal("1"), new BigDecimal("1"), new ArrayList<ShoppingDto>(), "Nota asociada al ticket");
		restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).path(TicketResource.TICKETS)
				.body(ticketCreationInputDto).clazz(byte[].class).post().build();
	}

	@Test
	public void testGetTicket() {
		byte[] bodyResponse = restService.loginAdmin().restBuilder(new RestBuilder<byte[]>())
				.path(TicketResource.TICKETS).path(TicketResource.ID_ID).expand("201801121").clazz(byte[].class).get()
				.build();
		assertNotNull(bodyResponse);
	}

	@Test
	public void testGetTicketIdNotFoundException() {
		thrown.expect(new HttpMatcher(HttpStatus.NOT_FOUND));
		restService.loginAdmin().restBuilder(new RestBuilder<byte[]>()).path(TicketResource.TICKETS)
				.path(TicketResource.ID_ID).expand("20180112-6").clazz(byte[].class).get().log().build();
	}

	@Test
	public void testFindIdArticleDatesBetween() throws ParseException {
		Date dateStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-01-01 00:00:00");
		Date dateFinish = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2018-12-31 11:59:59");
		List<TicketSearchOutputDto> searchOutputDtos = Arrays.asList(restService.loginAdmin()
				.restBuilder(new RestBuilder<TicketSearchOutputDto[]>()).clazz(TicketSearchOutputDto[].class)
				.path(TicketResource.TICKETS).path(TicketResource.SEARCH_BY_ID_AND_DATES).param("id", "article1")
				.param("dateStart", "2018-01-01 00:00:00").param("dateFinish", "2018-12-31 11:59:59").get().build());
		List<TicketSearchOutputDto> searchOutputDtos_ = ticketController.findByIdArticleDatesBetween("article1",
				dateStart, dateFinish);
		assertEquals(searchOutputDtos_.size(), searchOutputDtos.size());
	}

	@Test
	public void testGetHistoricalProductsData() throws ParseException {

		Boolean result = false;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.add(Calendar.DATE, 7);
		today.add(Calendar.MONTH, -1);
		Date initDate = today.getTime();
		today.add(Calendar.MONTH, 1);
		Date endDate = today.getTime();
		format.format(initDate);

		List<HistoricalProductOutPutDto> historicalData = Arrays.asList(restService.loginAdmin()
				.restBuilder(new RestBuilder<HistoricalProductOutPutDto[]>()).clazz(HistoricalProductOutPutDto[].class)
				.path(TicketResource.TICKETS).path(TicketResource.HISTORICAL_PRODUCTS)
				.param("initDate", format.format(initDate)).param("endDate", format.format(endDate)).get().build());

		List<String> articleNamesCollection = Arrays.asList("ar11", "ar12");
		for (HistoricalProductOutPutDto item : historicalData) {
			if (articleNamesCollection.contains(item.getProductName())) {
				if (item.getNumProductsPerMonth().contains(6)) {
					result = true;
				} else
					result = false;
			}
		}
		assertTrue(result);
	}

	@Test
	public void testGetNumProductsSold() throws ParseException {

		Boolean result = false;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.add(Calendar.DATE, 7);
		today.add(Calendar.MONTH, -1);
		Date initDate = today.getTime();
		today.add(Calendar.MONTH, 1);
		Date endDate = today.getTime();
		format.format(initDate);

		List<NumProductsSoldDto> data = Arrays.asList(restService.loginAdmin()
				.restBuilder(new RestBuilder<NumProductsSoldDto[]>()).clazz(NumProductsSoldDto[].class)
				.path(TicketResource.TICKETS).path(TicketResource.NUM_PRODUCTS_SOLD)
				.param("initDate", format.format(initDate)).param("endDate", format.format(endDate)).get().build());

		List<String> articleNamesCollection = Arrays.asList("ar11", "ar12");
		for (NumProductsSoldDto item : data) {
			if (articleNamesCollection.contains(item.getProductName())) {
				if (item.getQuantity() == 6) {
					result = true;
				} else
					result = false;
			}
		}
		assertTrue(result);
	}
	
	
	@Test
	public void testGetComparisionIncome() throws ParseException {

		Boolean result = false;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.add(Calendar.DATE, 7);
		today.add(Calendar.MONTH, -1);
		Date initDate = today.getTime();
		today.add(Calendar.MONTH, 1);
		Date endDate = today.getTime();
		format.format(initDate);

		
		List<IncomeComparision> data = Arrays.asList(restService.loginAdmin()
				.restBuilder(new RestBuilder<IncomeComparision[]>()).clazz(IncomeComparision[].class)
				.path(TicketResource.TICKETS).path(TicketResource.COMPARISION_INCOME)
				.param("initDate", format.format(initDate)).param("endDate", format.format(endDate)).get().build());
		
		
		List<String> articleNamesCollection = Arrays.asList("ar11", "ar12", "ar13");
		float expectedIncome = 120;
		float expectedProductPrice = 150;
		for (IncomeComparision item : data) {
			if (articleNamesCollection.contains(item.getProductName())) {
				if (item.getProductName().equals("ar13") && item.getIncome().equals(expectedIncome)
						&& item.getProductPrice().equals(expectedProductPrice)) {
					result = true;
				} else if (item.getIncome().equals(item.getProductPrice())) {
					result = true;
				} else {
					result = false;
					break;
				}
			}
		}
		assertTrue(result);
	}

}
