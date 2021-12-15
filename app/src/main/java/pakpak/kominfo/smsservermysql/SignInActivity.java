package pakpak.kominfo.smsservermysql;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import pakpak.kominfo.smsservermysql.database.DbUser;
import pakpak.kominfo.smsservermysql.model.ModelDaftar;
import pakpak.kominfo.smsservermysql.model.ModelUser;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by dinaskominfokab.pakpakbharat on 09/04/18.
 */

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 0 ;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private com.google.android.gms.common.SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    private Button signOutButton;
    private TextView nameTextView;
    private TextView emailTextView;

    String string_id_member;

    public boolean isFirstStart;


    DatabaseReference mFirebaseDatabase;
    FirebaseDatabase mFirebaseInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        /************************* bikin intro ******************************/
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Intro App Initialize SharedPreferences
                SharedPreferences getSharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                isFirstStart = getSharedPreferences.getBoolean("firstStart", true);

                //  Check either activity or app is open very first time or not and do action

                if (isFirstStart)
                {

                    //  Launch application introduction screen
                    Intent i = new Intent(SignInActivity.this, Intro.class);
                    startActivity(i);
                    SharedPreferences.Editor e = getSharedPreferences.edit();
                    e.putBoolean("firstStart", false);
                    e.apply();
                }

                /*
                Intent i = new Intent(SignInActivity.this, Intro.class);
                startActivity(i);
                SharedPreferences.Editor e = getSharedPreferences.edit();
                e.putBoolean("firstStart", false);
                e.apply();
                */


            }
        });
        t.start();
        /************************* bikin intro ******************************/



        signInButton = (com.google.android.gms.common.SignInButton)findViewById(R.id.sign_in_button);
        signOutButton = (Button)findViewById(R.id.sign_out_button);
        nameTextView = (TextView)findViewById(R.id.name_text_view);
        emailTextView = (TextView)findViewById(R.id.email_text_view);

        signOutButton.setVisibility(View.GONE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(SignInActivity.this,"Coba lagi.",Toast.LENGTH_SHORT).show();
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                signInButton.setVisibility(View.GONE);
                signOutButton.setVisibility(View.VISIBLE);
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    if(user.getDisplayName() != null)
                        nameTextView.setText("Selamat datang " + user.getDisplayName().toString());
                        emailTextView.setText(user.getEmail().toString());




                    /*
                    eksekusi disini berhasil login...
                    */

                    // Instantiate the RequestQueue.


                    String eMail = user.getEmail().toString();
                    String emailnya;
                    try {
                        emailnya = URLEncoder.encode(eMail, "UTF-8");
                    }catch (Exception e)
                    {
                        emailnya ="";
                    }

                    int id_member =1;
                    DbUser dbUser = new DbUser(SignInActivity.this);

                    //insert public ModelUser(int id_member, String nama_member, String telepon, String email, String photoURL)

                    //int id_member = Integer.parseInt(string_id_member);

                    try{
                        dbUser.insert(new ModelUser(id_member,user.getDisplayName().toString(),"",user.getEmail().toString(),user.getPhotoUrl().toString()));
                    }catch (Exception e)
                    {

                        Log.d("exception updatenya1",e.toString());
                        try{
                            dbUser.update_profil_user_aja(id_member,user.getDisplayName().toString(),"",user.getEmail().toString(),user.getPhotoUrl().toString(),"Update terbaru");
                        }catch (Exception x)
                        {
                            Log.d("exception updatenya2",x.toString());
                        }

                    }




                    Intent member_page = new Intent(SignInActivity.this,MainActivity.class);
                    member_page.putExtra("email",user.getEmail());
                    startActivity(member_page);
                    finish();


                    /*
                    final String api = "https://smsfirebaseproject-d76df.firebaseio.com/";
                    final String urlnya = api+"data_user/"+user.getDisplayName()+".json";
                    //final String urlnya = api+"054f2c3a1fe4eb1eb0e5f21ab8f92c64.json";

                    // prepare the Request
                    RequestQueue queue = Volley.newRequestQueue(SignInActivity.this);
                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                            urlnya, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                            Log.d("inicek_email",urlnya);
                            System.out.println("jumlah:"+response.length());

                            if(response.length() > 0)
                            {
                                try {

                                    Log.d("hasil",response.toString());
                                    System.out.println(response.get("email"));

                                    if(response.get("status").equals("non_aktif"))
                                    {
                                        Toast.makeText(SignInActivity.this,"Hai "+user.getDisplayName().toString()+" Anda telah terdaftar.. dan belum disetujui.",Toast.LENGTH_SHORT).show();
                                    }else{
                                        Intent member_page = new Intent(SignInActivity.this,MainActivity.class);
                                        member_page.putExtra("email",user.getEmail());
                                        startActivity(member_page);
                                        finish();
                                    }

                                }catch (Exception e)
                                {
                                    Log.d("errornya","aaa-"+e.toString());
                                }
                            }else {


                            }


                        }
                    },
                            new Response.ErrorListener()
                            {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("Error.Response",error.toString());


                                    mFirebaseInstance = FirebaseDatabase.getInstance();
                                    mFirebaseDatabase = mFirebaseInstance.getReference("data_user/"+user.getDisplayName().toString());
                                    mFirebaseDatabase.setValue(new ModelDaftar(1,user.getDisplayName(),user.getPhoneNumber(),user.getEmail(),user.getPhotoUrl().toString(),"non_aktif"));
                                    Toast.makeText(SignInActivity.this,"Anda telah terdaftar.. dan belum disetujui.",Toast.LENGTH_SHORT).show();



                                }
                            }
                    );

                    // add it to the RequestQueue
                    queue.add(jsonObjReq);

                    */





                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                signInButton.setVisibility(View.VISIBLE);
                                signOutButton.setVisibility(View.GONE);
                                emailTextView.setText(" ".toString());
                                nameTextView.setText(" ".toString());
                            }
                        });
            }
            // ..
        });

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                Log.d("LOGIN","Login gagal");
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
