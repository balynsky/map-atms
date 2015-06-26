package su.balynsky.android.atms.model.atm;

import java.util.Date;

public class ModificationRequiredInfo {
	private Date		currentDateTime	= null;
	private Boolean	isUpdateNeeded		= null;

	public ModificationRequiredInfo() {
		super();
	}

	public ModificationRequiredInfo(Date currentDateTime, Boolean isUpdateNeeded) {
		super();
		this.currentDateTime = currentDateTime;
		this.isUpdateNeeded = isUpdateNeeded;
	}

	public Date getCurrentDateTime() {
		return currentDateTime;
	}

	public void setCurrentDateTime(Date currentDateTime) {
		this.currentDateTime = currentDateTime;
	}

	public Boolean getIsUpdateNeeded() {
		return isUpdateNeeded;
	}

	public void setIsUpdateNeeded(Boolean isUpdateNeeded) {
		this.isUpdateNeeded = isUpdateNeeded;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModificationRequiredInfo [currentDateTime=");
		builder.append(currentDateTime);
		builder.append(", isUpdateNeeded=");
		builder.append(isUpdateNeeded);
		builder.append("]");
		return builder.toString();
	}
}
