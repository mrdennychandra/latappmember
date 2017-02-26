package com.ccdp.appmember;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ccdp.appmember.api.UserAPI;
import com.ccdp.appmember.model.LoginResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsername,mPassword;
    private Button mButtonLogin;
    Retrofit retrofit;
    UserAPI userAPI;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //dapatkan shared preference isLogin
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        String isLogin = settings.getString("com.ccdp.appmember.isLogin","");
        //jika isLogin = 1 maka langsung alihkan ke MainAcitivity
        if(!isLogin.equals("")){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mButtonLogin = (Button) findViewById(R.id.button_login);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAPI = retrofit.create(UserAPI.class);
                Call<LoginResult> call = userAPI.login(mUsername.getText().toString(),mPassword.getText().toString());
                mProgressDialog = new ProgressDialog(LoginActivity.this);
                mProgressDialog.setIndeterminate(true);
                mProgressDialog.setMessage("Loading...");
                mProgressDialog.show();

                call.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        try {
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                            Log.d("LoginActivity",response.body().toString());
                            LoginResult result = response.body();
                            Log.d("LoginActivity",result.message);
                            if(result.success){
                                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("com.ccdp.appmember.token", result.token);
                                editor.putString("com.ccdp.appmember.isLogin", "1");
                                editor.putString("com.ccdp.appmember.username", mUsername.getText().toString());
                                editor.commit();
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(LoginActivity.this,result.message,Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            if (mProgressDialog.isShowing()) {
                                mProgressDialog.dismiss();
                            }
                            Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }
                    }
                });

            }
        });
    }
}
