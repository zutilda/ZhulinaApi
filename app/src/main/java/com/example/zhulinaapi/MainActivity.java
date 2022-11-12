package com.example.zhulinaapi;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    EditText FindTreiner;
    ListView listView;
    List<Mask> lvdays;
    AdapterMask pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener((view -> startActivity(new Intent(
                MainActivity.this, AddData.class))));

        FindTreiner = findViewById(R.id.FindTreiner);
        FindTreiner.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                sort(spinner.getSelectedItemPosition());
            }
        });
        listView = findViewById(R.id.lvData);
        lvdays = new ArrayList<>();
        pAdapter = new AdapterMask(MainActivity.this, lvdays);
        listView.setAdapter(pAdapter);
        listView.setOnItemClickListener((arg0, arg1, position, arg3) -> {
                    Intent intent = new Intent(MainActivity.this, Change.class);
                    intent.putExtra("id", Integer.parseInt(String.valueOf(arg3)));
                    intent.putExtra("day", lvdays.get(position).getday());
                    intent.putExtra("wotkout", lvdays.get(position).getwotkout());
                    intent.putExtra("trainer", lvdays.get(position).gettrainer());
                    intent.putExtra("image", lvdays.get(position).getImage());
                    startActivity(intent);
        });
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                sort(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }
        });



        new Getdays().execute();

        try {
            TimeUnit.MILLISECONDS.sleep(200); // Без этого запускается пустой
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sort(int position) {

        List<Mask> list = new ArrayList<>();
        if (FindTreiner.getText().equals(null)) {
            list.addAll(lvdays);
        } else {
            for (Mask item : lvdays) {
                if (item.gettrainer().contains(FindTreiner.getText())) {
                    list.add(item);
                }
            }
        }

        switch (position) {

            case 1:
                SortByday sp = new SortByday();
                Collections.sort(list, sp);
                break;

            case 2:
                SortBywotkout sq = new SortBywotkout();
                Collections.sort(list, sq);
                break;

            case 3:
                SortBytrainer sc = new SortBytrainer();
                Collections.sort(list, sc);
                break;
        }

        pAdapter = new AdapterMask(MainActivity.this, list);
        listView.setAdapter(pAdapter);
    }

    private class Getdays extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL("https://ngknn.ru:5001/NGKNN/ЖулинаАА/api/Workouts");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();

            } catch (Exception exception) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            try {
                JSONArray tempArray = new JSONArray(s);
                for (int i = 0; i < tempArray.length(); i++) {

                    JSONObject productJson = tempArray.getJSONObject(i);
                    Mask tempday = new Mask(
                            productJson.getInt("id"),
                            productJson.getString("day"),
                            productJson.getString("wotkout"),
                            productJson.getString("trainer"),
                            productJson.getString("image")
                    );
                    lvdays.add(tempday);
                    pAdapter.notifyDataSetInvalidated();
                }
            } catch (Exception ignored) {

            }
        }
    }
}