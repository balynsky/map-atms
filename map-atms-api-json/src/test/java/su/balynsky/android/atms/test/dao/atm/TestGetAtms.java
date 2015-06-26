package su.balynsky.android.atms.test.dao.atm;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import su.balynsky.android.atms.dao.IAtmsDao;
import su.balynsky.android.atms.dao.IDAOFactory;
import su.balynsky.android.atms.dao.json.JsonDAOFactory;
import su.balynsky.android.atms.dao.json.base.IBaseDAO;
import su.balynsky.android.atms.model.atm.AtmsInfoResponse;
import su.balynsky.android.atms.model.atm.ModificationRequiredInfo;

import java.util.Date;

public class TestGetAtms extends Assert {
	@SuppressWarnings("deprecation")
	private static Date						LAST_UPDATE_TIME			= new Date(2012 - 1900, 1, 1, 9, 45, 15);
	private static Date						LAST_UPDATE_TIME_ZERO	= new Date(0);

	protected static final IDAOFactory daoFactory					= new JsonDAOFactory();

	private IAtmsDao configurationDao;

	@Before
	public void createDao() throws Exception {
		configurationDao = (IAtmsDao) daoFactory.createInstance(IAtmsDao.class);
		assertNotNull(((IBaseDAO) configurationDao).getBaseURL());
		System.out.println(((IBaseDAO) configurationDao).getBaseURL());
	}

	@After
	public void removeDao() {
		configurationDao = null;
	}

	@Test
	public void testIsModificationRequired() throws Exception {
		System.out.println("TestGetAtms.testIsModificationRequired()");

		ModificationRequiredInfo info = configurationDao.isModificationRequired(LAST_UPDATE_TIME);
		System.out.println(info);
	}

	@Test
	public void testGetBranchesAndAtmsUpdate() throws Exception {
		System.out.println("TestGetAtms.testGetBranchesAndAtmsUpdate()");

		AtmsInfoResponse info = configurationDao.getAtmsInfo(LAST_UPDATE_TIME);
		System.out.println(info);
	}

	@Test
	public void testGetBranchesAndAtmsAll() throws Exception {
		System.out.println("TestGetAtms.testGetBranchesAndAtmsAll()");

		AtmsInfoResponse info = configurationDao.getAtmsInfo(LAST_UPDATE_TIME_ZERO);
		System.out.println(info);
	}

}
