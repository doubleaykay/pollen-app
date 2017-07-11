package mystikos.pollen;

import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    private JsonElement jelement2;
    private JsonObject locationobject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        JnumberToday = (TextView) findViewById(R.id.numberToday);
        JnumberTomorrow = (TextView) findViewById(R.id.numberTomorrow);
        JnumberDayAfter = (TextView) findViewById(R.id.numberDayAfter);

        run();
    }

    private void run() {
        //getZip();
        getPollenData();
        setPollenText();
        setLocationText();
    }

    private String[] getPollenData() {
        String[] pollen = new String[5];
        try {
            URL url = new URL("http://pollenapps.com/AllergyAlertWebSVC/api/1.0/Forecast/ForecastForZipCode?Zipcode=02145&Affiliateid=9642&AppID=2.1.0&uid=6693636764");
            //URL url = new URL("http://pollenapps.com/AllergyAlertWebSVC/api/1.0/Forecast/ForecastForZipCode?Zipcode=" + getZip() + "&Affiliateid=9642&AppID=2.1.0&uid=6693636764"); //url with zip code variable
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
    } //method to parse pollen data and return array of values

    private void setPollenText() {
        JnumberToday.setText(getPollenData()[0]);
        setTodayTextColor();
        JnumberTomorrow.setText(getPollenData()[1]);
        setTomorrowTextColor();
        JnumberDayAfter.setText(getPollenData()[2]);
        setDayAfterTextColor();
    } //method to set the textviews for pollen data based on values in array returned by method getPollenData()

    private void getZip() {
        String zip;
        //return zip;
    } //method to get zip code from settings file

    private void setTodayTextColor() {
        double pollenToday = Double.parseDouble(getPollenData()[0]);
        if ( pollenToday <= 4.0)
            JnumberToday.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
        else if ( pollenToday > 4.0 && pollenToday <= 8.0)
            JnumberToday.setTextColor(ResourcesCompat.getColor(getResources(), R.color.yellow, null));
        else
            JnumberToday.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
    } //set today text color based on pollen level

    private void setTomorrowTextColor() {
        double pollenTomorrow = Double.parseDouble(getPollenData()[1]);
        if ( pollenTomorrow <= 4.0)
            JnumberTomorrow.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
        else if ( pollenTomorrow > 4.0 && pollenTomorrow <= 8.0)
            JnumberTomorrow.setTextColor(ResourcesCompat.getColor(getResources(), R.color.yellow, null));
        else
            JnumberTomorrow.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
    } //set tomorrow text color based on pollen level

    private void setDayAfterTextColor() {
        double pollenDayAfter = Double.parseDouble(getPollenData()[2]);
        if ( pollenDayAfter <= 4.0)
            JnumberDayAfter.setTextColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
        else if ( pollenDayAfter > 4.0 && pollenDayAfter <= 8.0)
            JnumberDayAfter.setTextColor(ResourcesCompat.getColor(getResources(), R.color.yellow, null));
        else
            JnumberDayAfter.setTextColor(ResourcesCompat.getColor(getResources(), R.color.red, null));
    } //set day after text color based on pollen level

    private void setLocationText() {
        String city = getPollenData()[3].replace("\"", "");
        String state = getPollenData()[4].replace("\"", "");
        String location = city + ", " + state;
        setTitle(location);
    }
}
