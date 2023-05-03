import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Export {
  private static final String DB_URL = "jdbc:mysql://localhost:3306/transactions";
  private static final String DB_USER = "root";
  private static final String DB_PASSWORD = "password";

  public void exportTransactionsToCSV(String fileName) {
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
      String sql = "SELECT * FROM transactions";
      PreparedStatement stmt = conn.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      FileWriter csvWriter = new FileWriter(fileName);
      csvWriter.append("ID,User ID,Description,Amount,Is Expense,Date\n");
      while (rs.next()) {
        csvWriter.append(String.valueOf(rs.getInt("id"))).append(",");
        csvWriter.append(String.valueOf(rs.getInt("user_id"))).append(",");
        csvWriter.append(rs.getString("description")).append(",");
        csvWriter.append(String.valueOf(rs.getDouble("amount"))).append(",");
        csvWriter.append(rs.getBoolean("is_expense") ? "true" : "false").append(",");
        csvWriter.append(rs.getString("date")).append("\n");
      }
      csvWriter.flush();
      csvWriter.close();
      System.out.println("Transactions exported successfully to " + fileName);
    } catch (Exception e) {
      System.out.println("Error exporting transactions: " + e.getMessage());
    }
  }
  public static List<Transaction> getTrans(int userId) {
	    List<Transaction> transactions = new ArrayList<>();
	    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
	        String sql = "SELECT * FROM transactions WHERE user_id = ?";
	        PreparedStatement stmt = conn.prepareStatement(sql);
	        stmt.setInt(1, userId);
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            int id = rs.getInt("id");
	            double amount = rs.getDouble("amount");
	            boolean isExpense = rs.getBoolean("is_expense");
	            Date date = rs.getDate("date");
	            Transaction transaction = new Transaction(id, userId, rs.getString("description"), amount, isExpense, date);
	            transactions.add(transaction);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return transactions;
	}

  public boolean exportFinancialReport(int userId, String format) {
    try {
      // Retrieve all transactions for the given user
      List<Transaction> transactions = getTrans(userId);

      // Create a file chooser to allow the user to select where to save the CSV file
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setDialogTitle("Save Financial Report");
      int userSelection = fileChooser.showSaveDialog(null);
      if (userSelection == JFileChooser.APPROVE_OPTION) {
        File fileToSave = fileChooser.getSelectedFile();

        // Create a PrintWriter to write the transactions to the CSV file
        PrintWriter writer = new PrintWriter(fileToSave);

        // Write the CSV header row
        writer.println("Date,Description,Amount,Is Expense");

        // Write each transaction to the CSV file
        for (Transaction transaction : transactions) {
          writer.println(transaction.getDate() + "," +
                         transaction.getDescription() + "," +
                         transaction.getAmount() + "," +
                         transaction.getIsExpense());
        }

        writer.close();
        System.out.println("Financial report exported to " + fileToSave.getAbsolutePath());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }
}
