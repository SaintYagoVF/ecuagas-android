package com.example.ecuagas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class InicioActivity extends AppCompatActivity {

    ImageView btnInicioComprar, btnInicioVender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        btnInicioComprar=(ImageView) findViewById(R.id.btnInicioComprar);
        btnInicioVender=(ImageView) findViewById(R.id.btnInicioVender);

        btnInicioComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, LoginActivity.class);

                startActivity(intent);
            }
        });

        btnInicioVender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioActivity.this, LoginDistribuidorActivity.class);

                startActivity(intent);
            }
        });

        btnInicioComprar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ImageView myImageView = (ImageView)findViewById(R.id.btnInicioComprar);
                    myImageView.setImageResource(R.drawable.button1_1);
                }else if(event.getAction() == MotionEvent.ACTION_UP){

                    ImageView myImageView = (ImageView)findViewById(R.id.btnInicioComprar);
                    myImageView.setImageResource(R.drawable.button1);
                }
                return false;
            }
        });

        btnInicioVender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    ImageView myImageView = (ImageView)findViewById(R.id.btnInicioVender);
                    myImageView.setImageResource(R.drawable.button2_2);
                }else if(event.getAction() == MotionEvent.ACTION_UP){

                    ImageView myImageView = (ImageView)findViewById(R.id.btnInicioVender);
                    myImageView.setImageResource(R.drawable.button2);
                }
                return false;
            }
        });
    }
}
