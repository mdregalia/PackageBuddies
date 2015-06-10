package melissaregalia.firstproj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class AddPackage extends ActionBarActivity
{

    Activity mSelf = this;
    TextView intLocation;
    EditText editName, editWeight;
    Spinner recipientSpinner, locationSpinner, intermediarySpinner, intLocationSpinner;
    Button addPackageButton;
    ArrayAdapter<String> friendListAdapter, locListAdapter, intLocListAdapter;
    List<String> friendList, locations, intLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_package);

        intLocation = (TextView) findViewById(R.id.packageIntLocation);
        editName = (EditText) findViewById(R.id.packageNameText);
        editWeight = (EditText) findViewById(R.id.packageWeightText);
        recipientSpinner = (Spinner) findViewById(R.id.recipientSpinner);
        locationSpinner = (Spinner) findViewById(R.id.deliverySpinner);
        intermediarySpinner = (Spinner) findViewById(R.id.intermediarySpinner);
        intLocationSpinner = (Spinner) findViewById(R.id.intLocationSpinner);
        addPackageButton = (Button) findViewById(R.id.packageAddButton);

        friendList = new ArrayList<String>();
        locations = new ArrayList<String>();
        intLocations = new ArrayList<String>();
        friendList.add("None");
        locations.add("None");
        intLocations.add("None");

        GetUserListSQL l = new GetUserListSQL();
        l.execute("SELECT user2 FROM friends WHERE user1=\'" + MainActivity.currentUser + "\'");

        try
        {
            friendList.addAll(l.get());
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        friendListAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,friendList);
        recipientSpinner.setAdapter(friendListAdapter);
        intermediarySpinner.setAdapter(friendListAdapter);


        //locations.add("somelocation"); // DEBUG STATEMENT: delete this later
        locListAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,locations);
        locationSpinner.setAdapter(locListAdapter);

        intLocListAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,intLocations);
        intLocationSpinner.setAdapter(intLocListAdapter);

        addPackageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameStr = editName.getText().toString();
                //Log.d(MainActivity.class.getSimpleName(), "package name is " + nameStr);
                String weightStr = editWeight.getText().toString();
                String recipientStr = recipientSpinner.getSelectedItem().toString();
                String loc1Str = locationSpinner.getSelectedItem().toString();
                String intermediaryStr = intermediarySpinner.getSelectedItem().toString();
                String loc2Str = intLocationSpinner.getSelectedItem().toString();
                String existingStr = "";
                String emptyStr = "";

                GetUserSQL s = new GetUserSQL();
                s.execute("SELECT name FROM packages WHERE name = \'" + nameStr + "\'");
                try {
                    existingStr = s.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (nameStr.equals("") || weightStr.equals("") || recipientStr.equals("None") || loc1Str.equals("None")) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mSelf);
                    //alert.setTitle("Title");
                    dialog.setTitle("Please enter all of the required information.");

                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });

                    dialog.show();
                } else if (recipientStr.equals(intermediaryStr)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mSelf);
                    //alert.setTitle("Title");
                    dialog.setTitle("Recipient and intermediary cannot be the same.");

                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });

                    dialog.show();
                } else if (nameStr.equals(existingStr)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mSelf);
                    //alert.setTitle("Title");
                    dialog.setTitle("This package name already exists.");

                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });

                    dialog.show();
                } else {
                    if (intermediaryStr.equals("None"))
                        intermediaryStr = "";
                    if (loc2Str.equals("None"))
                        loc2Str = "";

                    AddFriendSQL a = new AddFriendSQL();
                    a.execute("INSERT INTO packages(name,weight,sender,recipient,rec_location,intermediary,int_location,status,rconf,dconf)" +
                            "VALUES (\'" + nameStr + "\',\'" + weightStr + "\',\'" + MainActivity.currentUser + "\'," +
                            "\'" + recipientStr + "\',\'" + loc1Str + "\',\'" + intermediaryStr + "\',\'" + loc2Str + "\'," +
                            "\'" + emptyStr + "\',\'" + emptyStr + "\',\'" + emptyStr + "\')");

                    Intent intent = new Intent(mSelf, PackageScreen.class);
                    startActivity(intent);
                }

            }
        });


        recipientSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String rec = recipientSpinner.getItemAtPosition(position).toString();
                String interm = intermediarySpinner.getSelectedItem().toString();
                if (!rec.equals("None") && intermediarySpinner.getSelectedItem().toString().equals("None")) // only a recipient is selected
                {
                    // need to populate the recipient location spinner only with recipient union currentuser
                    locations = new ArrayList<String>();
                    locations.add("None");
                    GetUserListSQL l = new GetUserListSQL();
                    l.execute("SELECT location FROM location_info WHERE username = \'" + MainActivity.currentUser + "\' UNION SELECT location FROM location_info WHERE username = \'" + rec + "\'");
                    try
                    {
                        locations.addAll(l.get());
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    } catch (ExecutionException e)
                    {
                        e.printStackTrace();
                    }
                    locListAdapter = new ArrayAdapter<String>(mSelf, R.layout.support_simple_spinner_dropdown_item, locations);
                    locationSpinner.setAdapter(locListAdapter);
                }
                else if(!rec.equals("None") && !interm.equals("None")) // both a recipient and an intermediary are selected
                {
                    // need to populate the recipient location spinner only with recipient union intermediary
                    locations = new ArrayList<String>();
                    locations.add("None");
                    GetUserListSQL l = new GetUserListSQL();
                    l.execute("SELECT location FROM location_info WHERE username = \'" + rec + "\' UNION SELECT location FROM location_info WHERE username = \'" + interm + "\'");
                    try
                    {
                        locations.addAll(l.get());
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    } catch (ExecutionException e)
                    {
                        e.printStackTrace();
                    }
                    locListAdapter = new ArrayAdapter<String>(mSelf, R.layout.support_simple_spinner_dropdown_item, locations);
                    locationSpinner.setAdapter(locListAdapter);
                }
                else if(rec.equals("None")) // "None" is selected for recipient
                {
                    // need to reset the recipient location spinner
                    locations = new ArrayList<String>();
                    locations.add("None");
                    locListAdapter = new ArrayAdapter<String>(mSelf, R.layout.support_simple_spinner_dropdown_item, locations);
                    locationSpinner.setAdapter(locListAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });

        intermediarySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String interm = intermediarySpinner.getItemAtPosition(position).toString();
                String rec = recipientSpinner.getSelectedItem().toString();
                if(!interm.equals("None")) // intermediary is selected
                {
                    // need to set intermediary spinner to have intermediary union currentuser
                    intLocations = new ArrayList<String>();
                    intLocations.add("None");
                    GetUserListSQL l = new GetUserListSQL();
                    l.execute("SELECT location FROM location_info WHERE username = \'" + MainActivity.currentUser + "\' UNION SELECT location FROM location_info WHERE username = \'" + interm + "\'");
                    try
                    {
                        intLocations.addAll(l.get());
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    } catch (ExecutionException e)
                    {
                        e.printStackTrace();
                    }
                    intLocListAdapter = new ArrayAdapter<String>(mSelf, R.layout.support_simple_spinner_dropdown_item, intLocations);
                    intLocationSpinner.setAdapter(intLocListAdapter);

                    if(!rec.equals("None")) // both an intermediary and a recipient are selected
                    {
                        // need to set recipient spinner to have recipient union intermediary
                        locations = new ArrayList<String>();
                        locations.add("None");
                        l = new GetUserListSQL();
                        l.execute("SELECT location FROM location_info WHERE username = \'" + rec + "\' UNION SELECT location FROM location_info WHERE username = \'" + interm + "\'");
                        try
                        {
                            locations.addAll(l.get());
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        } catch (ExecutionException e)
                        {
                            e.printStackTrace();
                        }
                        locListAdapter = new ArrayAdapter<String>(mSelf, R.layout.support_simple_spinner_dropdown_item, locations);
                        locationSpinner.setAdapter(locListAdapter);
                    }
                }
                else // "None" is selected in intermediary spinner
                {
                    // need to reset both intermediary and recipient location spinners
                    intLocations = new ArrayList<String>();
                    intLocations.add("None");
                    intLocListAdapter = new ArrayAdapter<String>(mSelf, R.layout.support_simple_spinner_dropdown_item, intLocations);
                    intLocationSpinner.setAdapter(intLocListAdapter);

                    if(rec.equals("None")) // this one is probably not required
                    {
                        locations = new ArrayList<String>();
                        locations.add("None");
                        locListAdapter = new ArrayAdapter<String>(mSelf, R.layout.support_simple_spinner_dropdown_item, locations);
                        locationSpinner.setAdapter(locListAdapter);
                    }
                    else // but this is required: recipient has a value, reset to recipient union currentuser
                    {
                        locations = new ArrayList<String>();
                        locations.add("None");
                        GetUserListSQL l = new GetUserListSQL();
                        l.execute("SELECT location FROM location_info WHERE username = \'" + MainActivity.currentUser + "\' UNION SELECT location FROM location_info WHERE username = \'" + rec + "\'");
                        try
                        {
                            locations.addAll(l.get());
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        } catch (ExecutionException e)
                        {
                            e.printStackTrace();
                        }
                        locListAdapter = new ArrayAdapter<String>(mSelf, R.layout.support_simple_spinner_dropdown_item, locations);
                        locationSpinner.setAdapter(locListAdapter);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_package, menu);
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
