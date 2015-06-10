package melissaregalia.firstproj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class Locations extends ActionBarActivity {

    Activity mSelf = this;
    Button addlocationButton;
    Button locDeleteButton;
    //TextView resultArea;
    EditText locline;
    Spinner locSpinner;

    ArrayAdapter<String> locListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        addlocationButton = (Button) findViewById(R.id.button3);
        locDeleteButton = (Button) findViewById(R.id.button4);
        //resultArea = (TextView) findViewById(R.id.textView2);
        locline = (EditText) findViewById(R.id.editText);
        locSpinner = (Spinner) findViewById(R.id.spinner);

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

                List<String> locList = new ArrayList<String>();
                GetUserListSQL l = new GetUserListSQL();
                l.execute("SELECT location FROM location_info WHERE username=\'" + MainActivity.currentUser + "\'");

                locList.add("None");

                try
                {
                    locList.addAll(l.get());
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                } catch (ExecutionException e)
                {
                    e.printStackTrace();
                }

                locListAdapter = new ArrayAdapter<String>(mSelf, R.layout.support_simple_spinner_dropdown_item, locList);
                locSpinner.setAdapter(locListAdapter);
            }
        });

        List<String> locList = new ArrayList<String>();
        GetUserListSQL l = new GetUserListSQL();
        l.execute("SELECT location FROM location_info WHERE username=\'" + MainActivity.currentUser + "\'");

        locList.add("None");

        try
        {
            locList.addAll(l.get());
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        locListAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,locList);
        locSpinner.setAdapter(locListAdapter);

        locDeleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String toDelete = locSpinner.getSelectedItem().toString();
                if(toDelete.equals("None"))
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mSelf);
                    //alert.setTitle("Title");
                    dialog.setTitle("Please select a location to delete.");

                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });

                    dialog.show();
                }
                else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mSelf);
                    //alert.setTitle("Title");
                    dialog.setMessage("Are you sure you would like to delete \'" + toDelete + "\' from your list of preferred locations?");

                    dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            AddFriendSQL s = new AddFriendSQL();
                            s.execute("DELETE FROM location_info WHERE username = \'" + MainActivity.currentUser + "\' AND location = \'" + toDelete + "\'");

                            List<String> locList2 = new ArrayList<String>();
                            GetUserListSQL l2 = new GetUserListSQL();
                            l2.execute("SELECT location FROM location_info WHERE username=\'" + MainActivity.currentUser + "\'");

                            locList2.add("None");

                            try
                            {
                                locList2.addAll(l2.get());
                            } catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            } catch (ExecutionException e)
                            {
                                e.printStackTrace();
                            }

                            locListAdapter = new ArrayAdapter<String>(mSelf, R.layout.support_simple_spinner_dropdown_item, locList2);
                            locSpinner.setAdapter(locListAdapter);
                        }
                    });
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });

                    dialog.show();
                }
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
