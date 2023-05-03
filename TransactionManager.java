import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class TransactionManager {
  private static final String DB_URL = "jdbc:mysql://localhost:3306/transactions";
  private static final String DB_USER = "root";
  private static final String DB_PASSWORD = "password";

  public void addTransaction(int userId, String description, double amount, boolean isExpense) {
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
      String sql = "INSERT INTO transactions (user_id, description, amount, is_expense) VALUES (?, ?, ?, ?)";
      PreparedStatement stmt = conn.prepareStatement(sql);
      //stmt.setInt(stmt.setInt(1, userId));
      stmt.setString(2, description);
      stmt.setDouble(3, amount);
      stmt.setBoolean(4, isExpense);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  public double getTotalIncome(int userId) {
	    double totalIncome = 0;

	    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
	        String sql = "SELECT SUM(amount) FROM transactions WHERE user_id = ? AND is_expense = false";
	        PreparedStatement stmt = conn.prepareStatement(sql);
	        stmt.setInt(1, userId);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            totalIncome = rs.getDouble(1);
	        }
	    } catch (SQLException ex) {
	        System.out.println("Error occurred while getting total income: " + ex.getMessage());
	    }

	    return totalIncome;
	}


  public void editTransaction(int transactionId, String description, double amount, boolean isExpense) {
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
      String sql = "UPDATE transactions SET description = ?, amount = ?, is_expense = ? WHERE id = ?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1, description);
      stmt.setDouble(2, amount);
      stmt.setBoolean(3, isExpense);
      stmt.setInt(4, transactionId);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteTransaction(int transactionId) {
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
      String sql = "DELETE FROM transactions WHERE id = ?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, transactionId);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  public double getTotalExpense(int userId) {
	    double totalExpense = 0;
	    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
	      String sql = "SELECT SUM(amount) AS total_expense FROM transactions WHERE user_id = ? AND is_expense = true";
	      PreparedStatement stmt = conn.prepareStatement(sql);
	      stmt.setInt(1, userId);
	      ResultSet rs = stmt.executeQuery();

	      if (rs.next()) {
	        totalExpense = rs.getDouble("total_expense");
	      }
	    } catch (SQLException e) {
	      e.printStackTrace();
	    }
	    return totalExpense;
	}
  
  public double getTotalBudget(int userId) {
	    double totalIncome = getTotalIncome(userId);
	    double totalExpense = getTotalExpense(userId);
	    return totalIncome - totalExpense;
	}


}

