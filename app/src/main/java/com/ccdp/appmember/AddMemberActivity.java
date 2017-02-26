package com.ccdp.appmember;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ccdp.appmember.api.MemberAPI;
import com.ccdp.appmember.model.Member;
import com.ccdp.appmember.model.MemberResults;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddMemberActivity extends AppCompatActivity {

    private EditText txtMemberName;
    private EditText txtMemberEmail;
    private Button btnSave;

    private Retrofit retrofit;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(AddMemberActivity.this);
        token = settings.getString("com.ccdp.appmember.token","");

        txtMemberName = (EditText) findViewById(R.id.member_name);
        txtMemberEmail = (EditText) findViewById(R.id.member_email);
        btnSave = (Button) findViewById(R.id.btn_save);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final MemberAPI memberAPI = retrofit.create(MemberAPI.class);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Member> call = memberAPI.insert(txtMemberName.getText().toString(),
                        txtMemberEmail.getText().toString(),
                        "2016-01-01","MALE","Islam","Bandung",token);
                call.enqueue(new Callback<Member>() {
                    @Override
                    public void onResponse(Call<Member> call, Response<Member> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(AddMemberActivity.this,"data berhasil disimpan",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddMemberActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(AddMemberActivity.this,"Data gagal dihapus",Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onFailure(Call<Member> call, Throwable t) {
                        Toast.makeText(AddMemberActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
