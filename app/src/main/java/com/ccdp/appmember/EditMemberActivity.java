package com.ccdp.appmember;

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

import com.ccdp.appmember.api.MemberAPI;
import com.ccdp.appmember.model.Member;
import com.ccdp.appmember.model.MemberResults;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditMemberActivity extends AppCompatActivity {

    private EditText txtMemberName;
    private EditText txtMemberEmail;
    private Button btnSave;

    private Retrofit retrofit;
    private int id;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(EditMemberActivity.this);
        token = settings.getString("com.ccdp.appmember.token","");

        txtMemberName = (EditText) findViewById(R.id.member_name);
        txtMemberEmail = (EditText) findViewById(R.id.member_email);
        btnSave = (Button) findViewById(R.id.btn_save);

        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final MemberAPI memberAPI = retrofit.create(MemberAPI.class);
        Call<MemberResults> call = memberAPI.find(id,token);
        call.enqueue(new Callback<MemberResults>() {
            @Override
            public void onResponse(Call<MemberResults> call, Response<MemberResults> response) {
                try {
                    MemberResults results = response.body();
                    List<Member> mems = results.data;
                    Member member = mems.get(0);
                    Log.d("REQUEST = ",call.request().toString());
                    String strId = String.valueOf(member.getId());
                    txtMemberName.setText(member.getMemberName());
                    txtMemberEmail.setText(member.getMemberEmail());
                }catch (Exception e){
                    Toast.makeText(EditMemberActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MemberResults> call, Throwable t) {
                Toast.makeText(EditMemberActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Member> call = memberAPI.update(txtMemberName.getText().toString(),txtMemberEmail.getText().toString(),"2016-01-01","MALE","Islam","Bandung",id,token);
                call.enqueue(new Callback<Member>() {
                    @Override
                    public void onResponse(Call<Member> call, Response<Member> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(EditMemberActivity.this,"data berhasil diubah",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditMemberActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(EditMemberActivity.this,"Data gagal diubah",Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onFailure(Call<Member> call, Throwable t) {
                        Toast.makeText(EditMemberActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
