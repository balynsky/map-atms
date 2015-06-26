package su.balynsky.android.atms;

import su.balynsky.android.atms.content.file.AssetFileUtils;
import su.balynsky.android.atms.content.provider.AbstractContentProvider;
import su.balynsky.android.atms.content.provider.AtmsContentProvider;
import su.balynsky.android.atms.content.provider.BaseContentProvider;
import su.balynsky.android.atms.dao.IAtmsDao;
import su.balynsky.android.atms.dao.json.JsonDAOFactory;
import su.balynsky.android.atms.model.atm.AtmInfo;
import su.balynsky.android.atms.model.atm.AtmsInfoResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * @author Sergey Balynsky
 *         on 28.01.2015.
 */
public class DatabaseInit {
    private static final String COLUMN_TYPE_ID = " integer primary key autoincrement";
    private static final String COLUMN_TYPE_TEXT_NOT_NULL = " text not null";
    private static final String COLUMN_TYPE_TEXT = " text";
    private static final String COLUMN_TYPE_REAL_NOT_NULL = " real not null";
    private static final String COLUMN_SEPARATOR = ", ";
    private static final String NO_GENERATE = "NO";


    public static void main(String[] args) throws Exception {
        if (args.length != 3) {
            throw new IllegalArgumentException("Please put thre args (path and db name)");
        }
        if (args[2].equalsIgnoreCase(NO_GENERATE)) {
            System.out.println("[DatabaseInit]: No generate db.");
            return;
        }
        // load the sqlite-JDBC driver using the current class loader
        Class.forName("org.sqlite.JDBC");

        Connection connection = null;
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + args[0] + File.separator + args[1]);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists " + AtmsContentProvider.ATMS_TABLE);
            statement.executeUpdate(getCreateDatabaseQuery());

            IAtmsDao configurationDao = (IAtmsDao) (new JsonDAOFactory()).createInstance(IAtmsDao.class);
            System.out.println(configurationDao);
            AtmsInfoResponse branchesAndAtms = configurationDao.getAtmsInfo(new java.util.Date(0));

            for (AtmInfo info : branchesAndAtms.getModifiedAtms()) {
                insertAtm(connection, info);
            }

            ResultSet resultSet = statement.executeQuery("select count(1) from " + AtmsContentProvider.ATMS_TABLE);

            System.out.println("querying SELECT count(1) FROM " + AtmsContentProvider.ATMS_TABLE + " : " + resultSet.getInt(1));

            createRecordDatabaseInit(args[0]);

        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
            throw e;
        } finally {
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }

        System.out.println("DatabaseInit Success");
    }

    private static void createRecordDatabaseInit(String directory) throws IOException {
        java.util.Date currentDate = new java.util.Date();
        DateFormat df = new SimpleDateFormat(AtmsContentProvider.DATABASE_STAMP_FORMAT);
        Properties properties = new Properties();
        properties.load(new FileInputStream(new File(directory + File.separator + AssetFileUtils.STARTUP_PROPERTIES)));
        properties.setProperty(AtmsContentProvider.DATABASE_INI_DATE, df.format(currentDate));
        properties.store(new FileOutputStream(new File(directory + File.separator + AssetFileUtils.STARTUP_PROPERTIES)), null);
    }

    private static void insertAtm(Connection connection, AtmInfo info) throws SQLException {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ").append(AtmsContentProvider.ATMS_TABLE);
        sb.append("(");
        sb.append(AbstractContentProvider.Columns.LOCAL_STATUS).append(COLUMN_SEPARATOR);
        sb.append(AtmsContentProvider.Columns.CODE).append(COLUMN_SEPARATOR);
        sb.append(AtmsContentProvider.Columns.LATITUDE).append(COLUMN_SEPARATOR);
        sb.append(AtmsContentProvider.Columns.LONGTITUDE).append(COLUMN_SEPARATOR);
        sb.append(AtmsContentProvider.Columns.COMISSION).append(COLUMN_SEPARATOR);
        sb.append(AtmsContentProvider.Columns.DOLLARS).append(COLUMN_SEPARATOR);
        sb.append(AtmsContentProvider.Columns.HOURS).append(COLUMN_SEPARATOR);
        sb.append(AtmsContentProvider.Columns.TELEPHONE).append(COLUMN_SEPARATOR);
        sb.append(AtmsContentProvider.Columns.NAME).append(COLUMN_SEPARATOR);
        sb.append(AtmsContentProvider.Columns.ADDRESSL1).append(COLUMN_SEPARATOR);
        sb.append(AtmsContentProvider.Columns.ADDRESSL2).append(COLUMN_SEPARATOR);
        sb.append(AtmsContentProvider.Columns.CITY).append(COLUMN_SEPARATOR);
        sb.append(AtmsContentProvider.Columns.STATE);
        sb.append(") ");
        sb.append(" values (?,?,?,?,?,?,?,?,?,?,?,?,?)");

        PreparedStatement preparedStatement = connection.prepareStatement(sb.toString());

        preparedStatement.setString(1, BaseContentProvider.ContentStatus.READY.name());
        preparedStatement.setString(2, info.getCode());
        preparedStatement.setFloat(3, Float.parseFloat(info.getLatitude()));
        preparedStatement.setFloat(4, Float.parseFloat(info.getLongitude()));
        preparedStatement.setString(5, info.getCommissionFlag().toString());
        preparedStatement.setString(6, info.getDollarsFlag().toString());
        preparedStatement.setString(7, info.getHours());
        preparedStatement.setString(8, info.getTelephone());
        preparedStatement.setString(9, info.getName());
        preparedStatement.setString(10, info.getAddressL1());
        preparedStatement.setString(11, info.getAddressL2());
        preparedStatement.setString(12, info.getCity());
        preparedStatement.setString(13, info.getState());

        preparedStatement.executeUpdate();
        preparedStatement.close();

    }

    private static String getCreateDatabaseQuery() {
        StringBuilder builder = new StringBuilder();
        builder.append("create table ").append(AtmsContentProvider.ATMS_TABLE).append(" (");
        builder.append(AbstractContentProvider.Columns.LOCAL_ID).append(COLUMN_TYPE_ID).append(COLUMN_SEPARATOR);
        builder.append(AbstractContentProvider.Columns.LOCAL_STATUS).append(COLUMN_TYPE_TEXT_NOT_NULL).append(COLUMN_SEPARATOR);
        builder.append(AtmsContentProvider.Columns.CODE).append(COLUMN_TYPE_TEXT_NOT_NULL).append(COLUMN_SEPARATOR);
        builder.append(AtmsContentProvider.Columns.LATITUDE).append(COLUMN_TYPE_REAL_NOT_NULL).append(COLUMN_SEPARATOR);
        builder.append(AtmsContentProvider.Columns.LONGTITUDE).append(COLUMN_TYPE_REAL_NOT_NULL).append(COLUMN_SEPARATOR);
        builder.append(AtmsContentProvider.Columns.COMISSION).append(COLUMN_TYPE_TEXT).append(COLUMN_SEPARATOR);
        builder.append(AtmsContentProvider.Columns.DOLLARS).append(COLUMN_TYPE_TEXT).append(COLUMN_SEPARATOR);
        builder.append(AtmsContentProvider.Columns.HOURS).append(COLUMN_TYPE_TEXT).append(COLUMN_SEPARATOR);
        builder.append(AtmsContentProvider.Columns.TELEPHONE).append(COLUMN_TYPE_TEXT).append(COLUMN_SEPARATOR);
        builder.append(AtmsContentProvider.Columns.NAME).append(COLUMN_TYPE_TEXT).append(COLUMN_SEPARATOR);
        builder.append(AtmsContentProvider.Columns.ADDRESSL1).append(COLUMN_TYPE_TEXT).append(COLUMN_SEPARATOR);
        builder.append(AtmsContentProvider.Columns.ADDRESSL2).append(COLUMN_TYPE_TEXT).append(COLUMN_SEPARATOR);
        builder.append(AtmsContentProvider.Columns.CITY).append(COLUMN_TYPE_TEXT).append(COLUMN_SEPARATOR);
        builder.append(AtmsContentProvider.Columns.STATE).append(COLUMN_TYPE_TEXT);
        builder.append(");");
        return builder.toString();
    }
}
