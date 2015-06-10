package melissaregalia.firstproj;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;

import java.util.concurrent.ExecutionException;


public class PackageView extends ActionBarActivity {

    TextView senderText;
    TextView packageNameText;
    TextView weightText;
    TextView recText;
    Button deleteB;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_view);

        final Bundle extras = getIntent().getExtras();
        senderText = (TextView) findViewById(R.id.pv_sender);
        packageNameText = (TextView) findViewById(R.id.pv_packagename);
        weightText = (TextView) findViewById(R.id.pv_weight);
        recText = (TextView) findViewById(R.id.pv_reciever);
        deleteB = (Button) findViewById(R.id.button2);
        confirm = (Button) findViewById(R.id.button);


        /* DO NOT UNCOMMENT
        intent.putExtra("package name", value);
        intent.putExtra("sender",packagedetails.get(5));
        intent.putExtra("recipient",intermed);
        intent.putExtra("location",packagedetails.get(3)); //int location
        intent.putExtra("weight",packagedetails.get(0));
        intent.putExtra("type","1"); //1 means intermediate
        intent.putExtra("status",status);*/

        final String sender = extras.getString("sender");
        final String pname = extras.getString("packagename");
        final String weight = extras.getString("weight");
        final String type = extras.getString("type");
        final String reciever = extras.getString("recipient");
        packageNameText.setText("Package: "+pname);
        weightText.setText("Weight: " + weight);

        if (type.equals("0")) { //no intermediary
            senderText.setText("Sender: " + sender);
            recText.setText("Recipient: "+reciever);
        }
        else{ //intermediary
            String status = extras.getString("status");
            if (status.equals("")){ //first leg
                senderText.setText("Sender: " + sender);
                recText.setText("Intermediate: "+reciever);
            }
            else{ //secondleg
                senderText.setText("Intermediate: " + sender);
                recText.setText("Recipient: "+reciever);
            }
        }

        deleteB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AddFriendSQL sql = new AddFriendSQL();
                sql.execute("DELETE from packages where name=\'"+pname+"\'");
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriendSQL update = new AddFriendSQL();
                GetUserSQL check = new GetUserSQL();

                if (type.equals("0")) { //No intermediary
                    if (MainActivity.currentUser.equals(sender)) { //user is sender
                        check.execute("SELECT rconf,dconf FROM packages WHERE name=\'" + pname + "\'");

                        String value = "";

                        try {
                            value = check.get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                        /*Context context1 = getApplicationContext();
                        int duration1 = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context1,value, duration1);
                        toast.show();*/

                        if (value.equals("")) {
                            update.execute("UPDATE packages SET dconf = '1' where name=\'" + pname + "\'");
                        } else {
                            update.execute("DELETE from packages where name=\'" + pname + "\'");
                            finish();
                        }
                    }
                    else { //where user is recipient
                            check.execute("SELECT dconf,rconf FROM packages WHERE name=\'" + pname + "\'");

                            String value = "";

                            try {
                                value = check.get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                        /*Context context1 = getApplicationContext();
                        int duration1 = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context1,value, duration1);
                        toast.show();*/

                            if (value.equals("")) {
                                update.execute("UPDATE packages SET rconf = '1' where name=\'" + pname + "\'");
                            } else {
                                update.execute("DELETE from packages where name=\'" + pname + "\'");
                                finish();
                            }
                    }
                }

                else{ //intermediary
                    String status = extras.getString("status");
                    if (status.equals("")){
                        if (MainActivity.currentUser.equals(sender)) { //user is sender
                            check.execute("SELECT rconf,dconf FROM packages WHERE name=\'" + pname + "\'");

                            String value = "";

                            try {
                                value = check.get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                        /*Context context1 = getApplicationContext();
                        int duration1 = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context1,value, duration1);
                        toast.show();*/

                            if (value.equals("")) {
                                update.execute("UPDATE packages SET dconf = '1' where name=\'" + pname + "\'");
                            } else {
                                update.execute("UPDATE packages set dconf = '', rconf = '', status = '1' where name=\'" + pname + "\'");
                                finish();
                            }
                        }
                        else if (MainActivity.currentUser.equals(reciever)){ //where user is recipient
                            check.execute("SELECT dconf,rconf FROM packages WHERE name=\'" + pname + "\'");

                            String value = "";

                            try {
                                value = check.get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                        /*Context context1 = getApplicationContext();
                        int duration1 = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context1,value, duration1);
                        toast.show();*/

                            if (value.equals("")) {
                                update.execute("UPDATE packages SET rconf = '1' where name=\'" + pname + "\'");
                            } else {
                                update.execute("UPDATE packages set dconf = '', rconf = '', status = '1' where name=\'" + pname + "\'");
                                finish();
                            }
                        }
                    }
                    else {
                        if (MainActivity.currentUser.equals(sender)) { //user is sender
                            check.execute("SELECT rconf,dconf FROM packages WHERE name=\'" + pname + "\'");

                            String value = "";

                            try {
                                value = check.get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                        /*Context context1 = getApplicationContext();
                        int duration1 = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context1,value, duration1);
                        toast.show();*/

                            if (value.equals("")) {
                                update.execute("UPDATE packages SET dconf = '1' where name=\'" + pname + "\'");
                            } else {
                                update.execute("DELETE from packages where name=\'" + pname + "\'");
                                finish();
                            }
                        }
                        else if (MainActivity.currentUser.equals(reciever)) { //where user is recipient
                            check.execute("SELECT dconf,rconf FROM packages WHERE name=\'" + pname + "\'");

                            String value = "";

                            try {
                                value = check.get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                        /*Context context1 = getApplicationContext();
                        int duration1 = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context1,value, duration1);
                        toast.show();*/

                            if (value.equals("")) {
                                update.execute("UPDATE packages SET rconf = '1' where name=\'" + pname + "\'");
                            } else {
                                update.execute("DELETE from packages where name=\'" + pname + "\'");
                                finish();
                            }
                        }
                    }
                }

                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, "Delivery Confirmed!", duration);
                toast.show();
            }

        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_package_view, menu);
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
