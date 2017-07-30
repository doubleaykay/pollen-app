package mystikos.pollen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private TextView JnumberToday;
    private TextView JnumberTomorrow;
    private TextView JnumberDayAfter;
    private TextView JtextDayAfter;
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

        JnumberToday = (TextView) findViewById(R.id.numberToday);
        JnumberTomorrow = (TextView) findViewById(R.id.numberTomorrow);
        JnumberDayAfter = (TextView) findViewById(R.id.numberDayAfter);

        JtextDayAfter = (TextView) findViewById(R.id.textDayAfter);

        JcardToday = (CardView) findViewById(R.id.cardToday);
        JcardTomorrow = (CardView) findViewById(R.id.cardTomorrow);
        JcardDayAfter = (CardView) findViewById(R.id.cardDayAfter);

        pollen = new String[5];

        run();
    } //run when activity created

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    } //inflate menu

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
                finish();
                return true;
            case R.id.menuRefresh:
                run();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    } //listener for menu items

    public void run() {
        //setDayAfterTitleText(); //TODO set day after title text
        resetCardColor();
        setLoadingText();
        if (isOnline() == true) {
            new getPollenDataAsync().execute();
        }
        else {
            Context context = getApplicationContext();
            CharSequence text = "Please connect to the internet to proceed.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    } //calls other methods, linked to refresh button

    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {

            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);

        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false; }

    private void setDayAfterTitleText() {
        String[] days = new String[7];
        days[0] = "SUNDAY";
        days[1] = "MONDAY";
        days[2] = "TUESDAY";
        days[3] = "WEDNESDAY";
        days[4] = "THURSDAY";
        days[5] = "FRIDAY";
        days[6] = "SATURDAY";

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.SUNDAY:
                JtextDayAfter.setText(days[2]);
            case Calendar.MONDAY:
                JtextDayAfter.setText(days[3]);
            case Calendar.TUESDAY:
                JtextDayAfter.setText(days[4]);
            case Calendar.WEDNESDAY:
                JtextDayAfter.setText(days[5]);
            case Calendar.THURSDAY:
                JtextDayAfter.setText(days[6]);
            case Calendar.FRIDAY:
                JtextDayAfter.setText(days[0]);
            case Calendar.SATURDAY:
                JtextDayAfter.setText(days[1]);
        }
    } //logic to set the day after text to the day in two days //TODO read from strings file so that this can be localized //TODO DOES NOT WORK

    private void setLoadingText() {
        JnumberToday.setText("...");
        JnumberTomorrow.setText("...");
        JnumberDayAfter.setText("...");
    } //set until AsyncTask loads data

    private void setPollenText() {
        JnumberToday.setText(pollen[0]);
        JnumberTomorrow.setText(pollen[1]);
        JnumberDayAfter.setText(pollen[2]);
        setCardColor();
    } //method to set the textviews for pollen data based on values in pollen array

    private String getZip() {
        return PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString("zip", "02145");
    } //method to get zip code from shared preferences

    private void setCardColor() {
        double pollenToday = Double.parseDouble(pollen[0]);
        if ( pollenToday <= 4.0)
            JcardToday.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
        else if ( pollenToday > 4.0 && pollenToday <= 8.0)
            JcardToday.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.yellow, null));
        else
            JcardToday.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.red, null)); //today card color

        double pollenTomorrow = Double.parseDouble(pollen[1]);
        if ( pollenTomorrow <= 4.0)
            JcardTomorrow.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
        else if ( pollenTomorrow > 4.0 && pollenTomorrow <= 8.0)
            JcardTomorrow.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.yellow, null));
        else
            JcardTomorrow.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.red, null)); //tomorrow card color

        double pollenDayAfter = Double.parseDouble(pollen[2]);
        if ( pollenDayAfter <= 4.0)
            JcardDayAfter.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
        else if ( pollenDayAfter > 4.0 && pollenDayAfter <= 8.0)
            JcardDayAfter.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.yellow, null));
        else
            JcardDayAfter.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.red, null)); //day after card color
    } //set card color based on pollen level

    private void resetCardColor() {
        JcardToday.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.card, null));
        JcardTomorrow.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.card, null));
        JcardDayAfter.setCardBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.card, null));
    } //reset card background to default

    private void setLocationText() {
        String city = pollen[3].replace("\"", "");
        String state = pollen[4].replace("\"", "");
        String location = city + ", " + state;
        setTitle(location);
    } //set title of activity based on parsed location data

    private class getPollenDataAsync extends AsyncTask<Void, Void, String[]> {
        protected String[] doInBackground(Void... params) {
            String[] asyncdata = new String[5];
            try {
                URL url = new URL("http://pollenapps.com/AllergyAlertWebSVC/api/1.0/Forecast/ForecastForZipCode?Zipcode=" + getZip() + "&Affiliateid=9642&AppID=2.1.0&uid=6693636764"); //url with zip code variable
                InputStream in = url.openStream();

                jelement = new JsonParser().parse(new InputStreamReader(in));
                jobject = jelement.getAsJsonObject();

                asyncdata[3] = jobject.get("City").toString(); //city name
                asyncdata[4] = jobject.get("State").toString(); //state abbreviation

                jobject = jobject.getAsJsonObject("allergyForecast");
                asyncdata[0] = jobject.get("Day0").toString(); //pollen today
                asyncdata[1] = jobject.get("Day1").toString(); //pollen tomorrow
                asyncdata[2] = jobject.get("Day2").toString(); //pollen day after
            } catch (Exception e) {e.printStackTrace();}
            return asyncdata;
        }

        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            pollen[0] = result[0];
            pollen[1] = result[1];
            pollen[2] = result[2];
            pollen[3] = result[3];
            pollen[4] = result[4];

            if (pollen[0] == null) {
                Context context = getApplicationContext();
                CharSequence text = "Please enter a valid zip code in location settings!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            } //if array is blank due to invalid zip code, alert user instead of just crashing
            else {
                setPollenText();
                setLocationText();
            } //if array is not blank, continue as normal
        }
    }
}
