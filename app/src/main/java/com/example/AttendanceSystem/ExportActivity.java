package com.example.AttendanceSystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExportActivity extends AppCompatActivity {

    ImageButton backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);

        backbtn = findViewById(R.id.btn_back);
        backbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });

    }

    public void export(View view){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ExportActivity.this);
        Gson gson = new Gson();
        String json = prefs.getString("namelist", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        List<String> namelists  = gson.fromJson(json, type);
        Collections.reverse(namelists);
        String name = namelists.toString().replaceAll("(^\\[|\\]$)", "");

        String json1 = prefs.getString("datelist", null);
        Type type1 = new TypeToken<ArrayList<String>>() {}.getType();
        List<String> datelists  = gson.fromJson(json1, type1);
        String date = datelists.toString().replaceAll("(^\\[|\\]$)", "");

        String json2 = prefs.getString("attendancerate", null);
        Type type2 = new TypeToken<ArrayList<String>>() {}.getType();
        List<String> attendancerate  = gson.fromJson(json2, type2);
        String rate = attendancerate.toString().replaceAll("(^\\[|\\]$)", "");

        //String[] separated0 = name.split("\\.");
        //String[] separated1 = date.split("\\.");
        //String[] separated2 = rate.split("\\.");


        System.out.println(name);
        System.out.println(date);
        System.out.println(rate);

        //generate data
        StringBuilder data = new StringBuilder();
        data.append("Student");
        data.append(name);
        data.append("\n\nAttendance Date");
        data.append(date);
        data.append("\n\nAttendance Rate");
        data.append(rate);

        try{
            //saving the file into device
            FileOutputStream out = openFileOutput("data.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

            //exporting
            Context context = getApplicationContext();
            File filelocation = new File(getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.AttendanceSystem.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            startActivity(Intent.createChooser(fileIntent, "Send mail"));
        }
        catch(Exception e){
            e.printStackTrace();
        }


    }
}