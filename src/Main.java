import com.user_sql_connection.UserSqlConnection;
import com.user_sql_connection.ColumnName;

public class Main{
    public static void main(String[] args){
        UserSqlConnection connected = UserSqlConnection.stablishConnection();
        connected.updateDatabase(ColumnName.CITY, "rajarshi");
        connected.showStudentTable();
        //connected.deleteFromDatabase("shovona");
        //connected.showStudentTable();
        connected.closeConnection();
    }
}