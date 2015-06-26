package su.balynsky.android.atms.dao.json;

import su.balynsky.android.atms.dao.IAtmsDao;
import su.balynsky.android.atms.dao.json.base.IBaseDAO;
import su.balynsky.android.atms.dao.json.model.atm.GetAtmsRequest;
import su.balynsky.android.atms.dao.json.model.atm.GetAtmsResponse;
import su.balynsky.android.atms.dao.json.model.atm.IsModificationRequiredRequest;
import su.balynsky.android.atms.dao.json.model.atm.IsModificationRequiredResponse;
import su.balynsky.android.atms.exceptions.ConnectorException;
import su.balynsky.android.atms.model.atm.AtmsInfoResponse;
import su.balynsky.android.atms.model.atm.ModificationRequiredInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AtmsDaoImpl extends BaseDAOImpl implements IAtmsDao, IBaseDAO {
    private static final SimpleDateFormat DATE_FORMAT_COLON = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
    private static final SimpleDateFormat DATE_FORMAT_DOT = new SimpleDateFormat("dd.MM.yyyy hh.mm.ss");

    public AtmsInfoResponse getAtmsInfo(Date lastUpdateTime) throws ConnectorException {
        GetAtmsRequest request = new GetAtmsRequest();
        request.setLastUpdate(DATE_FORMAT_DOT.format(lastUpdateTime));

        GetAtmsResponse response = (GetAtmsResponse) Connector.getInstance().call(getBaseURL(), request);

        Date returnedCurrentDateTime = null;
        if (response.getCurrentDateTime() != null) {
            try {
                returnedCurrentDateTime = DATE_FORMAT_COLON.parse(response.getCurrentDateTime());
            } catch (ParseException e) {
                returnedCurrentDateTime = new Date(0);
            }
        }

        return new AtmsInfoResponse(response.getDeletedAtms(), response.getModifiedAtms(), returnedCurrentDateTime);
    }

    public ModificationRequiredInfo isModificationRequired(Date lastUpdateTime) throws ConnectorException {
        IsModificationRequiredRequest request = new IsModificationRequiredRequest();
        request.setLastUpdate(DATE_FORMAT_DOT.format(lastUpdateTime));

        IsModificationRequiredResponse response = (IsModificationRequiredResponse) Connector.getInstance().call(getBaseURL(), request);

        Date returnedCurrentDateTime = null;
        if (response.getCurrentDateTime() != null) {
            try {
                returnedCurrentDateTime = DATE_FORMAT_COLON.parse(response.getCurrentDateTime());
            } catch (ParseException e) {
                returnedCurrentDateTime = new Date(0);
            }
        }

        return new ModificationRequiredInfo(returnedCurrentDateTime, response.getIsUpdateNeeded());
    }
}
