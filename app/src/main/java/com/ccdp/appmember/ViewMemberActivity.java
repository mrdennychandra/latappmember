package com.ccdp.appmember;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ccdp.appmember.api.MemberAPI;
import com.ccdp.appmember.model.Member;
import com.ccdp.appmember.model.MemberResults;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ViewMemberActivity extends AppCompatActivity {

    private TextView txtId;
    private TextView txtMemberName;
    private TextView txtMemberEmail;
    private TextView txtMemberBirthDate;
    private TextView txtMemberSex;
    private TextView txtMemberReligion;
    private TextView txtMemberAddress;
    private Button btnDelete,btnEdit,btnPhoto;
    private int id;
    private Retrofit retrofit;
    private ImageView photo;
    File file;

    ProgressDialog mProgressDialog;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_view_member);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(ViewMemberActivity.this);
        token = settings.getString("com.ccdp.appmember.token","");

        txtId = (TextView) findViewById(R.id.id);
        txtMemberName = (TextView) findViewById(R.id.member_name);
        txtMemberEmail = (TextView) findViewById(R.id.member_email);
        txtMemberBirthDate = (TextView) findViewById(R.id.member_birthdate);
        txtMemberSex = (TextView) findViewById(R.id.member_sex);
        txtMemberReligion = (TextView) findViewById(R.id.member_religion);
        txtMemberAddress = (TextView) findViewById(R.id.member_address);
        btnDelete = (Button) findViewById(R.id.btn_delete);
        btnEdit = (Button) findViewById(R.id.btn_edit);
        btnPhoto = (Button) findViewById(R.id.btn_photo);
        photo = (ImageView) findViewById(R.id.photo);

        //ambil data terbaru dari server
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final MemberAPI memberAPI = retrofit.create(MemberAPI.class);
        Call<MemberResults> call = memberAPI.find(id,token);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        call.enqueue(new Callback<MemberResults>() {
            @Override
            public void onResponse(Call<MemberResults> call, Response<MemberResults> response) {
                try {
                    MemberResults results = response.body();
                    List<Member> mems = results.data;
                    Member member = mems.get(0);
                    Log.d("REQUEST = ",call.request().toString());
                    String strId = String.valueOf(member.getId());
                    txtId.setText(strId);
                    txtMemberName.setText(member.getMemberName());
                    txtMemberEmail.setText(member.getMemberEmail());
                    txtMemberBirthDate.setText(member.getMemberBirthDate());
                    txtMemberSex.setText(member.getMemberSex());
                    txtMemberReligion.setText(member.getMemberReligion());
                    txtMemberAddress.setText(member.getMemberAddress());
                    String photoUrl = Constants.BASE_ASSETS+member.getId()+".jpg";
                    Glide.with(getApplicationContext())
                            .load(photoUrl).error(R.mipmap.ic_launcher)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .into(photo);

                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }

                }catch (Exception e){
                    Toast.makeText(ViewMemberActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<MemberResults> call, Throwable t) {
                Toast.makeText(ViewMemberActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();

                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               AlertDialog.Builder builder = new AlertDialog.Builder(ViewMemberActivity.this);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        MemberAPI memberAPI = retrofit.create(MemberAPI.class);
                        Call<Member> call = memberAPI.delete(id,token);
                        Log.d("call request",call.request().toString());
                        call.enqueue(new Callback<Member>() {
                            @Override
                            public void onResponse(Call<Member> call, Response<Member> response) {
                                if(response.isSuccessful()){
                                    Toast.makeText(ViewMemberActivity.this,"data berhasil dihapus",Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }else{
                                    Toast.makeText(ViewMemberActivity.this,"Data gagal dihapus",Toast.LENGTH_SHORT);
                                }
                            }

                            @Override
                            public void onFailure(Call<Member> call, Throwable t) {
                                Toast.makeText(ViewMemberActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewMemberActivity.this,EditMemberActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(photoCaptureIntent, Constants.CAMERA_REQUEST_CODE);
            }
        });

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadFile(file);
            }
        });
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(ViewMemberActivity.this);
    }

    private void uploadFile(File file) {

        mProgressDialog.show();
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("gambar", file.getName(), requestFile);

        // finally, execute the request
        Log.d("BEGIN UPLOAD",file.getAbsolutePath());
        MemberAPI memberAPI = retrofit.create(MemberAPI.class);
        Call<ResponseBody> call = memberAPI.upload(id,body,token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                onBackPressed();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(Constants.CAMERA_REQUEST_CODE == requestCode && resultCode == RESULT_OK){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            photo.setImageBitmap(bitmap);
            photo.setDrawingCacheEnabled(true);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = Environment.getExternalStorageDirectory().toString();
            file = new File(path, id+".jpg");
            try {
                FileOutputStream out = new FileOutputStream(file.getAbsolutePath());
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
