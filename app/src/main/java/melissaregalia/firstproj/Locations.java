package melissaregalia.firstproj;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;


public class Locations extends ActionBarActivity {

    Button addlocationButton;
    //TextView resultArea;
    EditText locline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        addlocationButton = (Button) findViewById(R.id.button3);
        //resultArea = (TextView) findViewById(R.id.textView2);
        locline = (EditText) findViewById(R.id.editText);

        addlocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //if they click on the Login button
                //check that login isn't void
                //verify that login is in database
                AddFriendSQL s = new AddFriendSQL();
                GetUserSQL s2 = new GetUserSQL();
                String locname = locline.getText().toString();

                String value = "";

                if (locname.equals("")){
                    return;
                }
                //resultArea.setText(loginname);
                s2.execute("SELECT * FROM location_info WHERE username=\'"+MainActivity.currentUser+"\' and location=\'"+locname+"\'");

                /*int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(getApplicationContext(), "SELECT * FROM location_info WHERE username=\'"+MainActivity.currentUser+"\' and location=\'"+locname+"\'", duration);
                toast.show();*/

                try
                {
                    value = s2.get();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                } catch (ExecutionException e)
                {
                    e.printStackTrace();
                }

                if (!value.equals("")){ //then it exists already with that name
                    return;
                }

                s.execute("INSERT INTO location_info VALUES (\'"+MainActivity.currentUser+"\',\'"+locname+"\')");

                try
                {
                    value = s.get();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                } catch (ExecutionException e)
                {
                    e.printStackTrace();
                }

                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), "Location Added!" , duration);
                toast.show();

                locline.setText("");
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_locations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
