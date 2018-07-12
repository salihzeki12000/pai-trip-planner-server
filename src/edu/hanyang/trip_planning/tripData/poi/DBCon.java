package edu.hanyang.trip_planning.tripData.poi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 7. 20
 * Time: 오후 12:45
 * To change this template use File | Settings | File Templates.
 */
public class DBCon {
    static String url;

    public static void main(String[] args) {
        try {

            String driverName = "org.gjt.mm.mysql.Driver"; // 드라이버 이름 지정
            String DBName = "testdb";
            String dbURL = "jdbc:mysql://localhost:3306/" + DBName; // URL 지정
            String userID = "wykwon";
            String userPassword = "20071010";
            String tableName = "poi";
            String SQL = "select * from poi;";


            Class.forName(driverName); // 드라이버 로드
            Connection con = DriverManager.getConnection(dbURL, userID, userPassword); // 연결
            System.out.println("Mysql DB Connection.");

            Statement stmt = con.createStatement();
            //      stmt.executeUpdate("CREATE DATABASE noondb");
            //      System.out.println("데이터 베이스가 mydb가 생성되었습니다.");

            //      stmt.executeUpdate(sqlCT);
            System.out.println("Table Created");
            //data Insert
                /*
                stmt.executeUpdate("insert into STUDENT values('01','Noon',20100909,'Security');");
                stmt.executeUpdate("insert into STUDENT values('02','Bom',20100909,'IT');");
                stmt.executeUpdate("insert into STUDENT values('03','Rye',20100909,'Devel');");
                stmt.executeUpdate("insert into STUDENT values('04','Kim',20100909,'Random');");
                System.out.println("Insert Data");*/

            stmt.executeQuery(SQL);
            ResultSet result = stmt.executeQuery(SQL);

            while (result.next()) {
                System.out.print(result.getString(1) + "\t");
                System.out.print(result.getString(2) + "\t");
                System.out.print(result.getString(3) + "\t");
                System.out.print(result.getString(4) + "\t");
                System.out.print(result.getString(5) + "\t");
                System.out.print(result.getString(6) + "\n");
            }

            con.close();
        } catch (Exception e) {
            System.out.println("Mysql Server Not Connection.");
            e.printStackTrace();
        }
    }

}
