package com.example.broadcastreceiverytelefonarespuestaautomtica;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText txtMensaje, txtNumero;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtMensaje=findViewById(R.id.txtMensaje);
        txtNumero=findViewById(R.id.txtNumero);
        button=findViewById(R.id.button);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_CALL_LOG}, 1);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    //Escribir datos en el archivo
                    String cad =txtNumero.getText().toString()+","+txtMensaje.getText().toString();
                    Archivo.writeToFile(cad,getApplicationContext());
                    Toast.makeText(MainActivity.this,"Guardado"+cad,Toast.LENGTH_LONG).show();
                }catch(Exception e){
                    Toast.makeText(MainActivity.this,e+"",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}