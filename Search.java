import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Search {
  private static final String DB_URL = "jdbc:mysql://localhost:3306/transactions";
  private static final String DB_USER = "root";
  private static final String DB_PASSWORD = "password";

  public List<Transaction> searchTransactions(String searchTerm) {
    List<Transaction> results = new ArrayList<>();
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
      String sql = "SELECT * FROM transactions WHERE description LIKE ? ";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1, "%" + searchTerm + "%");
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int id = rs.getInt("id");
        int userId = rs.getInt("user_id");
        String description = rs.getString("description");
        double amount = rs.getDouble("amount");
        boolean isExpense = rs.getBoolean("is_expense");
        Date date = rs.getDate("date");
        results.add(new Transaction(id, userId, description, amount, isExpense, date));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return results;
  }
}
