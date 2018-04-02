package plantkiller.wayne.com.plantskiller2099.data.model;

import android.database.Cursor;

import plantkiller.wayne.com.plantskiller2099.data.database.TreeDatabase;

public class TreeData {
    private String mTreeName;
    private double mLong;
    private double mLat;
    private int mStatus;
    private String mDes;
    private int mDbId;

    public TreeData(String treeName, double lat, double aLong, int status, String des) {
        mTreeName = treeName;
        mLong = aLong;
        mLat = lat;
        mStatus = status;
        mDes = des;
    }

    public TreeData(Cursor cursor) {
        this.mStatus = cursor.getInt(cursor.getColumnIndex(TreeDatabase.TreeEntry.COLUMN_STATUS));
        this.mTreeName =
            cursor.getString(cursor.getColumnIndex(TreeDatabase.TreeEntry.COLUMN_NAME));
        this.mDes = cursor.getString(cursor.getColumnIndex(TreeDatabase.TreeEntry.COLUMN_DES));
        this.mLong = cursor.getInt(cursor.getColumnIndex(TreeDatabase.TreeEntry
            .COLUMN_LONG));
        this.mLat = cursor.getInt(cursor.getColumnIndex(TreeDatabase.TreeEntry
            .COLUMN_LAT));
        this.mDbId = cursor.getInt(cursor.getColumnIndex(TreeDatabase.TreeEntry._ID));
    }

    public String getTreeName() {
        return mTreeName;
    }

    public void setTreeName(String treeName) {
        mTreeName = treeName;
    }

    public double getLong() {
        return mLong;
    }

    public void setLong(double aLong) {
        mLong = aLong;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public String getDes() {
        return mDes;
    }

    public void setDes(String des) {
        mDes = des;
    }

    public int getDbId() {
        return mDbId;
    }

    public void setDbId(int dbId) {
        mDbId = dbId;
    }
}
