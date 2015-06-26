package su.balynsky.android.atms.dao.json.base;

/**
 * Базовый ответ REST API iPUMB
 * 
 * @author Sergey Balynsky
 */
public class ResponseBase {
	private String		Error					= null;
	private Boolean	IsSessionExpired	= null;
	private Boolean	Success				= null;
	private String		Guid					= null;

	public String getError() {
		return Error;
	}

	public Boolean getIsSessionExpired() {
		return IsSessionExpired;
	}

	public Boolean getSuccess() {
		return Success;
	}

	public String getGuid() {
		return Guid;
	}
}