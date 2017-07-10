package mystikos.pollen;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getData();
    }

    protected void getData () {

        setTitleToCity(); //run method to set title of activity to currently active city
    }

    protected void setTitleToCity () {
        setTitle("test");
    }
}
