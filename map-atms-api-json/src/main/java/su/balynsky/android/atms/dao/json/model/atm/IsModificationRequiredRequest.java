package su.balynsky.android.atms.dao.json.model.atm;

import su.balynsky.android.atms.dao.json.base.RequestBase;
import su.balynsky.android.atms.dao.json.base.ResponseBase;

public class IsModificationRequiredRequest extends RequestBase {
	private String		LastUpdate	= null;

	public String getLastUpdate() {
		return LastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		LastUpdate = lastUpdate;
	}

	@Override
	public String getRestUrl() {
		return "Configuration.svc/ismodificationrequired";
	}

	@Override
	public Class<? extends ResponseBase> getResponseClass() {
		return IsModificationRequiredResponse.class;
	}

}
