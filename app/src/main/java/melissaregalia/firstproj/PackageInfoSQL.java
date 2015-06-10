package melissaregalia.firstproj;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by melissaregalia on 6/6/15.
 */
public class PackageInfoSQL extends AsyncTask<String, Void, List<String>> {
    private static final String POSTGRESS_DRIVER = "org.postgresql.Driver";

    public PackageInfoSQL ()
    {

    }

    @Override
    protected List<String> doInBackground(String... params)
    {
        List<String> retval = new ArrayList<String>();
        try
        {
            Class.forName(POSTGRESS_DRIVER);
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            retval.add(e.toString());
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

            //String weight = "";
            while (rs.next())
            {
                retval.add(rs.getString("weight").trim());
                retval.add(rs.getString("intermediary").trim());
                retval.add(rs.getString("rec_location").trim());
                retval.add(rs.getString("int_location").trim());
                retval.add(rs.getString("sender").trim());
                retval.add(rs.getString("recipient").trim());
                retval.add(rs.getString("status").trim());
                //retval.add(rs.getString("intermediary").trim());
                //retval.add(rs.getString("weight").trim());
                //retval.add(rs.getString("rec_location").trim());
                //retval.add(rs.getString("int_location").trim());
                //retval.add(rs.getString("status").trim());
                //add others
            }
            rs.close();

            //retval = weight;

            st.close();
            conn.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
            retval.add(e.toString());
        }
        return retval;
    }
    @Override
    protected void onPostExecute(List<String> value)
    {

    }
}