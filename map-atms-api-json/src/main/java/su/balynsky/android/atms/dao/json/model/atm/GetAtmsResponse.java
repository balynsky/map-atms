package su.balynsky.android.atms.dao.json.model.atm;

import su.balynsky.android.atms.dao.json.base.ResponseBase;
import su.balynsky.android.atms.model.atm.AtmInfo;

public class GetAtmsResponse extends ResponseBase {
    private String[] DeletedAtms = null;
    private AtmInfo[] ModifiedAtms = null;
    private String CurrentDateTime = null;

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

    public String getCurrentDateTime() {
        return CurrentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        CurrentDateTime = currentDateTime;
    }
}
