package dlmbg.pckg.crud.category;

import java.io.File;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Gede Lumbung - 2013
 * http://gedelumbung.com
 * Just Simple Android CRUD App with Parent Child Content
 */

public class MainActivity extends ListActivity {
	private SqliteManager sqliteDB;
	private SimpleCursorAdapter mCursorAdapter;
	private EditText cari_et;

	SessionManager session;
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);
        sqliteDB = new SqliteManager(this);
        sqliteDB.bukaKoneksi();
        
        File direktori_gambar = new File(Environment.getExternalStorageDirectory().toString()+"/app_image_penemu/");
        direktori_gambar.mkdirs();

		Cursor cursor = sqliteDB.bacaDataKategori();

		startManagingCursor(cursor);
		
		cari_et = (EditText) findViewById(R.id.cari_penemu);

		String[] awal = new String[] { "kategori" };
		int[] tujuan = new int[] { R.id.rowtext };
		mCursorAdapter = new SimpleCursorAdapter(this, R.layout.baris, cursor, awal, tujuan);

		mCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if (columnIndex == SqliteManager.POSISI_ID) {
					TextView textView = (TextView) view;
						textView.setText("");
					return true;
			    }
			    return false;
			}
			});

		setListAdapter(mCursorAdapter);

		session = new SessionManager(getApplicationContext());

		if (session.isLoggedIn() == true) 
		{
			registerForContextMenu(getListView());
		}	
		
		String cek = "ok";
		if(cek=="asu")
		{
		}
		
		Button button = (Button) findViewById(R.id.btn_cari_penemu);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
	    		CariData();
				finish();
			}
		});

    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sqliteDB.tutupKoneksi();
	}

    public boolean onCreateOptionsMenu(Menu menu) {
    	if (session.isLoggedIn() == true) 
		{
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.opt_menu, menu);
		}
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case R.id.tambah_kategori:
        		Intent intent = new Intent(this, TambahKategori.class);
        		intent.putExtra("kategori", "");
        		startActivity(intent);
        		return true;
        	default:
        		return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
			case R.id.menu_edit:
				startDetail(info.id, false);
				return true;

			case R.id.menu_delete:
				hapus(info.id);
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}

    @SuppressWarnings("deprecation")
	public void hapus(long rowId) {
    	sqliteDB.hapusData(rowId, "tbl_kategori", "_id");
		mCursorAdapter.getCursor().requery();
    }

	public static final String EXTRA_ROWID = "rowid";

	@Override
	protected void onListItemClick(ListView l, View v, int position, long rowId) {
		super.onListItemClick(l, v, position, rowId);
		tampilTempatTerseleksi(rowId);

	}

	public void tampilTempatTerseleksi(Long mRowId) {
		Cursor cursor = sqliteDB.bacaDataTerseleksiKategori(mRowId);
		Intent intent = new Intent(this, DaftarPenemu.class);
		intent.putExtra("id_kategori", cursor.getString(0));
		Toast.makeText(this, cursor.getString(1),Toast.LENGTH_SHORT).show();
		startActivity(intent);

	}

	public void startDetail(long rowId, boolean baru) {
		Intent intent = new Intent(this, TambahKategori.class);
		if (!baru) {
			intent.putExtra(EXTRA_ROWID, rowId);
		}
		startActivity(intent);
	}

	public void CariData() {
		Intent intent = new Intent(this, CariPenemu.class);
		intent.putExtra("cari_data", cari_et.getText().toString());
		startActivity(intent);
	}
}
