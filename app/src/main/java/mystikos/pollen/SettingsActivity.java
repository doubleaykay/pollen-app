package mystikos.pollen;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends AppCompatActivity {

    private EditText JtextZip;
    private Button JbuttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        setTitle("Settings");

        JtextZip = (EditText) findViewById(R.id.textZip);
        JbuttonSave = (Button) findViewById(R.id.buttonSave);
    }

    public void save(View v) {
        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString("zip", JtextZip.getText().toString()).apply();
        Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
        SettingsActivity.this.startActivity(myIntent);
        finish();
    }

    public void cancel(View v) {
        Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
        SettingsActivity.this.startActivity(myIntent);
        finish();
    }

    private void getLocation() {
        //location services bs
    } //TODO get location from device location services and then pull zip code from there
}
