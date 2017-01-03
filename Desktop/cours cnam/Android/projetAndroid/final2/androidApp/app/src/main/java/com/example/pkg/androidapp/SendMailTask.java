package com.example.pkg.androidapp;

/**
 * Created by LENOVO on 29/12/2016.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SendMailTask extends AsyncTask {

        private ProgressDialog statusDialog;
        private Activity sendMailActivity;
        String body="";
        String attach="";

        public SendMailTask(Activity activity, String mbody, String mAttach) {
            sendMailActivity = activity;
            body=mbody;
            attach=mAttach;


        }
    public SendMailTask() {

    }

        protected void onPreExecute() {
            //statusDialog = new ProgressDialog(sendMailActivity);
            statusDialog = new ProgressDialog(sendMailActivity);
            statusDialog.setMessage("Getting ready...");
            statusDialog.setIndeterminate(false);
            statusDialog.setCancelable(false);
            statusDialog.show();
        }

        @Override
        protected Object doInBackground(Object... args) {
            /* try {
                Log.i("SendMailTask", "About to instantiate GMail...");
                publishProgress("Processing input....");
                GMail androidEmail = new GMail(args[0].toString(),
                        args[1].toString(), (List) args[2], args[3].toString(),
                        args[4].toString());
                publishProgress("Preparing mail message....");
                androidEmail.createEmailMessage();
                publishProgress("Sending email....");
                androidEmail.sendEmail();
                publishProgress("Email Sent.");
                Log.i("SendMailTask", "Mail Sent.");
            } catch (Exception e) {
                publishProgress(e.getMessage());
                Log.e("SendMailTask", e.getMessage(), e);
            }  */

                try {
                    GMailSender sender = new GMailSender("userprenom.nom@gmail.com", "prenom@nom");
                    sender.sendMail("Android Test",
                            body,
                            "userprenom.nom@gmail.com",
                            "userprenom.nom@gmail.com");
                   // if (attach.equals("")) set attacheement
                    Log.i("SendMailTask", "mail sent ...");
                    Toast.makeText( sendMailActivity.getApplicationContext(), "in catch send gmail", Toast.LENGTH_SHORT ).show();


                } catch (Exception e) {
                   // Log.e("SendMailTask exception ", e.getMessage(), e);
                    //Toast.makeText( sendMailActivity.getApplicationContext(), "in catch send gmail", Toast.LENGTH_SHORT ).show();


                }





            return null;
        }

        @Override
        public void onProgressUpdate(Object... values) {
            statusDialog.setMessage(values[0].toString());

        }

        @Override
        public void onPostExecute(Object result) {
           statusDialog.dismiss();
        }

    }

