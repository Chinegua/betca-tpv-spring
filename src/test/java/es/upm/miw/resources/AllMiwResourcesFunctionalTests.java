package es.upm.miw.resources;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    AdminResourceFunctionalTesting.class,
    ArticleResourceFunctionalTesting.class,
    BudgetResourceFunctionalTesting.class,
    CashierClosureResourceFunctionalTesting.class,
    InvoiceResourceFunctionalTesting.class,
    OfferResourceFunctionalTesting.class,
    OrderResourceFunctionalTesting.class,
    ProviderResourceFunctionalTesting.class,
    TicketResourceFunctionalTesting.class,
    TokenResourceFunctionalTesting.class,
    UserResourceFunctionalTesting.class,
    VoucherResourceFunctionalTesting.class
})
public class AllMiwResourcesFunctionalTests {

}
