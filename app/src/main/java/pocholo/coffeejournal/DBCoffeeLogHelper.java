package pocholo.coffeejournal;

import java.util.ArrayList;
import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBCoffeeLogHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String COFFEELOG_TABLE_NAME = "coffeelog";
    public static final String COFFEELOG_COLUMN_ID = "_id";
    public static final String COFFEELOG_COLUMN_NAME = "name";
    public static final String COFFEELOG_COLUMN_ROASTER = "roaster";
    public static final String COFFEELOG_COLUMN_CITY = "city";
    public static final String COFFEELOG_COLUMN_COUNTRY = "country";
    public static final String COFFEELOG_COLUMN_ROASTDATE = "roastdate";
    public static final String COFFEELOG_COLUMN_BREWDATE = "brewdate";
    public static final String COFFEELOG_COLUMN_BREWMETHOD = "brewmethod";
    public static final String COFFEELOG_COLUMN_COFFEEGRAMS = "coffegrams";
    public static final String COFFEELOG_COLUMN_WATERGRAMS = "watergrams";
    public static final String COFFEELOG_COLUMN_OVERALL = "overall";
    public static final String COFFEELOG_COLUMN_TASTEPROFILE = "tasteprofile";
    public static final String COFFEELOG_COLUMN_NOTES = "notes";

    public DBCoffeeLogHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " +
                        COFFEELOG_TABLE_NAME + " (" +
                        COFFEELOG_COLUMN_ID + " integer primary key, " +
                        COFFEELOG_COLUMN_NAME + " text," +
                        COFFEELOG_COLUMN_ROASTER + " text," +
                        COFFEELOG_COLUMN_CITY + " text," +
                        COFFEELOG_COLUMN_COUNTRY + " text," +
                        COFFEELOG_COLUMN_ROASTDATE + " date," +
                        COFFEELOG_COLUMN_BREWDATE + " date," +
                        COFFEELOG_COLUMN_BREWMETHOD + " text," +
                        COFFEELOG_COLUMN_COFFEEGRAMS + " grams," +
                        COFFEELOG_COLUMN_WATERGRAMS + " grams," +
                        COFFEELOG_COLUMN_OVERALL + " rating," +
                        COFFEELOG_COLUMN_TASTEPROFILE + " ratings," +
                        COFFEELOG_COLUMN_NOTES + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + COFFEELOG_TABLE_NAME);
        onCreate(db);
    }

    public long insertCoffeeLog(CoffeeLog coffeeLog) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COFFEELOG_COLUMN_NAME, coffeeLog.Name);
        contentValues.put(COFFEELOG_COLUMN_ROASTER, coffeeLog.Roaster);
        contentValues.put(COFFEELOG_COLUMN_CITY, coffeeLog.City);
        contentValues.put(COFFEELOG_COLUMN_COUNTRY, coffeeLog.Country);
        contentValues.put(COFFEELOG_COLUMN_ROASTDATE, coffeeLog.RoastDate.getTime());
        contentValues.put(COFFEELOG_COLUMN_BREWDATE, coffeeLog.BrewDate.getTime());
        contentValues.put(COFFEELOG_COLUMN_BREWMETHOD, coffeeLog.BrewMethod);
        contentValues.put(COFFEELOG_COLUMN_COFFEEGRAMS, coffeeLog.CoffeeGrams);
        contentValues.put(COFFEELOG_COLUMN_WATERGRAMS, coffeeLog.WaterGrams);
        contentValues.put(COFFEELOG_COLUMN_OVERALL, coffeeLog.Overall);
        contentValues.put(COFFEELOG_COLUMN_TASTEPROFILE, coffeeLog.TasteProfile);
        contentValues.put(COFFEELOG_COLUMN_NOTES, coffeeLog.Notes);

        return db.insert(COFFEELOG_TABLE_NAME, null, contentValues);
    }

    public CoffeeLog getCoffeeLog(long id) {
        CoffeeLog coffeeLog = new CoffeeLog();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + COFFEELOG_TABLE_NAME + " where " + COFFEELOG_COLUMN_ID + "=" + id + "", null);
        if (res != null) {
            res.moveToFirst();
            coffeeLog.Id = res.getLong(res.getColumnIndex(COFFEELOG_COLUMN_ID));
            coffeeLog.Name = res.getString(res.getColumnIndex(COFFEELOG_COLUMN_NAME));
            coffeeLog.Roaster = res.getString(res.getColumnIndex(COFFEELOG_COLUMN_ROASTER));
            coffeeLog.City = res.getString(res.getColumnIndex(COFFEELOG_COLUMN_CITY));
            coffeeLog.Country = res.getString(res.getColumnIndex(COFFEELOG_COLUMN_COUNTRY));
            coffeeLog.RoastDate = new Date(res.getLong(res.getColumnIndex(COFFEELOG_COLUMN_ROASTDATE)));
            coffeeLog.BrewDate = new Date(res.getLong(res.getColumnIndex(COFFEELOG_COLUMN_BREWDATE)));
            coffeeLog.BrewMethod = res.getString(res.getColumnIndex(COFFEELOG_COLUMN_BREWMETHOD));
            coffeeLog.CoffeeGrams = res.getDouble(res.getColumnIndex(COFFEELOG_COLUMN_COFFEEGRAMS));
            coffeeLog.WaterGrams = res.getDouble(res.getColumnIndex(COFFEELOG_COLUMN_WATERGRAMS));
            coffeeLog.Overall = res.getInt(res.getColumnIndex(COFFEELOG_COLUMN_OVERALL));
            coffeeLog.TasteProfile = res.getString(res.getColumnIndex(COFFEELOG_COLUMN_TASTEPROFILE));
            coffeeLog.Notes = res.getString(res.getColumnIndex(COFFEELOG_COLUMN_NOTES));
            if (!res.isClosed()) {
                res.close();
            }
        }
        return coffeeLog;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, COFFEELOG_TABLE_NAME);
    }


    public int updateCoffeeLog(Long id, CoffeeLog coffeeLog) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COFFEELOG_COLUMN_NAME, coffeeLog.Name);
        contentValues.put(COFFEELOG_COLUMN_ROASTER, coffeeLog.Roaster);
        contentValues.put(COFFEELOG_COLUMN_CITY, coffeeLog.City);
        contentValues.put(COFFEELOG_COLUMN_COUNTRY, coffeeLog.Country);
        contentValues.put(COFFEELOG_COLUMN_ROASTDATE, coffeeLog.RoastDate.getTime());
        contentValues.put(COFFEELOG_COLUMN_BREWDATE, coffeeLog.BrewDate.getTime());
        contentValues.put(COFFEELOG_COLUMN_BREWMETHOD, coffeeLog.BrewMethod);
        contentValues.put(COFFEELOG_COLUMN_COFFEEGRAMS, coffeeLog.CoffeeGrams);
        contentValues.put(COFFEELOG_COLUMN_WATERGRAMS, coffeeLog.WaterGrams);
        contentValues.put(COFFEELOG_COLUMN_OVERALL, coffeeLog.Overall);
        contentValues.put(COFFEELOG_COLUMN_TASTEPROFILE, coffeeLog.TasteProfile);
        contentValues.put(COFFEELOG_COLUMN_NOTES, coffeeLog.Notes);

        return db.update(COFFEELOG_TABLE_NAME, contentValues, COFFEELOG_COLUMN_ID + " = ? ", new String[]{Long.toString(id)});
    }

    public Integer deleteCoffeeLog(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(COFFEELOG_TABLE_NAME,
                COFFEELOG_COLUMN_ID + " = ? ",
                new String[]{Long.toString(id)});
    }

    public CoffeeLog[] getCoffeeLogs() {
        ArrayList<CoffeeLog> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + COFFEELOG_TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            CoffeeLog coffeeLog = new CoffeeLog();
            coffeeLog.Id = res.getInt(res.getColumnIndex(COFFEELOG_COLUMN_ID));
            coffeeLog.Name = res.getString(res.getColumnIndex(COFFEELOG_COLUMN_NAME));
            coffeeLog.Roaster = res.getString(res.getColumnIndex(COFFEELOG_COLUMN_ROASTER));
            coffeeLog.City = res.getString(res.getColumnIndex(COFFEELOG_COLUMN_CITY));
            coffeeLog.Country = res.getString(res.getColumnIndex(COFFEELOG_COLUMN_COUNTRY));
            coffeeLog.RoastDate = new Date(res.getLong(res.getColumnIndex(COFFEELOG_COLUMN_ROASTDATE)));
            coffeeLog.BrewDate = new Date(res.getLong(res.getColumnIndex(COFFEELOG_COLUMN_BREWDATE)));
            coffeeLog.BrewMethod = res.getString(res.getColumnIndex(COFFEELOG_COLUMN_BREWMETHOD));
            coffeeLog.CoffeeGrams = res.getDouble(res.getColumnIndex(COFFEELOG_COLUMN_COFFEEGRAMS));
            coffeeLog.WaterGrams = res.getDouble(res.getColumnIndex(COFFEELOG_COLUMN_WATERGRAMS));
            coffeeLog.Overall = res.getInt(res.getColumnIndex(COFFEELOG_COLUMN_OVERALL));
            coffeeLog.TasteProfile = res.getString(res.getColumnIndex(COFFEELOG_COLUMN_TASTEPROFILE));
            coffeeLog.Notes = res.getString(res.getColumnIndex(COFFEELOG_COLUMN_NOTES));

            array_list.add(coffeeLog);
            res.moveToNext();
        }
        res.close();
        CoffeeLog[] dbCoffeeLogs = new CoffeeLog[array_list.size()];
        array_list.toArray(dbCoffeeLogs);

        return dbCoffeeLogs;
    }

    public Cursor getCoffeeLogsCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + COFFEELOG_TABLE_NAME, null);
        if (res != null) {
            res.moveToFirst();
        }
        return res;
    }
}