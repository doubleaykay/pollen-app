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

        getData();
    }

    protected void getData () {
        try {
            URL url = new URL("http://pollenapps.com/AllergyAlertWebSVC/api/1.0/Forecast/ForecastForZipCode?Zipcode=02145&Affiliateid=9642&AppID=2.1.0&uid=6693636764");
            //URL url = new URL("http://pollenapps.com/AllergyAlertWebSVC/api/1.0/Forecast/ForecastForZipCode?Zipcode=" + zip + "&Affiliateid=9642&AppID=2.1.0&uid=6693636764"); //url with zip code variable
            InputStream in = url.openStream();

            jelement = new JsonParser().parse(new InputStreamReader(in));
            jobject = jelement.getAsJsonObject();

            jobject = jobject.getAsJsonObject("allergyForecast");
            String pollenToday = jobject.get("Day0").toString();
            String pollenTomorrow = jobject.get("Day1").toString();
            String pollenDayAfter = jobject.get("Day2").toString();

            JnumberToday.setText(pollenToday);
            JnumberTomorrow.setText(pollenTomorrow);
            JnumberDayAfter.setText(pollenDayAfter);

            //setLocationText();
        } catch (Exception e) {e.printStackTrace();}
    } //method to get pollen data and set it into its respective textviews

    protected void getZip () {

    } //method to get zip code from settings file

    protected void setTheme () {

    }

    protected void setLocationText () {
        try {
            URL url = new URL("http://pollenapps.com/AllergyAlertWebSVC/api/1.0/Forecast/ForecastForZipCode?Zipcode=02145&Affiliateid=9642&AppID=2.1.0&uid=6693636764");
            //URL url = new URL("http://pollenapps.com/AllergyAlertWebSVC/api/1.0/Forecast/ForecastForZipCode?Zipcode=" + zip + "&Affiliateid=9642&AppID=2.1.0&uid=6693636764"); //url with zip code variable
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
