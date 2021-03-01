package com.example.broadcastreceiverytelefonarespuestaautomtica;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class llamadaentrante extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager mTelManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        mTelManager.listen(new TeleListener(context),PhoneStateListener.LISTEN_CALL_STATE);
        }
    }
class TeleListener extends PhoneStateListener{
    private Context ctx;
    public  TeleListener (Context ctx){this.ctx=ctx;}
    @Override
    public void onCallStateChanged(int state, String phoneNumber) {
        super.onCallStateChanged(state, phoneNumber);
        String[] arch =Archivo.readFromFile(ctx).split(",");
        String numero=limpiarNumero(arch[0]);
        String mensaje=arch[1];
        if(TelephonyManager.CALL_STATE_RINGING==state){
            Log.d("IDEA",phoneNumber);
            phoneNumber=limpiarNumero(phoneNumber);
            if(phoneNumber.equals(numero)){
                sendSMS(numero,mensaje,ctx);
            }else{
                Toast.makeText(ctx,numero+ " != "+phoneNumber + " "+ numero.length()+" != "+phoneNumber.length(),Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void sendSMS (String numero, String mensaje,Context context){
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(numero,null,mensaje,null,null);
            Toast.makeText(context, "Mensaje Enviado.", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            Toast.makeText(context, "Mensaje no enviado, datos incorrectos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public String limpiarNumero (String c){
        String result="";
        String t;
        for(int i=0; i<c.length(); i++){
            t=c.charAt(i)+"";
            if(t.equals("+")){
                c=c.substring(2);
            }else if(t.matches("[0-9]")){
                result+=t;
            }
        }
        return result;
    }
}





