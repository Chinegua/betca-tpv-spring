package es.upm.miw.repositories.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    ArticleRepositoryIT.class,
    ProviderRepositoryIT.class,
    TicketRepositoryIT.class,
    VoucherRepositoryIT.class,

    })
public class AllMiwRepositoriesCoreIntegrationTets {

}
