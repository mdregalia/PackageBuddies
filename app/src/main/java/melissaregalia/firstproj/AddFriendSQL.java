package melissaregalia.firstproj;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Akanksha Trivedi on 5/30/2015.
 */
public class AddFriendSQL extends AsyncTask<String, Void, String>
{
    private static final String POSTGRESS_DRIVER = "org.postgresql.Driver";

    public AddFriendSQL ()
    {

    }

    @Override
    protected String doInBackground(String... params)
    {
        String retval = "";
        try
        {
            Class.forName(POSTGRESS_DRIVER);
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            retval = e.toString();
        }
        String url = "jdbc:postgresql://ec2-174-129-26-115.compute-1.amazonaws.com:5432/d4hp0ep351mjmr?user=mlcqisdxxxgoct&password=D1Cu5DZU0oi9Vy1L5QjY3WsbHU&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        Connection conn;

        try
        {
            DriverManager.setLoginTimeout(5);
            conn = DriverManager.getConnection(url);
            Statement st = conn.createStatement();
            String sql;
            sql = params[0]; //string for getting user
            ResultSet rs = st.executeQuery(sql);

            String user = "";
            while (rs.next())
            {
                user = (rs.getString(0)).trim();
            }
            rs.close();

            retval = user;

            st.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
            retval = e.toString();
        }
        return retval;
    }
    @Override
    protected void onPostExecute(String value)
    {

    }
}

