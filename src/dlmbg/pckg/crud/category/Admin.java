package dlmbg.pckg.crud.category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/*
 * Gede Lumbung - 2013
 * http://gedelumbung.com
 * Just Simple Android CRUD App with Parent Child Content
 */

public class Admin extends Activity {
	EditText username, password;
	SessionManager session;

    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.admin);
        
        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn() == false) {
        
			username = (EditText) findViewById(R.id.ed_username);
			password = (EditText) findViewById(R.id.ed_password);
			
	        Button button = (Button) findViewById(R.id.log_in_admin);
			button.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					masuk();
					finish();
				}
			});
        }
        else
        {
			Intent i = new Intent(getApplicationContext(),MainActivity.class);
			startActivity(i);
			finish();
        }

    }
    
    private void masuk() {
		String usr = username.getText().toString();
		String psw = password.getText().toString();
		
		if(usr.equals("admin") && psw.equals("admin"))
		{
			session.createLoginSession(usr,psw);
			Intent intent = new Intent(this, PanelUtama.class);
			startActivity(intent);
			finish();
		}
		else
		{
			Toast.makeText(this, "Username dan password tidak valid",Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, Admin.class);
			startActivity(intent);
			finish();
		}
	}
}
