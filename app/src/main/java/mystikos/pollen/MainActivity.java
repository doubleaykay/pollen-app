package mystikos.pollen;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView JnumberToday;
    private TextView JnumberTomorrow;
    private TextView JnumberDayAfter;
    private JsonElement jelement;
    private JsonObject jobject;
    private CardView JcardToday;
    private CardView JcardTomorrow;
    private CardView JcardDayAfter;

    private String[] pollen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        JnumberToday = (TextView) findViewById(R.id.numberToday);
        JnumberTomorrow = (TextView) findViewById(R.id.numberTomorrow);
        JnumberDayAfter = (TextView) findViewById(R.id.numberDayAfter);

        JcardToday = (CardView) findViewById(R.id.cardToday);
        JcardTomorrow = (CardView) findViewById(R.id.cardTomorrow);
        JcardDayAfter = (CardView) findViewById(R.id.cardDayAfter);

        pollen = new String[5];

        run();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAbout:
                Intent about = new Intent(MainActivity.this, AboutActivity.class);
                MainActivity.this.startActivity(about);
                return true;
            case R.id.menuSettings:
                Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(settings);
                return true;
            case R.id.menuRefresh:
                run();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void run() {
        //setLoadingText();
        getPollenData();
        setPollenText();
        setLocationText();
    }

    private void setLoadingText() {
        JnumberToday.setText("...");
        JnumberTomorrow.setText("...");
        JnumberDayAfter.setText("...");
    } //set until AsyncTask loads data

    private String[] getPollenData() {
        try {
            URL url = new URL("http://pollenapps.com/AllergyAlertWebSVC/api/1.0/Forecast/ForecastForZipCode?Zipcode=" + getZip() + "&Affiliateid=9642&AppID=2.1.0&uid=6693636764"); //url with zip code variable
            InputStream in = url.openStream();

            jelement = new JsonParser().parse(new InputStreamReader(in));
            jobject = jelement.getAsJsonObject();

            pollen[3] = jobject.get("City").toString(); //city name
            pollen[4] = jobject.get("State").toString(); //state abbreviation

            jobject = jobject.getAsJsonObject("allergyForecast");
            pollen[0] = jobject.get("Day0").toString(); //pollen today
            pollen[1] = jobject.get("Day1").toString(); //pollen tomorrow
            pollen[2] = jobject.get("Day2").toString(); //pollen day after
        } catch (Exception e) {e.printStackTrace();}
        return pollen;
    } //method to parse pollen data and return array of values //TODO turn into AsyncTask to not lag the main thread

    private void setPollenText() {
        JnumberToday.setText(pollen[0]);
        JnumberTomorrow.setText(pollen[1]);
        JnumberDayAfter.setText(pollen[2]);
        setTodayTextColor();
        setTomorrowTextColor();
        setDayAfterTextColor();
    } //method to set the textviews for pollen data based on values in array returned by method getPollenData()

    private String getZip() {
        return PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("zip", "02145");
    } //method to get zip code from settings file

    private void setTodayTextColor() {
        double pollenToday = Double.parseDouble(pollen[0]);
        if ( pollenToday <= 4.0)
            JcardToday.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
        else if ( pollenToday > 4.0 && pollenToday <= 8.0)
            JcardToday.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.yellow, null));
        else
            JcardToday.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
    } //set today card color based on pollen level

    private void setTomorrowTextColor() {
        double pollenTomorrow = Double.parseDouble(pollen[1]);
        if ( pollenTomorrow <= 4.0)
            JcardTomorrow.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
        else if ( pollenTomorrow > 4.0 && pollenTomorrow <= 8.0)
            JcardTomorrow.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.yellow, null));
        else
            JcardTomorrow.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
    } //set tomorrow card color based on pollen level

    private void setDayAfterTextColor() {
        double pollenDayAfter = Double.parseDouble(pollen[2]);
        if ( pollenDayAfter <= 4.0)
            JcardDayAfter.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
        else if ( pollenDayAfter > 4.0 && pollenDayAfter <= 8.0)
            JcardDayAfter.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.yellow, null));
        else
            JcardDayAfter.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
    } //set day after card color based on pollen level

    private void setLocationText() {
        String city = pollen[3].replace("\"", "");
        String state = pollen[4].replace("\"", "");
        String location = city + ", " + state;
        setTitle(location);
    } //set title of activity based on parsed location data
}
