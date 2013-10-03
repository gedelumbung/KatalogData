package dlmbg.pckg.crud.category;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * Gede Lumbung - 2013
 * http://gedelumbung.com
 * Just Simple Android CRUD App with Parent Child Content
 */

public class SqliteManager {
	public static final int VERSI_DATABASE= 1;
	public static final String NAMA_DATABASE = "Penemu";
	public static final int POSISI_ID = 0;

	public static final String[] FIELD_TABEL_KATEGORI ={"_id","kategori"};
	public static final String[] FIELD_TABEL_PENEMU ={"_id", "id_kategori", "nama_penemu", "kelahiran", "gambar", "keterangan"};

	private Context crudContext;
	private SQLiteDatabase crudDatabase;
	private SqliteManagerHelper crudHelper;

	private static class SqliteManagerHelper extends SQLiteOpenHelper {

		private static final String TABEL_KATEGORI =
			"CREATE TABLE IF NOT EXISTS tbl_kategori (" +
			"_id integer primary key autoincrement," +
			"kategori text NOT NULL)";

		private static final String TABEL_PENEMU =
			"CREATE TABLE IF NOT EXISTS tbl_penemu (" +
			"_id integer primary key autoincrement," +
			"id_kategori text NOT NULL," +
			"nama_penemu text NOT NULL," +
			"kelahiran text NOT NULL," +
			"gambar text NOT NULL," +
			"keterangan text NOT NULL)";
		public SqliteManagerHelper(Context context) {
			super(context, NAMA_DATABASE, null, VERSI_DATABASE);
		}

		@Override
		public void onCreate(SQLiteDatabase database) {
			database.execSQL(TABEL_KATEGORI);
			database.execSQL(TABEL_PENEMU);
		}

		@Override
		public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {}
	}

	public SqliteManager(Context context) {
		crudContext = context;
	}

	public void bukaKoneksi() throws SQLException {
		crudHelper = new SqliteManagerHelper(crudContext);
		crudDatabase = crudHelper.getWritableDatabase();
	}

	public void tutupKoneksi() {
		crudHelper.close();
		crudHelper = null;
		crudDatabase = null;
	}

	public long insertData(ContentValues values, String nama_tabel) {
		return crudDatabase.insert(nama_tabel, null, values);
	}

	public boolean updateData(long rowId, ContentValues values, String nama_tabel, String id_param) {
		return crudDatabase.update(nama_tabel, values,
				id_param + "=" + rowId, null) > 0;
	}

	public boolean hapusData(long rowId, String nama_tabel, String id_param) {
		return crudDatabase.delete(nama_tabel,
				id_param + "=" + rowId, null) > 0;
	}

	public Cursor bacaDataKategori() {
		return crudDatabase.query("tbl_kategori",FIELD_TABEL_KATEGORI,null, null,null, null,"kategori DESC");
	}

	public Cursor bacaDataPenemu(String rowId) {
		
		return crudDatabase.query("tbl_penemu", FIELD_TABEL_PENEMU, "id_kategori =" + rowId, null, null, null, null);
	}

	public Cursor bacaDataPencarianPenemu(String cari) {
		
		return crudDatabase.query("tbl_penemu", FIELD_TABEL_PENEMU, "nama_penemu like '%"+cari+"%'", null, null, null, null);
	}

	public Cursor bacaDataTerseleksiKategori(long rowId) throws SQLException {
		Cursor cursor = crudDatabase.query(true, "tbl_kategori",FIELD_TABEL_KATEGORI,"_id =" + rowId,null, null, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}

	public Cursor bacaDataTerseleksiPenemu(long rowId) throws SQLException {
		Cursor cursor = crudDatabase.query(true, "tbl_penemu",FIELD_TABEL_PENEMU,"_id =" + rowId,null, null, null, null, null);
		cursor.moveToFirst();
		return cursor;
	}

	public ContentValues ambilDataKategori(String kategori) {
		ContentValues values = new ContentValues();
		values.put("kategori", kategori);
		return values;
	}

	public ContentValues ambilDataPenemu(String id_kategori, String nama_penemu, String kelahiran, String gambar, String keterangan) {
		ContentValues values = new ContentValues();
		values.put("id_kategori", id_kategori);
		values.put("nama_penemu", nama_penemu);
		values.put("kelahiran", kelahiran);
		values.put("gambar", gambar);
		values.put("keterangan", keterangan);
		return values;
	}
	
	public List<String> getAllLabels(){
        List<String> labels = new ArrayList<String>();
 
        String selectQuery = "SELECT * FROM tbl_kategori";
 
        Cursor cursor = crudDatabase.rawQuery(selectQuery, null);
 
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(0)+" | "+cursor.getString(1));
            } while (cursor.moveToNext());
        }
 
        cursor.close();
 
        return labels;
    }
}
