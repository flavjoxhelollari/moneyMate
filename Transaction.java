
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.DriverManager;
import java.sql.PreparedStatement;


public class Transaction {
	
	private static String DB_URL = "jdbc:mysql://localhost:3306/transactions";
	private static final String DB_USER = "root";
	private static final String DB_PASSWORD = "password";
	
    private int id;
    private int userId;
    private String description;
    private double amount;
    private boolean isExpense;
    private Date date;

    public Transaction(int id, int userId, String description, double amount, boolean isExpense, Date date) {
        this.id = id;
        this.userId = userId;
        this.description = description;
        this.amount = amount;
        this.isExpense = isExpense;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    public boolean getIsExpense() {
        return isExpense;
    }

    public Date getDate() {
        return date;
    }

	public Object isExpense() {
		// TODO Auto-generated method stub
		return this.isExpense;
	}
	
	public static List<Transaction> getTransactions(int userId) {
	    List<Transaction> transactions = new ArrayList<>();
	    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
	        String sql = "SELECT * FROM transactions WHERE user_id = ?";
	        PreparedStatement stmt = conn.prepareStatement(sql);
	        stmt.setInt(1, userId);
	        ResultSet rs = stmt.executeQuery();
	        while (rs.next()) {
	            int id = rs.getInt("id");
	            String name = rs.getString("name");
	            double amount = rs.getDouble("amount");
	            boolean isExpense = rs.getBoolean("is_expense");
	            Date date = rs.getDate("date");
	            Transaction transaction = new Transaction(id, userId, name, amount, isExpense, date);
	            transactions.add(transaction);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return transactions;
	}

}
