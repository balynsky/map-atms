package su.balynsky.android.atms.dao.json;


import su.balynsky.android.atms.dao.json.base.IBaseDAO;

public class BaseDAOImpl implements IBaseDAO {
	private String	baseURL;

	public String getBaseURL() {
		return baseURL;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

}
