package dlmbg.pckg.crud.category;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

/*
 * Gede Lumbung - 2013
 * http://gedelumbung.com
 * Just Simple Android CRUD App with Parent Child Content
 */

public class TambahPenemu extends Activity{
	private Uri mImageCaptureUri;
	private ImageView mImageView;	
	
	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;
	
	private SqliteManager sqliteDB;
	private Long id;

	private EditText nama_penemu_et, kelahiran_et, keterangan_et, gambar_et;
	private Spinner kategori_sp;
	
	String nama_penemu,kelahiran,keterangan,gambar;
	public static final String SIMPAN_DATA = "simpan";

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		setContentView(R.layout.simpan_penemu);

		nama_penemu_et = (EditText) findViewById(R.id.edit_penemu);
		kelahiran_et = (EditText) findViewById(R.id.edit_kelahiran);
		keterangan_et = (EditText) findViewById(R.id.edit_keterangan);
		gambar_et = (EditText) findViewById(R.id.edit_gambar);
		kategori_sp = (Spinner) findViewById(R.id.spinner_penemu);
		
		mImageView = (ImageView) findViewById(R.id.iv_pic);
		

		id = null;

		if (bundle == null) {
			Bundle extras = getIntent().getExtras();
			if (extras != null && extras.containsKey(MainActivity.EXTRA_ROWID)) {
				id = extras.getLong(MainActivity.EXTRA_ROWID);
			}
			else
			{
				nama_penemu = extras.getString("nama_penemu");
				kelahiran = extras.getString("kelahiran");
				keterangan = extras.getString("keterangan");
				gambar = extras.getString("gambar");
			}
		}

		sqliteDB = new SqliteManager(this);
		sqliteDB.bukaKoneksi();

		pindahData();
		loadSpinnerData();

		Button button = (Button) findViewById(R.id.btn_simpan_penemu);
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				simpan();
				finish();
			}
		});
		
		
		final String [] items			= new String [] {"From Camera", "From SD Card"};				
		ArrayAdapter<String> adapter	= new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder		= new AlertDialog.Builder(this);
		
		builder.setTitle("Select Image");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int item ) {
				if (item == 0) {
					Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file		 = new File(Environment.getExternalStorageDirectory().toString(),
							   			"app_image_penemu/tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
					mImageCaptureUri = Uri.fromFile(file);

					try {			
						intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
						intent.putExtra("return-data", true);
						
						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (Exception e) {
						e.printStackTrace();
					}			
					
					dialog.cancel();
				} else {
					Intent intent = new Intent();
					
	                intent.setType("image/*");
	                intent.setAction(Intent.ACTION_GET_CONTENT);
	                
	                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
				}
			}
		} );
		
		final AlertDialog dialog = builder.create();
		
		((Button) findViewById(R.id.btn_choose)).setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});

	}
	
   @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode != RESULT_OK) return;
	   
		Bitmap bitmap 	= null;
		String path		= "";
		
		if (requestCode == PICK_FROM_FILE) {
			mImageCaptureUri = data.getData(); 
			path = getRealPathFromURI(mImageCaptureUri); //from Gallery 
		
			if (path == null)
				path = mImageCaptureUri.getPath(); //from File Manager
			
			if (path != null) 
				bitmap 	= BitmapFactory.decodeFile(path);
		} else {
			path	= mImageCaptureUri.getPath();
			bitmap  = BitmapFactory.decodeFile(path);
		}
		gambar_et.setText(path);
		mImageView.setImageBitmap(bitmap);		
	}
	
	public String getRealPathFromURI(Uri contentUri) {
        String [] proj 		= {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
		Cursor cursor 		= managedQuery( contentUri, proj, null, null,null);
        
        if (cursor == null) return null;
        
        int column_index 	= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        
        cursor.moveToFirst();

        return cursor.getString(column_index);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sqliteDB.tutupKoneksi();
	}

	private void pindahData() {
		if (id != null) {
			Cursor cursor = sqliteDB.bacaDataTerseleksiPenemu(id);
			nama_penemu_et.setText(cursor.getString(2));
			kelahiran_et.setText(cursor.getString(3));
			gambar_et.setText(cursor.getString(4));
			keterangan_et.setText(cursor.getString(5));
			Bitmap bitmap = BitmapFactory.decodeFile(cursor.getString(4));
			mImageView.setImageBitmap(bitmap);
			cursor.close();
		}
	}

	private void simpan() {
		String nama_penemu_save = nama_penemu_et.getText().toString();
		String kelahiran_save = kelahiran_et.getText().toString();
		String keterangan_save = keterangan_et.getText().toString();
		String gambar_save = gambar_et.getText().toString();
		String kategori = kategori_sp.getSelectedItem().toString();
		String[] split_kategori = kategori.split(" | ");
		String id_kategori  = split_kategori[0];
		

		if (id != null) {
			Toast.makeText(this, gambar_save,Toast.LENGTH_SHORT).show();
			sqliteDB.updateData(id, sqliteDB.ambilDataPenemu(id_kategori, nama_penemu_save, kelahiran_save, gambar_save, keterangan_save),"tbl_penemu","_id");
		}
		else {
			sqliteDB.insertData(sqliteDB.ambilDataPenemu(id_kategori, nama_penemu_save, kelahiran_save, gambar_save, keterangan_save),"tbl_penemu");
		}
	}
	
	 private void loadSpinnerData() {
        List<String> lables = sqliteDB.getAllLabels();
 
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lables);
 
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 
        kategori_sp.setAdapter(dataAdapter);
    }
}
