package su.balynsky.android.atms.test;

import junit.framework.Assert;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import su.balynsky.android.atms.dao.IDAOFactory;
import su.balynsky.android.atms.dao.json.JsonDAOFactory;

/*
 * Для тестирования нужно зарегистрировать в пользовательских переменных окружения 
 * логин и пароль пользователя, под которым будет выполнен вход:
 * IPUMB_LOGIN и IPUMB_PASSWORD соответственно
 */

public class TestDaoBase extends Assert {
	private static final String			IPUMB_LOGIN		= "IPUMB_LOGIN";
	private static final String			IPUMB_PASSWORD	= "IPUMB_PASSWORD";
	protected static final IDAOFactory daoFactory		= new JsonDAOFactory();

	@Rule
	public final ExpectedException		thrown			= ExpectedException.none();
/*
	@BeforeClass
	public static void login() throws Exception {
		DEBUG.DEBUG_MODE = true;
		IClientDao loginPage = null;
		loginPage = (IClientDao) daoFactory.createInstance(IClientDao.class);
		System.out.println("BaseURL: " + ((IBaseDAO) loginPage).getBaseURL());
		// Сначала проверим пароль, потом логин. Т.к. если кто-то пропишет только логин, он себя залочит
		assertNotNull("Не установлена переменная среды IPUMB_PASSWORD", System.getenv(IPUMB_PASSWORD));
		assertNotNull("Не установлена переменная среды IPUMB_LOGIN", System.getenv(IPUMB_LOGIN));
		loginPage.login(System.getenv(IPUMB_LOGIN), System.getenv(IPUMB_PASSWORD));
	}

	@AfterClass
	public static void logout() throws Exception {
		IClientDao loginPage = null;
		loginPage = (IClientDao) daoFactory.createInstance(IClientDao.class);
		loginPage.logout();
	}*/
}
