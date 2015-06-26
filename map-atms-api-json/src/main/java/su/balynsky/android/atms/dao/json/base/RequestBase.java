package su.balynsky.android.atms.dao.json.base;

public abstract class RequestBase implements IRequest {
	public abstract String getRestUrl();

	public abstract Class<? extends ResponseBase> getResponseClass();

}
