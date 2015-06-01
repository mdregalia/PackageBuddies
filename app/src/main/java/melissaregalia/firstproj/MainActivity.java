package melissaregalia.firstproj;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;

import java.sql.*;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity {

    private static final String POSTGRESS_DRIVER = "org.postgresql.Driver";

    public static String currentUser = "";

    Activity mSelf = this;
    TextView resultArea;
    Button nextActivityButton;
    Button makeUserButton;
    EditText loginline;
    EditText passline;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultArea = (TextView) findViewById(R.id.text_view);
        nextActivityButton = (Button) findViewById(R.id.next_activity_button);
        makeUserButton = (Button) findViewById(R.id.make_new_user_button);
        loginline = (EditText) findViewById(R.id.enter_username);
        passline = (EditText) findViewById(R.id.enter_password);
        resultArea.setText("Please Login");//(getString(R.string.please_wait_message));
        //FetchSQL s = new FetchSQL(resultArea);
        //s.execute("SELECT * from \"dummyTable\" WHERE id=1");

        //When the LOGIN button is clicked it now does something
        nextActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //if they click on the Login button
                //check that login isn't void
                //verify that login is in database
                FetchSQL s = new FetchSQL(resultArea);
                String loginname = loginline.getText().toString();
                String password = passline.getText().toString();
                String value = "";

                if (loginname.equals("")){
                    return;
                }
                if (password.equals("")){
                    return;
                }
                //resultArea.setText(loginname);
                s.execute("SELECT * from login_info WHERE username=\'"+loginname+"\'",password);
                //Log.d(MainActivity.class.getSimpleName(), "success " + s.success);

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

                if(value.equals(""))
                {
                    resultArea.setText("Username does not exist");
                }
                else if (!value.equals(password))
                {
                    resultArea.setText("Incorrect Password");
                }
                else
                {
                    resultArea.setText("Login Successful");
                    currentUser = loginname;
                    Intent intent = new Intent(mSelf, HomeScreen.class);
                    startActivity(intent);
                };

                /*
                if(s.success == 1)
                {
                    Intent intent = new Intent(mSelf, HomeScreen.class);
                    startActivity(intent);
                }
                */
            }
        });


        //When the new user button is clicked it does something
        makeUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //if they click on the Login button
                //check that login isn't void
                //verify that login is in database
                NewUserSQL s = new NewUserSQL(resultArea);
                String loginname = loginline.getText().toString();
                String password = passline.getText().toString();
                String value = "";
                if (loginname.equals("")){
                    return;
                }
                if (password.equals("")){
                    return;
                }
                //resultArea.setText(loginname);
                s.execute("INSERT INTO login_info VALUES (\'"+loginname+"\',\'"+password+"\')",loginname);

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

                if(value.equals("exists"))
                {
                    resultArea.setText("Username already exists");
                }
                else if (value.equals("failed to add"))
                {
                    resultArea.setText("Re-try");
                }
                else
                {
                    resultArea.setText("Account created!");
                    currentUser = loginname;
                    Intent intent = new Intent(mSelf, HomeScreen.class);
                    startActivity(intent);
                };
                /*Intent intent = new Intent(mSelf, SecondActivity.class);
                startActivity(intent);*/
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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