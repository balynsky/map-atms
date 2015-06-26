package su.balynsky.android.atms.dao.json;

import su.balynsky.android.atms.dao.IDAOFactory;
import su.balynsky.android.atms.dao.json.base.IBaseDAO;
import su.balynsky.android.atms.exceptions.DAOFactoryException;

public class JsonDAOFactory extends BaseDAOImpl implements IDAOFactory {
	private static final String	BASE_URL	= "https://online.pumb.ua/ipumb2/";

	public Object createInstance(Class<?> requiredInterface) throws DAOFactoryException {
		try {
			Object obj = Class.forName(
					"su.balynsky.android.atms.dao.json." + requiredInterface.getSimpleName().substring(1) + "Impl")
					.newInstance();
			((IBaseDAO) obj).setBaseURL(BASE_URL);
			return obj;
		} catch (Exception e) {
			throw new DAOFactoryException("Error in createInstance method", e);
		}
	}

}
