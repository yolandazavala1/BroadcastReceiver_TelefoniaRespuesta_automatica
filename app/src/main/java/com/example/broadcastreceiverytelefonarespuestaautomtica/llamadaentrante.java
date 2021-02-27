package com.example.broadcastreceiverytelefonarespuestaautomtica;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class llamadaentrante extends BroadcastReceiver {
    private static final String TAG = "PhoneStatReceiver";
    private static boolean incomingFlag = false;
    private static String incoming_number = null;
    private static String mensajeGuardado = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        //OBTENEMOS EL MENSAJE ENVIADO DE LA CAJA DE TEXTO PARA GUARDARLO EN UNA VARIABLE
        String[] arch = Archivo.readFromFile(context).split(",");
        mensajeGuardado=arch[1];
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            incomingFlag = false;
        } else {
            // TELEPHONY MANAGER OBTENIENDO EL SERVICIO DE TELEFONIA.
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
            //SWITCH QUE PREGUNTA POR EL ESTADO DE MANAGER SI SE ENCUENTRA EN RINGING(SONANDO) O IDLE.
            switch (tm.getCallState()) {
                // SI EL ESTADO DEL MANAGER ES UN RINGING ES PORQUE HAY UNA LLAMADA ENTRANTE O SONANDO, ENTONCES CAPTURAMOS EL NUMERO TELEFONICO.
                case TelephonyManager.CALL_STATE_RINGING:
                    Toast.makeText(context, "Ringing", Toast.LENGTH_LONG).show();
                    incomingFlag = true;
                    incoming_number = arch[0];
                    //Si el incoming_number es igual al que esta sonando
                    if(incoming_number.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                        try{
                            //CREANDO UN OBJETO DE SMS MANAGER.
                            SmsManager smgr = SmsManager.getDefault();
                            // ENVIAMOS EL SMS CON EL MENSAJE GUARDADO Y EL NUMERO TELEFONICO.
                            smgr.sendTextMessage(incoming_number, null, mensajeGuardado, null, null);
                            Toast.makeText(context, "Enviando mensaje a :" + incoming_number + " " + mensajeGuardado, Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            Toast.makeText(context,"Error: "+e, Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context, incoming_number+"!="+TelephonyManager.EXTRA_STATE_RINGING, Toast.LENGTH_LONG).show();
                    }
                    break;
                    // SI CONTESTAS LA LLAMADA.
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (incomingFlag) {
                        Log.i(TAG, "incoming ACCEPT :" + incoming_number);
                    }
                    break;
                    //AL TERMINAR LA LLAMADA
                case TelephonyManager.CALL_STATE_IDLE:
                    if (incomingFlag) {
                        Toast.makeText(context, "incoming IDLE", Toast.LENGTH_LONG).show();
                    }
                    break;
                }
            }
        }
    }

