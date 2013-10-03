package dlmbg.pckg.crud.category;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/*
 * Gede Lumbung - 2013
 * http://gedelumbung.com
 * Just Simple Android CRUD App with Parent Child Content
 */

public class TambahKategori extends Activity {
	private SqliteManager sqliteDB;
	private Long id;

	private EditText teks_judul;
	String judul;

	public static final String SIMPAN_DATA = "simpan";

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		setContentView(R.layout.simpan);

		teks_judul = (EditText) findViewById(R.id.edit_judul);

		id = null;

		if (bundle == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null && extras.containsKey(MainActivity.EXTRA_ROWID)) {
				id = extras.getLong(MainActivity.EXTRA_ROWID);
			}
			else
			{
				judul = extras.getString("judul");
			}
		}

		sqliteDB = new SqliteManager(this);
		sqliteDB.bukaKoneksi();

		pindahData();

		Button button = (Button) findViewById(R.id.btn_simpan);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				simpan();
				finish();
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sqliteDB.tutupKoneksi();
	}

	private void pindahData() {
		if (id != null) {
			Cursor cursor = sqliteDB.bacaDataTerseleksiKategori(id);
			teks_judul.setText(cursor.getString(1));
			cursor.close();
		}
	}

	private void simpan() {
		String kategori = teks_judul.getText().toString();

		if (id != null) {
			sqliteDB.updateData(id, sqliteDB.ambilDataKategori(kategori),"tbl_kategori","_id");
		}
		else {
			id = sqliteDB.insertData(sqliteDB.ambilDataKategori(kategori),"tbl_kategori");
		}
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putLong(SIMPAN_DATA, id);
	}
}
