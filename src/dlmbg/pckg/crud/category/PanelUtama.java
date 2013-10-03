package dlmbg.pckg.crud.category;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
 * Gede Lumbung - 2013
 * http://gedelumbung.com
 * Just Simple Android CRUD App with Parent Child Content
 */

public class PanelUtama extends Activity {

	SessionManager session;
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.panel_utama);
        
 		Button tmb_browse = (Button) findViewById(R.id.btn_browse);
 		tmb_browse.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View view) {
        		StartBrowse();
 			}
 		});
        
 		Button tmb_dashboard = (Button) findViewById(R.id.btn_dashboard);
 		
 		session = new SessionManager(getApplicationContext());

		if (session.isLoggedIn() == true) 
		{
			tmb_dashboard.setText("Log Out");
		}	
		
 		tmb_dashboard.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View view) {
 				StartAdmin();
 			}
 		});
        
 		Button tmb_about = (Button) findViewById(R.id.btn_about);
 		tmb_about.setOnClickListener(new View.OnClickListener() {
 			@Override
 			public void onClick(View view) {
 				StartAbout();
 			}
 		});

    }
    
    public void StartBrowse() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
    
    public void StartAbout() {
		Intent intent = new Intent(this, About.class);
		startActivity(intent);
	}
    
    public void StartAdmin() {
    	if(session.isLoggedIn() == true)
    	{
    		session.logoutUser();
    		finish();
    	}
    	else
    	{
    		Intent intent = new Intent(this, Admin.class);
    		startActivity(intent);
    		finish();
    	}
	}
}
