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

import java.util.HashMap;
import java.util.Map;

public class RegistroDistribuidorActivity extends AppCompatActivity {

    EditText txtCodigo, txtClave;

    Button btnAceptar;

    private static final String KEY_CODIGO="codigo";
    private static final String KEY_CLAVE="clave";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Shared
    //SharedPreference
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Rol = "rolKey";
    public static final String Id = "idKey";
    SharedPreferences sharedpreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_distribuidor);

        //SharedPreferences

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        txtCodigo=(EditText)findViewById(R.id.txtDistribuidorRegistro);
        txtClave=(EditText)findViewById(R.id.txtDistribuidorClave);

        btnAceptar=(Button)findViewById(R.id.btnDistribuidorAceptar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            registroDistribuidor();
            }
        });


        btnAceptar.setOnTouchListener(new View.OnTouchListener() {
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

    private void registroDistribuidor(){

        final String codigo=txtCodigo.getText().toString();

        String clave=txtClave.getText().toString();

       final Map<String,Object> usuario=new HashMap<>();

        usuario.put(KEY_CODIGO,codigo);

        usuario.put(KEY_CLAVE,clave);


        db.collection("Base_Distribuidor").document(codigo).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){

                            db.collection("Distribuidor").document(codigo).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                                            if(documentSnapshot.exists()){

                                                Toast.makeText(RegistroDistribuidorActivity.this,"Este distribuidor ya se ha registrado anteriormente",Toast.LENGTH_LONG).show();


                                            }
                                            else{



                                                db.collection("Distribuidor").document(codigo).set(usuario)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(RegistroDistribuidorActivity.this,"¡Se ha registrado con éxito!",Toast.LENGTH_LONG).show();


                                                                try {


                                                                    SharedPreferences.Editor editor3 = sharedpreferences.edit();

                                                                    editor3.putString(Rol,"Distribuidor");

                                                                    editor3.commit();



                                                                } catch (Exception e) {


                                                                    e.printStackTrace();
                                                                }

                                                                try {


                                                                    SharedPreferences.Editor editor = sharedpreferences.edit();

                                                                    editor.putString(Id,txtCodigo.getText().toString());

                                                                    editor.commit();



                                                                } catch (Exception e) {


                                                                    e.printStackTrace();
                                                                }

                                                                Intent intent = new Intent(RegistroDistribuidorActivity.this, MainActivityDistribuidor.class);

                                                                startActivity(intent);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

                                                                Toast.makeText(RegistroDistribuidorActivity.this,"Error al registrar usuario",Toast.LENGTH_LONG).show();
                                                                Log.d("Error",e.toString());

                                                            }
                                                        });



                                            }


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegistroDistribuidorActivity.this,"Error al obtener código",Toast.LENGTH_LONG).show();
                                        }
                                    });



                        }
                        else
                            Toast.makeText(RegistroDistribuidorActivity.this,"No existe el código de Distribuidor ingresado",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistroDistribuidorActivity.this,"Error al obtener código",Toast.LENGTH_LONG).show();
                    }
                });




    }
}
