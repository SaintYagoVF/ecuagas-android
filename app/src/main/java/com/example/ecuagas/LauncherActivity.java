package com.example.ecuagas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class LauncherActivity extends AppCompatActivity {


    //SharedPreference
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Rol = "rolKey";

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        //SharedPreferences

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if(sharedpreferences.getString(Rol,"")==""){
            startActivity(new Intent(LauncherActivity.this, InicioActivity.class));

        }else if(sharedpreferences.getString(Rol,"").equals("Usuario")){
            startActivity(new Intent(LauncherActivity.this, MainActivity.class));

        }else if(sharedpreferences.getString(Rol,"").equals("Distribuidor")) {
            startActivity(new Intent(LauncherActivity.this, MainActivityDistribuidor.class));
        }

        finish();
    }
}
