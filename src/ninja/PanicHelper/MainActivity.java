package ninja.PanicHelper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button buttonOne = (Button) findViewById(R.id.button1);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

            }
        });


    }
}
