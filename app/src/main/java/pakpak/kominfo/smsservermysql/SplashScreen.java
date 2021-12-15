package pakpak.kominfo.smsservermysql;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by dinaskominfokab.pakpakbharat on 10/03/18.
 */

public class SplashScreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                startActivity(new Intent(SplashScreen.this,MainActivity.class));
                //startActivity(new Intent(SplashScreen.this,BarangPopuler.class));
                finish();
            }
        },2000);

    }
}
