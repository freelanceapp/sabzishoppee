package ibt.sabziwala.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Win-7 on 5/14/2016.
 */
public class Helper extends SQLiteOpenHelper {

    public static String DBName = "sabzishoppee";
    public static int DBVERSION = 2;

    public Helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("Create table " + DatabaseConstant.TABLE_NAME_WISHLIST + "(" + DatabaseConstant.TABLE_WISHLIST_ID + " text,"
                + DatabaseConstant.TABLE_WISHLIST_TITLE + " text,"
                + DatabaseConstant.TABLE_WISHLIST_PRICE + " text,"
                + DatabaseConstant.TABLE_WISHLIST_ORDER_QUANTITY + " text,"
                + DatabaseConstant.TABLE_WISHLIST_QUANTITY_TYPE + " text,"
                + DatabaseConstant.TABLE_WISHLIST_MIN_QUAITITY + " text,"
                + DatabaseConstant.TABLE_WISHLIST_AVAILABILITY + " text,"
                + DatabaseConstant.TABLE_WISHLIST_DISCOUNT + " text,"
                + DatabaseConstant.TABLE_WISHLIST_IMAGE + " text,"
                + DatabaseConstant.TABLE_WISHLIST_RATING + " text,"
                + DatabaseConstant.TABLE_WISHLIST_DESCRIPTION + " text,"
                + DatabaseConstant.TABLE_WISHLIST_TYPE + " text,"
                + DatabaseConstant.TABLE_WISHLIST_QUANTITY + " INTEGER)");

        db.execSQL("Create table " + DatabaseConstant.TABLE_NAME_CART + "(" + DatabaseConstant.TABLE_CART_ID + " text,"
                + DatabaseConstant.TABLE_CART_TITLE + " text,"
                + DatabaseConstant.TABLE_CART_PRICE + " text,"
                + DatabaseConstant.TABLE_CART_ORDER_QUANTITY + " text,"
                + DatabaseConstant.TABLE_CART_QUANTITY_TYPE + " text,"
                + DatabaseConstant.TABLE_CART_MIN_QUAITITY + " text,"
                + DatabaseConstant.TABLE_CART_AVAILABILITY + " text,"
                + DatabaseConstant.TABLE_CART_DISCOUNT + " text,"
                + DatabaseConstant.TABLE_CART_IMAGE + " text,"
                + DatabaseConstant.TABLE_CART_RATING + " text,"
                + DatabaseConstant.TABLE_CART_DESCRIPTION + " text,"
                + DatabaseConstant.TABLE_CART_TYPE + " text,"
                + DatabaseConstant.TABLE_CART_QUANTITY + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
