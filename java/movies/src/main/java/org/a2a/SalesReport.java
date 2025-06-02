package org.a2a;

import java.sql.*;
import java.util.Scanner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class SalesReport {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/mydb";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "secret";

    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter date (MM/DD/YYYY)");
        String inputDate = scanner.nextLine().trim();
        try{
            String validatedDate = validate_input_and_update_date(inputDate);
            queryTopSales(validatedDate);
        } catch (ParseException e) {
            System.err.println("Date must be in MM/DD/YYYY format");
        }


    }

    public static void queryTopSales(String targetDate) {
        String query = """
            SELECT
                s.show_date,
                t.name as theatre_name ,
                SUM(tickets * ticket_price) as total_sales
            FROM showings s
            JOIN theatres t ON s.theatre_id = t.theatre_id
            WHERE s.show_date = ?
            GROUP by s.show_date, t.name
            ORDER BY total_sales DESC
            LIMIT 1
	    """;

        try(Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setDate(1, java.sql.Date.valueOf(targetDate));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String date = rs.getString("show_date");
                String theatre = rs.getString("theatre_name");
                double sales = rs.getDouble("total_sales");

                System.out.printf("Date: " + date + "\nTheatre: " + theatre + "\nSales: $" + sales);
            }
        }
        catch( SQLException e){
            System.err.println("Database Error: " + e.getMessage());
        }




    }

    public static String validate_input_and_update_date(String inputDate) throws ParseException {
        SimpleDateFormat fromFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat toFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = fromFormat.parse(inputDate);
        return toFormat.format(date);
    }
}