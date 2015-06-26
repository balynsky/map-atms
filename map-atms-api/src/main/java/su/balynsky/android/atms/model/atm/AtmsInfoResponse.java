package su.balynsky.android.atms.model.atm;

import java.util.Arrays;
import java.util.Date;

public class AtmsInfoResponse {
	private String[]				DeletedAtms			= null;
	private AtmInfo[]				ModifiedAtms		= null;
	private Date					CurrentDateTime	= null;
	
	public AtmsInfoResponse() {
		super();
	}

	public AtmsInfoResponse(String[] deletedAtms,
                            AtmInfo[] modifiedAtms, Date currentDateTime) {
		super();
		DeletedAtms = deletedAtms;
		ModifiedAtms = modifiedAtms;
		CurrentDateTime = currentDateTime;
	}

	public String[] getDeletedAtms() {
		return DeletedAtms;
	}

	public void setDeletedAtms(String[] deletedAtms) {
		DeletedAtms = deletedAtms;
	}

	public AtmInfo[] getModifiedAtms() {
		return ModifiedAtms;
	}

	public void setModifiedAtms(AtmInfo[] modifiedAtms) {
		ModifiedAtms = modifiedAtms;
	}

	public Date getCurrentDateTime() {
		return CurrentDateTime;
	}

	public void setCurrentDateTime(Date currentDateTime) {
		CurrentDateTime = currentDateTime;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(Arrays.toString(DeletedAtms));
		builder.append(", ModifiedAtms=");
		builder.append(Arrays.toString(ModifiedAtms));
		builder.append(", CurrentDateTime=");
		builder.append(CurrentDateTime);
		builder.append("]");
		return builder.toString();
	}
}
