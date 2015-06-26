package su.balynsky.android.atms.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AtmsTable {
	// Table name
	public static final String				TABLE_NAME						= "atms";

	// Columns names
	public static final String				COLUMN_ID						= "_id";
	public static final String				COLUMN_CODE						= "code";
	public static final String				COLUMN_LATITUDE				    = "latitude";
	public static final String				COLUMN_LONGITUDE				= "longitude";
	public static final String				COLUMN_COMMISSION_FLAG		    = "commissionFlag";
	public static final String				COLUMN_DOLLARS_FLAG			    = "dollarsFlag";
	public static final String				COLUMN_HOURS					= "hours";
	public static final String				COLUMN_TELEPHONE				= "telephone";
	public static final String				COLUMN_NAME						= "name";
	public static final String				COLUMN_ADDRESS_L1				= "addressL1";
	public static final String				COLUMN_ADDRESS_L2				= "addressL2";
	public static final String				COLUMN_CITY						= "city";
	public static final String				COLUMN_STATE					= "state";

	private static final String			COLUMN_TYPE_ID					= " integer primary key autoincrement";
	private static final String			COLUMN_TYPE_TEXT_NOT_NULL	= " text not null";
	private static final String			COLUMN_TYPE_REAL_NOT_NULL	= " real not null";
	private static final String			COLUMN_SEPARATOR				= ", ";

	// Creating a table
	private static final StringBuilder	CREATE_TABLE					= new StringBuilder();
	static {
		CREATE_TABLE.append("create table ").append(TABLE_NAME).append(" (");
		CREATE_TABLE.append(COLUMN_ID).append(COLUMN_TYPE_ID).append(COLUMN_SEPARATOR);
		CREATE_TABLE.append(COLUMN_CODE).append(COLUMN_TYPE_TEXT_NOT_NULL).append(COLUMN_SEPARATOR);
		CREATE_TABLE.append(COLUMN_LATITUDE).append(COLUMN_TYPE_REAL_NOT_NULL).append(COLUMN_SEPARATOR);
		CREATE_TABLE.append(COLUMN_LONGITUDE).append(COLUMN_TYPE_REAL_NOT_NULL).append(COLUMN_SEPARATOR);
		CREATE_TABLE.append(COLUMN_COMMISSION_FLAG).append(COLUMN_TYPE_TEXT_NOT_NULL).append(COLUMN_SEPARATOR);
		CREATE_TABLE.append(COLUMN_DOLLARS_FLAG).append(COLUMN_TYPE_TEXT_NOT_NULL).append(COLUMN_SEPARATOR);
		CREATE_TABLE.append(COLUMN_HOURS).append(COLUMN_TYPE_TEXT_NOT_NULL).append(COLUMN_SEPARATOR);
		CREATE_TABLE.append(COLUMN_TELEPHONE).append(COLUMN_TYPE_TEXT_NOT_NULL).append(COLUMN_SEPARATOR);
		CREATE_TABLE.append(COLUMN_NAME).append(COLUMN_TYPE_TEXT_NOT_NULL).append(COLUMN_SEPARATOR);
		CREATE_TABLE.append(COLUMN_ADDRESS_L1).append(COLUMN_TYPE_TEXT_NOT_NULL).append(COLUMN_SEPARATOR);
		CREATE_TABLE.append(COLUMN_ADDRESS_L2).append(COLUMN_TYPE_TEXT_NOT_NULL).append(COLUMN_SEPARATOR);
		CREATE_TABLE.append(COLUMN_CITY).append(COLUMN_TYPE_TEXT_NOT_NULL).append(COLUMN_SEPARATOR);
		CREATE_TABLE.append(COLUMN_STATE).append(COLUMN_TYPE_TEXT_NOT_NULL);
		CREATE_TABLE.append(");");
	}

	// Deleting a table
	private static final StringBuilder	DROP_TABLE						= new StringBuilder();
	static {
		DROP_TABLE.append("drop table if exists ").append(TABLE_NAME).append(";");
	}

	// SELECT columns names
	public static final String[]			ALL_COLUMNS						= { COLUMN_ID, COLUMN_CODE, COLUMN_LATITUDE,
			COLUMN_LONGITUDE, COLUMN_COMMISSION_FLAG, COLUMN_DOLLARS_FLAG, COLUMN_HOURS, COLUMN_TELEPHONE, COLUMN_NAME,
			COLUMN_ADDRESS_L1, COLUMN_ADDRESS_L2, COLUMN_CITY, COLUMN_STATE };

	public static void onCreate(SQLiteDatabase database) {
		Log.d(AtmsTable.class.getName(), CREATE_TABLE.toString());
		database.execSQL(CREATE_TABLE.toString());
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.i(AtmsTable.class.getName(), "Upgrading version from " + oldVersion + " to " + newVersion);
		Log.d(AtmsTable.class.getName(), DROP_TABLE.toString());
		database.execSQL(DROP_TABLE.toString());
		onCreate(database);
	}

}
