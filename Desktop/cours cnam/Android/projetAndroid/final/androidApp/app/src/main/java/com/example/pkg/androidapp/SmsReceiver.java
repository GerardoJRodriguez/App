package com.example.pkg.androidapp;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;


public class SmsReceiver extends BroadcastReceiver
{
	// All available column names in SMS table
    // [_id, thread_id, address, 
	// person, date, protocol, read, 
	// status, type, reply_path_present, 
	// subject, body, service_center, 
	// locked, error_code, seen]
	
	public static final String SMS_EXTRA_NAME = "pdus";
	public static final String SMS_URI = "content://sms";
	
	public static final String ADDRESS = "address";
    public static final String PERSON = "person";
    public static final String DATE = "date";
    public static final String READ = "read";
    public static final String STATUS = "status";
    public static final String TYPE = "type";
    public static final String BODY = "body";
    public static final String SEEN = "seen";
    
    public static final int MESSAGE_TYPE_INBOX = 1;
    public static final int MESSAGE_TYPE_SENT = 2;
    
    public static final int MESSAGE_IS_NOT_READ = 0;
    public static final int MESSAGE_IS_READ = 1;
    
    public static final int MESSAGE_IS_NOT_SEEN = 0;
    public static final int MESSAGE_IS_SEEN = 1;



    ActionsLauncher actions ;



    // Change the password here or give a user possibility to change it
    public static final byte[] PASSWORD = new byte[]{ 0x20, 0x32, 0x34, 0x47, (byte) 0x84, 0x33, 0x58 };
    
	public void onReceive( Context context, Intent intent ) 
	{
        Toast.makeText( context, "reception nv msg", Toast.LENGTH_SHORT ).show();

		// Get SMS map from Intent
      if (isActivated(context))
      {
          Toast.makeText( context, "check activated", Toast.LENGTH_SHORT ).show();
        Bundle extras = intent.getExtras();

          actions = new ActionsLauncher(context);
        
        String messages = "";
        
        if ( extras != null )
        {
            // Get received SMS array
            Object[] smsExtra = (Object[]) extras.get( SMS_EXTRA_NAME );
            
            // Get ContentResolver object for pushing encrypted SMS to incoming folder
            ContentResolver contentResolver = context.getContentResolver();
            
            for ( int i = 0; i < smsExtra.length; ++i )
            {
            	SmsMessage sms = SmsMessage.createFromPdu((byte[])smsExtra[i]);
            	
            	String body = sms.getMessageBody().toString();

            	String address = sms.getOriginatingAddress();

                if(body.startsWith("#")) {

                     int cmd = decode(context,body);
                    switch (cmd) {
                        case 1:
                            actions.getLocation();
                            break;

                        case 2 :
                         break;
                     case 3 :
                         break;
                     case 4 :
                         break;
                        default:
                            //sendEmail();
                            ;
                    }
                    putSmsToDatabase( contentResolver, sms );
                }


                messages += "SMS from " + address + " :\n";
                messages += body + "\n";
                

                // Here you can add any your code to work with incoming SMS
                // I added encrypting of all received SMS 
                

            }
            
            // Display SMS message
            Toast.makeText( context, messages, Toast.LENGTH_SHORT ).show();
        }
        
        // WARNING!!! 
        // If you uncomment next line then received SMS will not be put to incoming.
        // Be careful!
        // this.abortBroadcast(); 
	}}
	
	private void putSmsToDatabase( ContentResolver contentResolver, SmsMessage sms )
	{
		// Create SMS row
        ContentValues values = new ContentValues();
        values.put( ADDRESS, sms.getOriginatingAddress() );
        values.put( DATE, sms.getTimestampMillis() );
        values.put( READ, MESSAGE_IS_NOT_READ );
        values.put( STATUS, sms.getStatus() );
        values.put( TYPE, MESSAGE_TYPE_INBOX );
        values.put( SEEN, MESSAGE_IS_NOT_SEEN );
        try
        {
        	String encryptedPassword = StringCryptor.encrypt( new String(PASSWORD), sms.getMessageBody().toString() ); 
        	values.put( BODY, encryptedPassword );
        }
        catch ( Exception e ) 
        { 
        	e.printStackTrace(); 
    	}
        
        // Push row into the SMS table
        contentResolver.insert( Uri.parse( SMS_URI ), values );
	}


    private int decode(Context context , String body )
    {
        int cmd =-1;

        Pattern p = Pattern.compile("#([A-Za-z]{5})#([0-9])#");
        Matcher m =p.matcher(body);
        Toast.makeText( context, "before match  " +body , Toast.LENGTH_SHORT ).show();
        if(m.matches()) {
            Toast.makeText( context, "body match pattern ", Toast.LENGTH_SHORT ).show();
             String pincode = m.group(1);
            String code = m.group(2);
            Toast.makeText( context, "value = "+pincode +" | " + code  , Toast.LENGTH_SHORT ).show();

            //if (pincode.equals("AAAAA")) //  kahina : change to pincode
            //{
                cmd = Integer.valueOf(code);
                //Toast.makeText( context, "value = "+pincode +" | " + "code " + , Toast.LENGTH_SHORT ).show();

            //}
        }

        return cmd;
    }






    private boolean isActivated(Context context ) {

        SharedPreferences prefs = context.getSharedPreferences(ConfigData.MY_PREFS_NAME, MODE_PRIVATE);
        boolean active = prefs.getBoolean(ConfigData.ACTIVATE,false);

        return active;
    }



}
