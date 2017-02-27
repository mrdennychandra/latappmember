package com.ccdp.appmember;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ccdp.appmember.adapter.MemberAdapter;
import com.ccdp.appmember.api.MemberAPI;
import com.ccdp.appmember.api.UserAPI;
import com.ccdp.appmember.model.LoginResult;
import com.ccdp.appmember.model.Member;
import com.ccdp.appmember.model.MemberResults;
import com.ccdp.appmember.model.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Member> members;
    private static MemberAdapter adapter;
    Retrofit retrofit;
    MemberAPI memberAPI;
    UserAPI userAPI;
    private ProgressDialog mProgressDialog;
    String token,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        token = settings.getString("com.ccdp.appmember.token","");
        username = settings.getString("com.ccdp.appmember.username","");

        Log.d("MainActivity : ",username);

        listView = (ListView)findViewById(R.id.lvnotes);

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        memberAPI = retrofit.create(MemberAPI.class);
        Call<MemberResults> call = memberAPI.all(token);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        call.enqueue(new Callback<MemberResults>() {
            @Override
            public void onResponse(Call<MemberResults> call, Response<MemberResults> response) {
                try {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    members.clear();
                    MemberResults results = response.body();
                    List<Member> mems = results.data;
                    for(Member m : mems){
                        members.add(m);
                    }
                    adapter.notifyDataSetChanged();
                }catch (Exception e){
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MemberResults> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });

        if(members == null){
            members = new ArrayList<>();
        }

        adapter= new MemberAdapter(getApplicationContext(),R.layout.row_item,members);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Member member = members.get(position);
                Intent intent = new Intent(MainActivity.this,ViewMemberActivity.class);
                intent.putExtra("id",member.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent;

        switch (id){
            case R.id.action_add:
                intent = new Intent(MainActivity.this,AddMemberActivity.class);
                startActivity(intent);
                break;

            case R.id.action_logout:
                logout();
                break;

            case R.id.action_exit:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        userAPI = retrofit.create(UserAPI.class);
        Call<LoginResult> call = userAPI.logout(token);
        mProgressDialog = new ProgressDialog(this);
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
                    LoginResult result = response.body();
                    Log.d("MainActivity",result.message);
                    if(result.success){
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        settings.edit().remove("com.ccdp.appmember.token").commit();
                        settings.edit().remove("com.ccdp.appmember.isLogin").commit();
                        settings.edit().remove("com.ccdp.appmember.username").commit();

                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }catch (Exception e){
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                    Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                Toast.makeText(MainActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }

}
