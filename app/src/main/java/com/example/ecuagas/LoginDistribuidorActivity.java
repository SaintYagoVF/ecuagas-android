package com.example.ecuagas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginDistribuidorActivity extends AppCompatActivity {


    Button btnLoginDistribuidor, btnRegistroDistribuidor;

    EditText txtCodigoDistribuidor, txtClaveDistribuidor;

    //FIRESTORE
    private static final String TAG="LoginDistribuidorActivity";
    private static final String KEY_CODIGO="codigo";
    private static final String KEY_CLAVE="clave";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    //SharedPreference
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Rol = "rolKey";
    public static final String Id = "idKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_distribuidor);

        btnLoginDistribuidor=(Button)findViewById(R.id.btnLoginDistribuidor);
        btnRegistroDistribuidor=(Button)findViewById(R.id.btnRegistroDistribuidor);

        txtCodigoDistribuidor=(EditText)findViewById(R.id.txtCodigoDistribuidor);
        txtClaveDistribuidor=(EditText)findViewById(R.id.txtClaveDistribuidor);

        //SharedPreferences

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        btnLoginDistribuidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginDistribuidor();

            }
        });

        btnRegistroDistribuidor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginDistribuidorActivity.this, RegistroDistribuidorActivity.class);

                startActivity(intent);

            }
        });


        btnLoginDistribuidor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    v.getBackground().setAlpha(150);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    v.getBackground().setAlpha(255);
                }
                return false;
            }
        });

        btnRegistroDistribuidor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    v.getBackground().setAlpha(150);
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    v.getBackground().setAlpha(255);
                }
                return false;
            }
        });
    }


    private void loginDistribuidor(){





        String codigo=txtCodigoDistribuidor.getText().toString();
        final String clave=txtClaveDistribuidor.getText().toString();



        db.collection("Distribuidor").document(codigo).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){

                            //Map<String,Object> usuario=documentSnapshot.getData();

                            String clave_firebase=documentSnapshot.getString(KEY_CLAVE);

                            if(clave_firebase.equals(clave)){


                                try {


                                    SharedPreferences.Editor editor3 = sharedpreferences.edit();

                                    editor3.putString(Rol,"Distribuidor");

                                    editor3.commit();



                                } catch (Exception e) {


                                    e.printStackTrace();
                                }

                                try {


                                    SharedPreferences.Editor editor = sharedpreferences.edit();

                                    editor.putString(Id,txtCodigoDistribuidor.getText().toString());

                                    editor.commit();



                                } catch (Exception e) {


                                    e.printStackTrace();
                                }


                                Toast.makeText(LoginDistribuidorActivity.this,"¡Bienvenido a Ecuagas!",Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(LoginDistribuidorActivity.this, MainActivityDistribuidor.class);

                                startActivity(intent);



                            }else{
                                Toast.makeText(LoginDistribuidorActivity.this,"La clave es incorrecta",Toast.LENGTH_LONG).show();
                            }





                        }
                        else{

                            Toast.makeText(LoginDistribuidorActivity.this,"El distribuidor no existe o no se ha registrado en la App.",Toast.LENGTH_LONG).show();

                        }


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginDistribuidorActivity.this,"Error al obtener código",Toast.LENGTH_LONG).show();
                        Log.d("ErrorLogin",e.toString());
                    }
                });

    }
}
