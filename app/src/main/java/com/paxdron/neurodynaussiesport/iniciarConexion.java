package com.paxdron.neurodynaussiesport;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class iniciarConexion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_conexion);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        getMenuInflater().inflate(R.menu.menu_conexion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.action_settings){
            startActivity(new Intent(this,settingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startControl(View v){
        startActivity(new Intent(this, Control.class));
    }
}
