package mystikos.pollen;

import android.os.StrictMode;
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
        //getLocationData();
        setPollenText();
        //setLocationText();
        //setTheme(); DOES NOT WORK YET
    }

    private String[] getPollenData() {
        String[] pollen = new String[3];
        try {
            URL url = new URL("http://pollenapps.com/AllergyAlertWebSVC/api/1.0/Forecast/ForecastForZipCode?Zipcode=02145&Affiliateid=9642&AppID=2.1.0&uid=6693636764");
            //URL url = new URL("http://pollenapps.com/AllergyAlertWebSVC/api/1.0/Forecast/ForecastForZipCode?Zipcode=" + getZip() + "&Affiliateid=9642&AppID=2.1.0&uid=6693636764"); //url with zip code variable
            InputStream in = url.openStream();

            jelement = new JsonParser().parse(new InputStreamReader(in));
            jobject = jelement.getAsJsonObject();

            jobject = jobject.getAsJsonObject("allergyForecast");

            pollen[0] = jobject.get("Day0").toString(); //pollen today
            pollen[1] = jobject.get("Day1").toString(); //pollen tomorrow
            pollen[2] = jobject.get("Day2").toString(); //pollen day after
        } catch (Exception e) {e.printStackTrace();}
        return pollen;
    } //method to parse pollen data and return array of values

    private void setPollenText() {
        JnumberToday.setText(getPollenData()[0]);
        JnumberTomorrow.setText(getPollenData()[1]);
        JnumberDayAfter.setText(getPollenData()[2]);
    } //method to set the textviews for pollen data based on values in array returned by method getPollenData()

    private void getZip() {
        String zip;
        //return zip;
    } //method to get zip code from settings file

    private void setTheme() {
        double pollenToday = Double.parseDouble(getPollenData()[0]);
        if ( pollenToday <= 4.0)
            super.setTheme(R.style.green);
        if ( pollenToday > 4.0 && pollenToday <= 8.0)
            super.setTheme(R.style.yellow);
        else
            super.setTheme(R.style.red);
    } //method to set activity theme based on pollen level

    private void setLocationText() {
        try {
            URL url = new URL("http://pollenapps.com/AllergyAlertWebSVC/api/1.0/Forecast/ForecastForZipCode?Zipcode=02145&Affiliateid=9642&AppID=2.1.0&uid=6693636764");
            //URL url = new URL("http://pollenapps.com/AllergyAlertWebSVC/api/1.0/Forecast/ForecastForZipCode?Zipcode=" + getZip() + "&Affiliateid=9642&AppID=2.1.0&uid=6693636764"); //url with zip code variable
            InputStream in = url.openStream();

            jelement2 = new JsonParser().parse(new InputStreamReader(in));
            locationobject = jelement2.getAsJsonObject();

            locationobject = locationobject.getAsJsonObject("ForecastInfo");
            String city = locationobject.get("City").toString();
            String state = locationobject.get("State").toString();
            String location = city + "," + state;
            setTitle(location);
        } catch (Exception e) {e.printStackTrace();}
    } //method to set title to location, currently does not work
}
