package melissaregalia.firstproj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;


public class FriendScreen extends ActionBarActivity {

    TextView resultArea;
    Activity mSelf = this;
    Button addFriendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_screen);

        resultArea = (TextView) findViewById(R.id.friendTextView);
        addFriendButton = (Button) findViewById(R.id.addFriendButton);

        addFriendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // from http://www.androidsnippets.com/prompt-user-input-with-an-alertdialog
                final AlertDialog.Builder alert = new AlertDialog.Builder(mSelf);

                //alert.setTitle("Title");
                alert.setTitle("Enter the username of the friend you would like to add.");
                alert.setMessage("");

                // Set an EditText view to get user input
                final EditText input = new EditText(mSelf);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton)
                    {

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                // from http://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked
                final AlertDialog dialog = alert.create();
                dialog.show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String user = input.getText().toString();
                        boolean success = false;

                        GetUserSQL s = new GetUserSQL();
                        s.execute("SELECT username FROM login_info WHERE username=\'"+user+"\'");

                        String value = "";
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

                        if(value.equals(MainActivity.currentUser))
                            dialog.setMessage("You cannot add yourself as a friend.");
                        else if (value.equals(""))
                            dialog.setMessage("User \'" + user + "\' does not exist.");
                        else
                        {
                            s = new GetUserSQL();
                            s.execute("SELECT user1 FROM friends WHERE (user1=\'"+MainActivity.currentUser+"\' AND user2=\'"+user+"\')");
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

                            if(value.equals(MainActivity.currentUser))
                            {
                                dialog.setMessage("You are already friends with this user.");
                            }
                            else
                            {
                                s = new GetUserSQL();
                                s.execute("SELECT asker FROM pending_friends WHERE (asker=\'"+MainActivity.currentUser+"\' AND responder=\'"+user+"\')");
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

                                if(value.equals(MainActivity.currentUser))
                                {
                                    dialog.setMessage("You have already requested to add this user as a friend.");
                                }
                                else
                                {
                                    s = new GetUserSQL();
                                    s.execute("SELECT responder FROM pending_friends WHERE (responder=\'" + MainActivity.currentUser + "\' AND asker=\'" + user + "\')");
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

                                    if(value.equals(MainActivity.currentUser))
                                    {
                                        // this means the person requested someone who already requested them
                                        //dialog.setMessage("You and user \'" + user + "\' are now friends!");
                                        // need to remove the pending friendship from pending_friends
                                        s = new GetUserSQL();
                                        s.execute("DELETE FROM pending_friends WHERE (responder=\'" + MainActivity.currentUser + "\' AND asker=\'" + user + "\')");
                                        // need to add the friendship both ways to friends
                                        s = new GetUserSQL();
                                        s.execute("INSERT INTO friends VALUES (\'" + MainActivity.currentUser + "\',\'" + user + "\')");
                                        s = new GetUserSQL();
                                        s.execute("INSERT INTO friends VALUES (\'" + user + "\',\'" + MainActivity.currentUser + "\')");

                                        dialog.dismiss();

                                        AlertDialog.Builder dialog2 = new AlertDialog.Builder(mSelf);
                                        //alert.setTitle("Title");
                                        dialog2.setTitle("You and user \'" + user + "\' are now friends!");

                                        dialog2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {

                                            }
                                        });

                                        dialog2.show();
                                    }
                                    else
                                    {
                                        // need to add the pending friendship here
                                        //dialog.setMessage("You have successfully sent a friend request to user \'" + user + "\'.");
                                        s = new GetUserSQL();
                                        s.execute("INSERT INTO pending_friends VALUES (\'" + MainActivity.currentUser + "\',\'" + user + "\')");

                                        dialog.dismiss();

                                        AlertDialog.Builder dialog2 = new AlertDialog.Builder(mSelf);
                                        //alert.setTitle("Title");
                                        dialog2.setTitle("You have successfully sent a friend request to user \'" + user + "\'.");

                                        dialog2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {

                                            }
                                        });

                                        dialog2.show();
                                    }
                                }
                            }
                        }
                    }
                });
            }
        });
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_screen, menu);
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
