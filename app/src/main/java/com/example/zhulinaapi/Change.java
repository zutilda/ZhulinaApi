package com.example.zhulinaapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Change extends AppCompatActivity implements View.OnClickListener {

    TextView txtday;
    TextView txtwotkout;
    TextView txttrainer;
    ImageView imageView;
    String image;
    int Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener((view -> {
            Intent intent = new Intent(Change.this, MainActivity.class);
            startActivity(intent);
        }));

        Button btnSafe = findViewById(R.id.btnSafe);
        btnSafe.setOnClickListener(this);

        Button btnDel = findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);



        txtday = findViewById(R.id.day);
        txtday.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                txtday.setHint(null);
            else
                txtday.setHint(R.string.day);
        });

        txtwotkout = findViewById(R.id.wotkout);
        txtwotkout.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                txtwotkout.setHint(null);
            else
                txtwotkout.setHint(R.string.workout);
        });

        txttrainer = findViewById(R.id.trainer);
        txttrainer.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                txttrainer.setHint(null);
            else
                txttrainer.setHint(R.string.trainer);
        });

        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImg.launch(intent);
        });

        setData();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSafe:

                String day = txtday.getText().toString();
                String wotkout = txtwotkout.getText().toString();
                String trainer = txttrainer.getText().toString();

                putData(Id, day, wotkout, trainer, image);
                break;

            case R.id.btnDel:

                deleteData(Id);
                new Handler().postDelayed(() -> startActivity(
                        new Intent(Change.this, MainActivity.class)), 200);
                break;


        }
    }
    private void setData() {
        Bundle arg = getIntent().getExtras();
        Id = arg.getInt("id");
        txtday.setText(arg.getString("day"));
        txtwotkout.setText(arg.getString("wotkout"));
        txttrainer.setText(arg.getString("trainer"));
        image = arg.getString("image");
        imageView.setImageBitmap(getImgBitmap(image));
    }

    private void putData(int Id, String day, String wotkout, String trainer, String image) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ЖулинаАА/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Mask mask = new Mask(Id, day, wotkout,
                trainer, image);

        Call<Mask> call = retrofitAPI.updateData(Id, mask);

        call.enqueue(new Callback<Mask>() {
            @Override
            public void onResponse(Call<Mask> call, Response<Mask> response) {
                Toast.makeText(Change.this, "Изменение зафиксировано", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Mask> call, Throwable t) {
                Toast.makeText(Change.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteData(int id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ЖулинаАА/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<Mask> call = retrofitAPI.deleteData(id);

        call.enqueue(new Callback<Mask>() {
            @Override
            public void onResponse(Call<Mask> call, Response<Mask> response) {
                Toast.makeText(Change.this, "Информация удалена", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Mask> call, Throwable t) {
                Toast.makeText(Change.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public static String encodeImage(Bitmap bitmap) {
        int prevW = 500;
        int prevH = bitmap.getHeight() * prevW / bitmap.getWidth();

        Bitmap b = Bitmap.createScaledBitmap(bitmap, prevW, prevH, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(bytes);
        }
        return "";
    }
    private Bitmap getImgBitmap(String encodedImg) {
        if (!encodedImg.equals("null")) {
            byte[] bytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                bytes = Base64.getDecoder().decode(encodedImg);
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        return BitmapFactory.decodeResource(getResources(),
                R.drawable.photo);
    }


    public final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imageView.setImageBitmap(bitmap);
                    image = encodeImage(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });
}