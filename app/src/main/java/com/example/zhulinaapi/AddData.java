package com.example.zhulinaapi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

public class AddData extends AppCompatActivity implements View.OnClickListener {
    TextView txtday;
    TextView txtwotkout;
    TextView txttrainer;
    ImageView imageView;
    String image;
Mask mask1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener((view -> {
            Intent intent = new Intent(AddData.this, MainActivity.class);
            startActivity(intent);
        }));

        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

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
    }

    @Override
    public void onClick(View v) {
        String day = txtday.getText().toString();
        String wotkout = txtwotkout.getText().toString();
        String trainer = txttrainer.getText().toString();

        switch (v.getId()) {

            case R.id.btnAdd:
                postData(day, wotkout, trainer, image);
        }
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
    private void postData(String day, String wotkout, String trainer, String image) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/ngknn/ЖулинаАА/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Mask mask = new Mask(day, wotkout,
                trainer, image);

        Call<Mask> call = retrofitAPI.createPost(mask);

        call.enqueue(new Callback<Mask>() {
            @Override
            public void onResponse(Call<Mask> call, Response<Mask> response) {
                Toast.makeText(AddData.this, "Товар успешно добавлен", Toast.LENGTH_LONG).show();
                txtday.setText("");
                txtday.clearFocus();
                txtwotkout.setText("");
                txtwotkout.clearFocus();
                txttrainer.setText("");
                txttrainer.clearFocus();
            }

            @Override
            public void onFailure(Call<Mask> call, Throwable t) {
                Toast.makeText(AddData.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
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