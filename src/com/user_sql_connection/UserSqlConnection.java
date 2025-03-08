package com.user_sql_connection;

import java.sql.Connection;
//import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import com.mysql.cj.jdbc.MysqlDataSource;

//import com.user_sql_connection.ColumnName;
import java.util.Scanner;

public class UserSqlConnection {
    Connection connection = null;
    PreparedStatement statement = null;
    String query = null;
    Scanner sc = new Scanner(System.in);
    private UserSqlConnection(){
        MysqlDataSource connect = new MysqlDataSource();
        connect.setServerName("localhost");
        connect.setPort(3306);
        connect.setUser("root");
        connect.setPassword("root30082004");
        connect.setDatabaseName("rajarshiDatabase");

        try{
            connection = connect.getConnection();
            System.out.println("Connection successfull....");
        } catch(SQLException s) {
            System.out.println(s);
        }
    }

    public static UserSqlConnection stablishConnection(){
        return new UserSqlConnection();
    }

    public void updateDatabase(ColumnName column, String name){
        //query = "UPDATE STUDENT SET %s = %s WHERE NAME = %s";
        String value = null;
        int integerValue = 0;
        name = name.toUpperCase();
        switch(column){
            case ROLL_NO: {
                System.out.print("Enter the new roll number: ");
                int rollNo = sc.nextInt();
                integerValue = rollNo;
                query = "UPDATE STUDENT SET ROLL_NO = ? WHERE NAME = ?";
                break;
            } case NAME: {
                System.out.print("Enter the new Name: ");
                String newName = sc.next().toUpperCase();
                value = newName;
                query = "UPDATE STUDENT SET NAME = ? WHERE NAME = ?";
                break;
            } case CLASS: {
                System.out.print("Enter the new class: ");
                char newClass = sc.next().charAt(0);
                value = Character.toString(newClass);
                query = "UPDATE STUDENT SET CLASS = ? WHERE NAME = ?";
                break;
            } case DOB: {
                System.out.print("Enter the day: ");
                int day = sc.nextInt();

                System.out.print("\nEnter the month: ");
                int month = sc.nextInt();

                System.out.print("\nEnter the year: ");
                int year = sc.nextInt();

                String dob = String.format("%d-%d-%d", year, month, day);
                value = dob;
                query = "UPDATE STUDENT SET DOB = ? WHERE NAME = ?";
                break;
            } case CITY: {
                System.out.print("Enter the city name: ");
                String city = sc.next().toUpperCase();
                value = city;
                query = "UPDATE STUDENT SET CITY = ? WHERE NAME = ?";
                break;
            }
            default:
                System.out.println("Invalid column name");
                break;
        }
        
        try{
            PreparedStatement statement = connection.prepareStatement(query);
            if(integerValue == 0){
                statement.setString(1, value);
            } else {
                statement.setInt(1, integerValue);
            }
            statement.setString(2, name);
            int execute = statement.executeUpdate();
            if(execute > 0){
                System.out.println("Database updated successfully....");
            } else {
                System.out.println("Database update has been failed...");
            }
        } catch(SQLException s) {
            System.out.println(s);
        }
    }

    public void deleteFromDatabase(String name){
        name = name.toUpperCase();
        query = "DELETE FROM STUDENT WHERE NAME = ?";
        try{
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            int execute = statement.executeUpdate();
            if(execute > 0){
                System.out.println("Data deleted successfully....");
            } else {
                System.out.println("Data deletion has been failed....");
            }
        } catch(SQLException s){
            System.out.println(s);
        }
    }

    public void showStudentTable(){
        query = """
                SELECT ROLL_NO, NAME, CLASS, DATE_FORMAT(DOB, '%Y-%M-%D') AS DOB, 
                GENDER, CITY, MARKS FROM STUDENT
                """;
        try{
            statement = connection.prepareStatement(query);
            ResultSet execute = statement.executeQuery();
            ResultSetMetaData data = execute.getMetaData();

            //The error "Before start of result set" 
            //occurs because you are trying to access 
            //the data in the ResultSet before calling 
            //next() on it. You need to call next() to 
            //move the cursor to the first row of the 
            //result set before accessing the data.

            int columnCount = data.getColumnCount();
            for(int i = 1; i <= columnCount; i++){
                //cannot execute Resultset<variable> before declearing resultset<variable>.next()
                String displayColumn = String.format("|%-20s", data.getColumnName(i));
                System.out.print(displayColumn);
            }
            System.out.println();

            while(execute.next()){
                for(int i = 1; i <= columnCount; i++){
                    String displayColumn = String.format("|%-20s", execute.getString(i));
                    System.out.print(displayColumn);
                }
                System.out.println();
            }
        } catch(SQLException s) {
            System.out.println(s);
        }
    }

    public void closeConnection(){
        try{
            if(connection != null && !connection.isClosed()){
                connection.close();
                System.out.println("Connection closed successfully....");
            } else {
                System.out.println("Connection is already closed....");
            }
        } catch(SQLException s) {
            System.out.println(s);
        }
    }
}
