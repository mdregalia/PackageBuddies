
/**
 * Created by melissaregalia on 5/30/15.
 */

package melissaregalia.firstproj;
import android.os.AsyncTask;
import android.widget.TextView;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NewUserSQL extends AsyncTask<String, Void, String> {
    private static final String POSTGRESS_DRIVER = "org.postgresql.Driver";

    TextView resultArea;
    String mQuery;
    String password;

    //here is the intialization arguments - pass in a textview to initialize it
    //make this an error slot
    public NewUserSQL(TextView textView) {
        this.resultArea = textView; //error box
        //this.mQuery = myString; //password user entered
    }

    @Override
    protected String doInBackground(String... params) {
        String retval = "";
        try {
            Class.forName(POSTGRESS_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            retval = e.toString();
        }
        String url = "jdbc:postgresql://ec2-174-129-26-115.compute-1.amazonaws.com:5432/d4hp0ep351mjmr?user=mlcqisdxxxgoct&password=D1Cu5DZU0oi9Vy1L5QjY3WsbHU&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
        Connection conn;
        try {
            DriverManager.setLoginTimeout(5);
            conn = DriverManager.getConnection(url);
            Statement st = conn.createStatement();
            String sql;
            sql = params[0];//"SELECT * from \"dummyTable\" WHERE id=1";
            password = params[1];
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                //FOR SOME REASON THIS ADDS SOME WHITESPACE
                retval = (rs.getString("password")).trim();
                //int temp =
                //retval = rs.getInt("dataVal");
            }
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            retval = e.toString();
        }
        return retval;
    }
    @Override
    protected void onPostExecute(String value) {
        //if there is no such id then it is empty
        if(value == "" ){
            resultArea.setText("No such id");
        }
        else if (!value.equals(password)){ //
            resultArea.setText("Incorrect Password");
        }
        else {
            resultArea.setText("WORKS");
        };
    }
}

