import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import javax.swing.table.DefaultTableModel;

public class BudgetManager {
  private static final String DB_URL = "jdbc:mysql://localhost:3306/budgets";
  private static final String DB_USER = "root";
  private static final String DB_PASSWORD = "password";
  
  public void addBudget(Budget budget) {
	    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
	      String sql = "INSERT INTO budgets (name, amount, is_expense, user_id) VALUES (?, ?, ?, ?)";
	      PreparedStatement stmt = conn.prepareStatement(sql);
	      stmt.setString(1, budget.getName());
	      stmt.setDouble(2, budget.getAmount());
	      stmt.setBoolean(3, budget.getIsExpense());
	      stmt.setInt(4, budget.getUserId());
	      stmt.executeUpdate();
	    } catch (SQLException e) {
	      e.printStackTrace();
	    }
	  }



  public void editBudget(int budgetId, String name, double limit) {
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
      String sql = "UPDATE budgets SET name = ?, limit = ? WHERE id = ?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1, name);
      stmt.setDouble(2, limit);
      stmt.setInt(3, budgetId);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteBudget(int budgetId) {
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
      String sql = "DELETE FROM budgets WHERE id = ?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, budgetId);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  public double getTotalBudget(int userId) {
	    double totalBudget = 0.0;
	    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
	        String sql = "SELECT SUM(limit) FROM budgets";
	        PreparedStatement stmt = conn.prepareStatement(sql);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            totalBudget = rs.getDouble(1);
	        }
	    } catch (SQLException e) {
	        System.out.println("Error retrieving total budget: " + e.getMessage());
	    }
	    return totalBudget;
	}
  
  public DefaultTableModel getTableModel(int userId) {
	  String[] columnNames = {"ID", "Name", "Limit", "Current Spending"};
	  DefaultTableModel model = new DefaultTableModel(columnNames, 0);

	  try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
	    String sql = "SELECT b.id, b.name, b.limit, COALESCE(SUM(t.amount), 0) AS spending " +
	                 "FROM budgets b LEFT JOIN transactions.transactions t ON b.user_id = t.user_id " +
	                 "WHERE b.user_id = ? GROUP BY b.id";
	    PreparedStatement stmt = conn.prepareStatement(sql);
	    stmt.setInt(1, userId);
	    ResultSet rs = stmt.executeQuery();

	    while (rs.next()) {
	      int id = rs.getInt("id");
	      String name = rs.getString("name");
	      double limit = rs.getDouble("limit");
	      double spending = rs.getDouble("spending");
	      Object[] row = {id, name, limit, spending};
	      model.addRow(row);
	    }
	  } catch (SQLException e) {
	    e.printStackTrace();
	  }

	  return model;
	}
  
  public Budget getBudget(int budgetId) {
	    Budget budget = null;
	    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
	        String sql = "SELECT * FROM budgets WHERE id = ?";
	        PreparedStatement stmt = conn.prepareStatement(sql);
	        stmt.setInt(1, budgetId);
	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            int userId = rs.getInt("user_id");
	            String name = rs.getString("name");
	            double limit = rs.getDouble("limit");
	            boolean isExpense = rs.getBoolean("is_expense");
	            budget = new Budget( name,  limit,  isExpense,  userId);
	        }
	    } catch (SQLException e) {
	        System.out.println("Error retrieving budget: " + e.getMessage());
	    }
	    return budget;
	}
//  public void addBudget(int userId, String name, double amount, boolean isExpense) {
//	    try {
//	        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
//	        String sql = "INSERT INTO budgets (user_id, name, amount, is_expense) VALUES (?, ?, ?, ?)";
//	        PreparedStatement stmt = conn.prepareStatement(sql);
//	        stmt.setInt(1, userId);
//	        stmt.setString(2, name);
//	        stmt.setDouble(3, amount);
//	        stmt.setBoolean(4, isExpense);
//	        stmt.executeUpdate();
//	        conn.close();
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//	}


  public static void addBudget(int currentUserId, String name, double amount, boolean isExpense) {
	    try {
	        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	        String sql = "INSERT INTO budgets (user_id, name, amount, is_expense) VALUES (?, ?, ?, ?)";
	        PreparedStatement pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, currentUserId);
	        pstmt.setString(2, name);
	        pstmt.setDouble(3, amount);
	        pstmt.setBoolean(4, isExpense);
	        pstmt.executeUpdate();
	        pstmt.close();
	        conn.close();
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }
	}

}
