package dlmbg.pckg.crud.category;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * Gede Lumbung - 2013
 * http://gedelumbung.com
 * Just Simple Android CRUD App with Parent Child Content
 */

public class DetailPenemu extends Activity{

	private TextView nama_penemu_et, kelahiran_et, keterangan_et;
	private ImageView gambar_iv;
	
	String nama_penemu,kelahiran,keterangan,gambar;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		setContentView(R.layout.detail);

		nama_penemu_et = (TextView) findViewById(R.id.nama_penemu_detail);
		kelahiran_et = (TextView) findViewById(R.id.kelahiran_detail);
		keterangan_et = (TextView) findViewById(R.id.keterangan_detail);
		gambar_iv = (ImageView) findViewById(R.id.gambar_detail);
		
		Bundle extras = getIntent().getExtras();
		nama_penemu = extras.getString("nama_penemu");
		kelahiran = extras.getString("kelahiran");
		keterangan = extras.getString("keterangan");
		gambar = extras.getString("gambar");

		nama_penemu_et.setText(nama_penemu);
		kelahiran_et.setText(kelahiran);
		keterangan_et.setText(keterangan);
		
		Bitmap bmImg = BitmapFactory.decodeFile(gambar);
		gambar_iv.setImageBitmap(bmImg);
		
		String imageInSD = gambar;      
		Bitmap bitmap = BitmapFactory.decodeFile(imageInSD);
		gambar_iv.setImageBitmap(bitmap);
		

	}

}
