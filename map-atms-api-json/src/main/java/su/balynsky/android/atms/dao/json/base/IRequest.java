package su.balynsky.android.atms.dao.json.base;

/**
 * Базовый класс всех запросов JSON API iPUMB
 * 
 * @author Sergey Balynsky
 */

public interface IRequest {
	/**
	 * Возвращает URL REST-сервиса для выполнения запроса относительно базового
	 * URL сервиса iPUMB. Базовый URL хранится в strings.xml в параметре
	 * strJsonBaseUrl. </p> Например, "Client.svc/logon" </p>
	 * 
	 * @return URL
	 */
	String getRestUrl();

	/**
	 * Возвращает тип ответа, который ожидается получить от REST-сервиса iPUMB в
	 * ответ на данный запрос
	 * 
	 * @return тип ответа
	 */
	Class<? extends ResponseBase> getResponseClass();
}
