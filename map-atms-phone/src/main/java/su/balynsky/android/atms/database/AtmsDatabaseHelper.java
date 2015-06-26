package su.balynsky.android.atms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Код [пере]создания БД путем копирования взят отсюда:
 * http://stackoverflow.com/questions/513084/how-to-ship-an-android-application-with-a-database
 */
public class AtmsDatabaseHelper extends SQLiteOpenHelper {
    private final static String TAG = AtmsDatabaseHelper.class.getCanonicalName();
    private final Context context;
    private final String databaseName;
    private final String databasePath;

    private boolean createDatabase = false;
    private boolean upgradeDatabase = false;

    public AtmsDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
        this.databaseName = name;
        // Реальный путь к файлу БД
        databasePath = context.getDatabasePath(databaseName).getAbsolutePath();
        Log.d(TAG, "AtmsDatabaseHelper databasePath:" + databasePath);
    }

    public void initializeDataBase() {
        /*
         * Creates or updates the database in internal storage if it is needed
         * before opening the database. In all cases opening the database copies
         * the database in internal storage to the cache.
         */
        getWritableDatabase();

        if (createDatabase) {
            /*
             * If the database is created by the copy method, then the creation
             * code needs to go here. This method consists of copying the new
             * database from assets into internal storage and then caching it.
             */
            try {
                /*
                 * Write over the empty data that was created in internal
                 * storage with the one in assets and then cache it.
                 */
                copyDataBase();
            } catch (Exception e) {
                throw new Error("Error copying database");
            }
        } else if (upgradeDatabase) {
            /*
             * If the database is upgraded by the copy and reload method, then
             * the upgrade code needs to go here. This method consists of
             * renaming the old database in internal storage, create an empty
             * new database in internal storage, copying the database from
             * assets to the new database in internal storage, caching the new
             * database from internal storage, loading the data from the old
             * database into the new database in the cache and then deleting the
             * old database from internal storage.
             */
           /* try {
                FileHelper.copyFile(DB_PATH, OLD_DB_PATH);
                copyDataBase();
                SQLiteDatabase old_db = SQLiteDatabase.openDatabase(OLD_DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
                SQLiteDatabase new_db = SQLiteDatabase.openDatabase(DB_PATH,null, SQLiteDatabase.OPEN_READWRITE);
                *//*
                 * Add code to load data into the new database from the old
                 * database and then delete the old database from internal
                 * storage after all data has been transferred.
                 *//*
            } catch (IOException e) {
                throw new Error("Error copying database");
            }*/
        }

    }

    /**
     * Копирование БД из assets с перезаписью существующей
     */
    private void copyDataBase() {
        Log.d(getClass().getName(), "copyDataBase");
        try {
            close();
            InputStream inputDbStream = context.getAssets().open(databaseName);
            if (inputDbStream != null) {
                Log.d(getClass().getName(), "inputDbStream success created");
            }
            OutputStream outputDbStream = new FileOutputStream(databasePath);
            copyFile(inputDbStream, outputDbStream);
            getWritableDatabase().close();
        } catch (IOException e) {
            Log.e(getClass().getName(), "Error copy database file", e);
        }
    }

    private void copyFile(InputStream fromFile, OutputStream toFile) throws IOException {
        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;

        try {
            while ((length = fromFile.read(buffer)) > 0) {
                toFile.write(buffer, 0, length);
            }
        }
        // Close the streams
        finally {
            try {
                if (toFile != null) {
                    try {
                        toFile.flush();
                    } finally {
                        toFile.close();
                    }
                }
            } finally {
                if (fromFile != null) {
                    fromFile.close();
                }
            }
        }
    }

    /**
     * This is where the creation of tables and the initial population of the
     * tables should happen, if a database is being created from scratch instead
     * of being copied from the application package assets. Copying a database
     * from the application package assets to internal storage inside this method
     * will result in a corrupted database.
     * <p/>
     * NOTE: This method is normally only called when a database has not already
     * been created. When the database has been copied, then this method is
     * called the first time a reference to the database is retrieved after the
     * database is copied since the database last cached by SQLiteOpenHelper is
     * different than the database in internal storage.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "AtmsDatabaseHelper.onCreate");
        createDatabase = true;
        Log.d(getClass().getName(), "onCreate");
    }

    /**
     * Called only if version number was changed and the database has already
     * been created. Copying a database from the application package assets to
     * the internal data system inside this method will result in a corrupted
     * database in the internal data system.
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.d(TAG, "AtmsDatabaseHelper.onUpgrade");
        upgradeDatabase = true;
        Log.d(getClass().getName(), "onUpgrade");
    }

    /**
     * Called everytime the database is opened by getReadableDatabase or
     * getWritableDatabase. This is called after onCreate or onUpgrade is called.
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.d(TAG, "AtmsDatabaseHelper.OnOpen");
        super.onOpen(db);
    }

}
