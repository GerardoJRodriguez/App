package com.example.pkg.androidapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import static android.widget.AdapterView.*;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        ListView listView       = (ListView)findViewById((R.id.stationList));
        String[] values = new String[]{"Change pin code", "Change password","command history ","SMS decoding statut", "Disconnect" , "Delete account"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent intent =null;
                switch(position){
                    case 0:
                        intent = new Intent(getApplicationContext(),SetPinCodeActivity.class);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(),SetPasswordActivity.class);
                        break;
                    case 2 :
                        intent = new Intent(getApplicationContext(),SecureMessagesActivity.class);
                        break;
                    case 3:
                       intent = new Intent(getApplicationContext(),StatutManagerActivity.class);
                        break;
                    case 4:
                        intent = new Intent(getApplicationContext(),LogActivity.class);
                        break;
                    case 5:
                        intent = new Intent(getApplicationContext(),RegisterActivity.class);
                        break;
                }
                if (intent !=null )startActivity(intent);
            }

        });



    }
}
