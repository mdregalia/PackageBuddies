package melissaregalia.firstproj;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class PackageScreen extends ActionBarActivity
{

    TextView resultArea;
    Activity mSelf = this;
    Button addPackageButton;
    ListView packageList;
    ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_screen);

        resultArea = (TextView) findViewById(R.id.noPackagesTextView);
        addPackageButton = (Button) findViewById(R.id.addPackageButton);
        packageList = (ListView) findViewById(R.id.packageList);

        // ADD PACKAGE BUTTON:

        addPackageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mSelf, AddPackage.class);
                startActivity(intent);
            }
        });

        // PACKAGE LIST:

        GetUserListSQL l = new GetUserListSQL();
        List<String> list = new ArrayList<String>();

        l.execute("SELECT name FROM packages WHERE sender=\'" + MainActivity.currentUser + "\'");

        try
        {
            list = l.get();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        l = new GetUserListSQL();

        l.execute("SELECT name FROM packages WHERE intermediary=\'" + MainActivity.currentUser + "\'");

        try
        {
            list.addAll(l.get());
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        l = new GetUserListSQL();

        l.execute("SELECT name FROM packages WHERE recipient=\'" + MainActivity.currentUser + "\'");

        try
        {
            list.addAll(l.get());
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        listAdapter = new ArrayAdapter<String>(this,R.layout.simplerow,list);

        if(list.isEmpty())
        {
            resultArea.setText("You currently do not have any packages.");
        }

        packageList.setAdapter(listAdapter);

        packageList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = packageList.getItemAtPosition(position).toString();

                //sql query to get info about package and see if intermed or not
                PackageInfoSQL squery = new PackageInfoSQL();

                squery.execute("SELECT * FROM packages WHERE name=\'"+value+"\'");
                String intermed = "";
                List<String> packagedetails = new ArrayList<String>();
                try
                {
                    packagedetails = squery.get();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                } catch (ExecutionException e)
                {
                    e.printStackTrace();
                }

                intermed = packagedetails.get(1);
                /*Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, intermed, duration);
                toast.show();*/

                if (intermed.equals("")) { //aka no intermediate
                    Intent intent = new Intent(mSelf, PackageView.class);
                    intent.putExtra("package name", value);
                    //add more and get from
                    startActivity(intent);
                }
                else{ //intermediate with package
                    Intent intent = new Intent(mSelf, PackageViewIntermed.class);
                    intent.putExtra("package name",value);
                    //add more here
                    startActivity(intent);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_package_screen, menu);
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
