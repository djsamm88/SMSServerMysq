package pakpak.kominfo.smsservermysql;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by dinaskominfokab.pakpakbharat on 22/05/18.
 */

public class Intro extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);

        Button btnLanjutkan = (Button)findViewById(R.id.btnLanjutkan);
        btnLanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intro.this,SignInActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

}
