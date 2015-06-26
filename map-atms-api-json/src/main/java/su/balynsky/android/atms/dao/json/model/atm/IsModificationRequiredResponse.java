package su.balynsky.android.atms.dao.json.model.atm;

import su.balynsky.android.atms.dao.json.base.ResponseBase;

public class IsModificationRequiredResponse extends ResponseBase {
	private String		CurrentDateTime	= null;
	private Boolean	IsUpdateNeeded		= null;

	public String getCurrentDateTime() {
		return CurrentDateTime;
	}

	public void setCurrentDateTime(String currentDateTime) {
		CurrentDateTime = currentDateTime;
	}

	public Boolean getIsUpdateNeeded() {
		return IsUpdateNeeded;
	}

	public void setIsUpdateNeeded(Boolean isUpdateNeeded) {
		IsUpdateNeeded = isUpdateNeeded;
	}
}
