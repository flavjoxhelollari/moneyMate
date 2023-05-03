import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JDialog;

public class FinanceManagerGUI extends JFrame {
/**	Main class
 * 	Used to fix GUI
 *  Includes helper methods that work with the database.
 * 
*/
	private static final long serialVersionUID = 1L;
protected static Object currentUser;
private final Login login = new Login();
private final TransactionManager transactionManager = new TransactionManager();
private final BudgetManager budgetManager = new BudgetManager();
private final Search search = new Search();
private final Export export = new Export();
private String DB_URL = "jdbc:mysql://localhost:3306/transactions";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = "password";

private final JTextField searchTermField = new JTextField(20);
private final JTextArea searchResultArea = new JTextArea(20, 50);

private int currentUserId;
private int workingID=2; 

public FinanceManagerGUI() {
	super("Personal Finance Manager");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setSize(800, 600);
	JTabbedPane tabbedPane = new JTabbedPane();
	tabbedPane.addTab("Login", createLoginPanel());
	tabbedPane.addTab("Dashboard", createDashboardPanel(workingID));
	tabbedPane.addTab("Transactions", createTransactionsPanel());
	tabbedPane.addTab("Budgets", createBudgetsPanel());
	tabbedPane.addTab("Search", createSearchPanel());
	tabbedPane.addTab("Export", createExportPanel());
	add(tabbedPane);
		
	}

public void addTransaction(int userId, String description, double amount, boolean isExpense) {
    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
      String sql = "INSERT INTO transactions (user_id, description, amount, is_expense) VALUES (?, ?, ?, ?)";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, userId);
      stmt.setString(2, description);
      stmt.setDouble(3, amount);
      stmt.setBoolean(4, isExpense);
      stmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

private JPanel createTransactionsPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // Create the title label
    JLabel titleLabel = new JLabel("Transactions");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
    panel.add(titleLabel, BorderLayout.NORTH);

    // Create the table model
    DefaultTableModel model = new DefaultTableModel(
        new String[] {"ID", "User ID", "Description", "Amount", "Is Expense", "Date"}, 0);

    // Populate the table model with data from the database
    List<Transaction> transactions = getTransactionsFromDatabase(workingID);
    for (Transaction t : transactions) {
      model.addRow(new Object[] {t.getId(), t.getUserId(), t.getDescription(), t.getAmount(),
          t.isExpense(), t.getDate()});
    }

    // Create the table and set the model
    JTable table = new JTable(model);
    table.setFont(new Font("Arial", Font.PLAIN, 18));
    table.setRowHeight(30);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
    table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 40));
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    panel.add(scrollPane, BorderLayout.CENTER);

    // Create the buttons panel
    JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
    buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
    panel.add(buttonsPanel, BorderLayout.SOUTH);

    // Create the add transaction button
    JButton addTransactionButton = new JButton("Add Transaction");
    addTransactionButton.setFont(new Font("Arial", Font.PLAIN, 18));
    addTransactionButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Frame frame = null;
			// Create a new dialog for entering transaction details
            JDialog dialog = new JDialog(frame, "Add Transaction", true);
            JPanel dialogPanel = new JPanel(new GridLayout(6, 2));
            JLabel userIdLabel = new JLabel("User ID:");
            JTextField userIdField = new JTextField();
            JLabel descriptionLabel = new JLabel("Description:");
            JTextField descriptionField = new JTextField();
            JLabel amountLabel = new JLabel("Amount:");
            JTextField amountField = new JTextField();
            JLabel isExpenseLabel = new JLabel("Is Expense:");
            JCheckBox isExpenseCheckbox = new JCheckBox();
            JLabel dateLabel = new JLabel("Date:");
            JTextField dateField = new JTextField();
            dialogPanel.add(userIdLabel);
            dialogPanel.add(userIdField);
            dialogPanel.add(descriptionLabel);
            dialogPanel.add(descriptionField);
            dialogPanel.add(amountLabel);
            dialogPanel.add(amountField);
            dialogPanel.add(isExpenseLabel);
            dialogPanel.add(isExpenseCheckbox);
            dialogPanel.add(dateLabel);
            dialogPanel.add(dateField);
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Get the transaction details from the dialog fields
                    int userId = Integer.parseInt(userIdField.getText());
                    String description = descriptionField.getText();
                    double amount = Double.parseDouble(amountField.getText());
                    boolean isExpense = isExpenseCheckbox.isSelected();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        @SuppressWarnings("unused")
						Date date = sdf.parse(dateField.getText());
                        // Do something with the date
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    // Add the new transaction to the database and update the table
                    addTransaction(userId, description, amount, isExpense);
                    updateTransactionsTable();
                    // Close the dialog
                    dialog.dispose();
                }

				private void updateTransactionsTable() {
					// TODO Auto-generated method stub
					
				}

              
            });
            dialogPanel.add(saveButton);
            dialog.add(dialogPanel);
            dialog.pack();
            dialog.setLocationRelativeTo(dialogPanel);
            dialog.setVisible(true);
        }
    });
    buttonsPanel.add(addTransactionButton);

    // Create the delete transaction button
    JButton deleteTransactionButton = new JButton("Delete Transaction");
    deleteTransactionButton.setFont(new Font("Arial", Font.PLAIN, 18));
    deleteTransactionButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int[] selectedRows = table.getSelectedRows();
            if (selectedRows.length > 0) {
                int option = JOptionPane.showConfirmDialog(panel, "Are you sure you want to delete the selected transactions?", "Delete Transactions", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        int selectedRow = selectedRows[i];
                        int transactionId = (int) table.getValueAt(selectedRow, 0);
                        deleteTransaction(transactionId);
                        model.removeRow(selectedRow);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Please select at least one transaction to delete.", "Delete Transactions", JOptionPane.INFORMATION_MESSAGE);
            }
        }

		private void deleteTransaction(int transactionId) {
			// TODO Auto-generated method stub
			 try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
			      String sql = "DELETE FROM transactions WHERE id = ?";
			      PreparedStatement stmt = conn.prepareStatement(sql);
			      stmt.setInt(1, transactionId);
			      stmt.executeUpdate();
			    } catch (SQLException e) {
			      e.printStackTrace();
			    }
		}
    });
    buttonsPanel.add(deleteTransactionButton);

    return panel;
}

private List<Transaction> getTransactionsFromDatabase(int userId) {
    List<Transaction> transactions = new ArrayList<>();

    try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
      String sql = "SELECT * FROM transactions WHERE user_id = ?";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, userId);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        int id = rs.getInt("id");
        String description = rs.getString("description");
        double amount = rs.getDouble("amount");
        boolean isExpense = rs.getBoolean("is_expense");
        Date date = rs.getDate("date");

        Transaction t = new Transaction(id, userId, description, amount, isExpense, date);
        transactions.add(t);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return transactions;
}

private JPanel createLoginPanel() {
    Dimension maxSize = new Dimension(200, 30);
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.insets = new Insets(5, 5, 5, 5);

    JLabel titleLabel = new JLabel("Finance App");
    Font titleFont = new Font("Helvetica", Font.BOLD, 30);
    titleLabel.setFont(titleFont);
    c.gridwidth = 2;
   // panel.add(titleLabel, c);

    JLabel usernameLabel = new JLabel("Username:");
    Font labelFont = new Font("Helvetica", Font.BOLD, 14);
    usernameLabel.setFont(labelFont);
    c.gridwidth = 1;
    c.gridy++;
    panel.add(usernameLabel, c);

    JTextField usernameField = new JTextField(20);
    usernameField.setPreferredSize(maxSize);
    c.gridx = 1;
    panel.add(usernameField, c);

    JLabel passwordLabel = new JLabel("Password:");
    passwordLabel.setFont(labelFont);
    c.gridx = 0;
    c.gridy++;
    panel.add(passwordLabel, c);

    JPasswordField passwordField = new JPasswordField(20);
    passwordField.setPreferredSize(maxSize);
    c.gridx = 1;
    panel.add(passwordField, c);

    JButton loginButton = new JButton("Login");
    loginButton.setPreferredSize(new Dimension(100, 30));
    c.gridx = 0;
    c.gridy++;
    c.gridwidth = 2;
    c.fill = GridBagConstraints.CENTER;
    panel.add(loginButton, c);

    JLabel messageLabel = new JLabel("");
    messageLabel.setFont(labelFont);
    messageLabel.setForeground(Color.RED);
    c.gridy++;
    panel.add(messageLabel, c);

    // Add a background image
    ImageIcon imageIcon = new ImageIcon("background.jpg");
    JLabel backgroundImage = new JLabel(imageIcon);
    backgroundImage.setSize(panel.getSize());
    panel.add(backgroundImage, new GridBagConstraints());
    
 // Create a JLabel to hold the GIF
    JLabel gifLabel = new JLabel();

    // Load the GIF file into an ImageIcon and resize it to 100x100
    ImageIcon gifIcon = new ImageIcon("load_screen.gif");
    Image gifImage = gifIcon.getImage().getScaledInstance(500, 350, Image.SCALE_DEFAULT);
    ImageIcon resizedGifIcon = new ImageIcon(gifImage);

    // Set the resized ImageIcon to the JLabel
    gifLabel.setIcon(resizedGifIcon);

    // Add the JLabel to your panel
    panel.add(gifLabel, new GridBagConstraints());

    // Add a transparent layer over the background image
    JPanel transparentPanel = new JPanel();
    transparentPanel.setBackground(new Color(255, 255, 255, 150));
    transparentPanel.setSize(panel.getSize());
    panel.add(transparentPanel, new GridBagConstraints());
    
    panel.setBackground(Color.WHITE); // set background to light blue

    loginButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            char[] passwordChars = passwordField.getPassword();
            String password = new String(passwordChars);
            boolean success = false;
            try {
                success = login.authenticate(username, password);
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
            if (success) {
                try {
                    currentUserId = login.getUserId(username);
                    workingID = currentUserId;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                messageLabel.setText("Login successful.");
                messageLabel.setForeground(new Color(0, 150, 0));
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        }
    });

    return panel;
}




private Component createDashboardPanel(int workingID) {
    JPanel panel = new JPanel(new BorderLayout());
    JLabel titleLabel = new JLabel("Dashboard");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setForeground(Color.WHITE);
//    titleLabel.setBackground(new Color(28, 90, 143));
    titleLabel.setBackground(Color.black);
    titleLabel.setOpaque(true);
    titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.WHITE));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setPreferredSize(new Dimension(0, 60));
    JPanel dataPanel = new JPanel(new GridLayout(3, 2, 10, 10));
    dataPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    panel.add(titleLabel, BorderLayout.NORTH);
    panel.add(dataPanel, BorderLayout.CENTER);

    // Create icon labels
    ImageIcon incomeIcon = new ImageIcon("incomeIcon.png");
    ImageIcon expenseIcon = new ImageIcon("expenseIcon.png");
    ImageIcon netIncomeIcon = new ImageIcon("netIncomeIcon.png");
    ImageIcon budgetIcon = new ImageIcon("budgetIcon.png");
    ImageIcon remainingBudgetIcon = new ImageIcon("remainingBudgetIcon.png");

    // Create data labels with icons
    JLabel totalIncomeLabel = new JLabel("Total Income:", incomeIcon, JLabel.LEFT);
    JLabel totalExpenseLabel = new JLabel("Total Expense:", expenseIcon, JLabel.LEFT);
    JLabel netIncomeLabel = new JLabel("Net Income:", netIncomeIcon, JLabel.LEFT);
    JLabel totalBudgetLabel = new JLabel("Total Budget:", budgetIcon, JLabel.LEFT);
    JLabel remainingBudgetLabel = new JLabel("Remaining Budget:", remainingBudgetIcon, JLabel.LEFT);

    // Set font size for data labels
    Font labelFont = new Font("Arial", Font.PLAIN, 10);
    totalIncomeLabel.setFont(labelFont);
    totalExpenseLabel.setFont(labelFont);
    netIncomeLabel.setFont(labelFont);
    totalBudgetLabel.setFont(labelFont);
    remainingBudgetLabel.setFont(labelFont);

    dataPanel.add(totalIncomeLabel);
    dataPanel.add(new JLabel(String.format("$%.2f", transactionManager.getTotalIncome(workingID))));
    dataPanel.add(totalExpenseLabel);
    dataPanel.add(new JLabel(String.format("$%.2f", transactionManager.getTotalExpense(workingID))));
    dataPanel.add(netIncomeLabel);
    dataPanel.add(new JLabel(String.format("$%.2f", transactionManager.getTotalIncome(workingID) - transactionManager.getTotalExpense(workingID))));
    dataPanel.add(totalBudgetLabel);
    dataPanel.add(new JLabel(String.format("$%.2f", budgetManager.getTotalBudget(workingID))));
    dataPanel.add(remainingBudgetLabel);
    dataPanel.add(new JLabel(String.format("$%.2f", budgetManager.getTotalBudget(workingID) - transactionManager.getTotalExpense(workingID))));

    // Create the screenshot button and add an ActionListener to it
    JButton screenshotButton = new JButton("Save Screenshot");
    screenshotButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Get the size and position of the dashboard panel
                Point panelLocation = panel.getLocationOnScreen();
                Dimension panelSize = panel.getSize();

                // Create a rectangle that covers just the dashboard panel
                Rectangle panelRect = new Rectangle(panelLocation, panelSize);

                // Create an image of the dashboard panel
                BufferedImage image = new Robot().createScreenCapture(panelRect);

                // Save the image as a file
                File file = new File("dashboard-screenshot.png");
                ImageIO.write(image, "png", file);

                JOptionPane.showMessageDialog(panel, "Screenshot saved to " + file.getAbsolutePath(), "Screenshot saved", JOptionPane.INFORMATION_MESSAGE);
            } catch (AWTException | IOException ex) {
                ((Throwable) ex).printStackTrace();
                JOptionPane.showMessageDialog(panel, "Error saving screenshot: " + ((Throwable) ex).getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    });

    // Add the screenshot button to the panel
    panel.add(screenshotButton, BorderLayout.SOUTH);

    return panel;
}


	
private JPanel createBudgetsPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    JLabel titleLabel = new JLabel("Budgets");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

    JTable budgetTable = new JTable(budgetManager.getTableModel(currentUserId));
    budgetTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
    JScrollPane scrollPane = new JScrollPane(budgetTable);

    JButton addButton = new JButton("Add");
    addButton.setFont(new Font("Arial", Font.PLAIN, 16));
    addButton.setFocusPainted(false);
    addButton.setBackground(new Color(52, 152, 219));
    addButton.setForeground(Color.WHITE);

    JButton editButton = new JButton("Edit");
    editButton.setFont(new Font("Arial", Font.PLAIN, 16));
    editButton.setFocusPainted(false);
    editButton.setBackground(new Color(46, 204, 113));
    editButton.setForeground(Color.WHITE);

    JButton deleteButton = new JButton("Delete");
    deleteButton.setFont(new Font("Arial", Font.PLAIN, 16));
    deleteButton.setFocusPainted(false);
    deleteButton.setBackground(new Color(231, 76, 60));
    deleteButton.setForeground(Color.WHITE);

    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.add(addButton);
    buttonPanel.add(editButton);
    buttonPanel.add(deleteButton);

    panel.add(titleLabel, BorderLayout.NORTH);
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.add(buttonPanel, BorderLayout.SOUTH);

    addButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        BudgetDialog dialog = new BudgetDialog(FinanceManagerGUI.this, "Add Budget");
        dialog.setVisible(true); 

        if (dialog.getButtonClicked() == BudgetDialog.ButtonClicked.OK) {
          String name = dialog.getName();
          double amount = dialog.getAmount();
          boolean isExpense = dialog.isExpense();
          Budget budget = new Budget(name, amount, isExpense, currentUserId);
          budgetManager.addBudget(budget);
          budgetTable.setModel(budgetManager.getTableModel(currentUserId));
        }
      }
    });

    editButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int selectedRow = budgetTable.getSelectedRow();
        if (selectedRow == -1) {
          JOptionPane.showMessageDialog(FinanceManagerGUI.this, "Please select a row to edit.");
          return;
        }
        int budgetId = (int) budgetTable.getModel().getValueAt(selectedRow, 0);
        BudgetDialog dialog = new BudgetDialog(FinanceManagerGUI.this, "Edit Budget");
        dialog.setVisible(true);

        if (dialog.getButtonClicked() == BudgetDialog.ButtonClicked.OK) {
          String name = dialog.getName();
          double amount = dialog.getAmount();
          boolean isExpense = dialog.isExpense();
          Budget budget = new Budget(name, amount, isExpense, currentUserId);
          budget.setId(budgetId);
          budgetManager.addBudget(budget);
          budgetTable.setModel(budgetManager.getTableModel(currentUserId));
        }
      }
    });

    deleteButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int selectedRow = budgetTable.getSelectedRow();
        if (selectedRow == -1) {
          JOptionPane.showMessageDialog(FinanceManagerGUI.this, "Please select a row to delete.");
          return;
        }
        int budgetId = (int) budgetTable.getModel().getValueAt(selectedRow, 0);
        budgetManager.deleteBudget(budgetId);
        budgetTable.setModel(budgetManager.getTableModel(currentUserId));
      }
    });

    return panel;
}

private JPanel createSearchPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    JLabel titleLabel = new JLabel("Search Transactions");
    JLabel searchLabel = new JLabel("Enter search term:");
    JButton searchButton = new JButton("Search");
    JTable searchResultTable = new JTable();
    JScrollPane resultScrollPane = new JScrollPane(searchResultTable);
    panel.add(titleLabel, BorderLayout.NORTH);

    JPanel searchPanel = new JPanel(new FlowLayout());
    searchPanel.add(searchLabel);
    searchPanel.add(searchTermField);
    searchPanel.add(searchButton);

    panel.add(searchPanel, BorderLayout.CENTER);
    panel.add(resultScrollPane, BorderLayout.SOUTH);

    // Create the table model
    DefaultTableModel model = new DefaultTableModel(
        new String[] {"ID", "User ID", "Description", "Amount", "Is Expense", "Date"}, 0);
    searchResultTable.setModel(model);

    searchButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String searchTerm = searchTermField.getText();
        List<Transaction> searchResults = search.searchTransactions(searchTerm);
        if (searchResults.isEmpty()) {
          searchResultTable.setModel(new DefaultTableModel());
          searchResultTable.revalidate();
          searchResultTable.repaint();
          searchResultTable.invalidate();
          searchResultTable.setEnabled(false);
          searchResultTable.getTableHeader().setReorderingAllowed(false);
          searchResultTable.getTableHeader().setResizingAllowed(false);
          searchResultArea.setText("No results found.");
        } else {
            DefaultTableModel model = new DefaultTableModel(
                    new String[] {"ID", "User ID", "Description", "Amount", "Is Expense", "Date"}, 0);
                for (Transaction transaction : searchResults) {
                    model.addRow(new Object[] {transaction.getId(), transaction.getUserId(), transaction.getDescription(),
                        transaction.getAmount(), transaction.isExpense(), transaction.getDate()});
                }
                searchResultTable.setModel(model);
            }
      }
    });

    return panel;
}


private JPanel createExportPanel() {
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    panel.setBackground(new Color(250, 250, 250));

    JLabel titleLabel = new JLabel("Export Financial Reports");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

    JLabel exportLabel = new JLabel("Export as:");
    exportLabel.setFont(new Font("Arial", Font.PLAIN, 14));

    JComboBox<String> exportComboBox = new JComboBox<>(new String[] {"CSV"});
    exportComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
    exportComboBox.setPreferredSize(new Dimension(100, 25));

    JButton exportButton = new JButton("Export");
    exportButton.setFont(new Font("Arial", Font.BOLD, 14));
    exportButton.setForeground(Color.WHITE);
    exportButton.setBackground(new Color(65, 105, 225));
    exportButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

    JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
    exportPanel.setBackground(new Color(250, 250, 250));
    exportPanel.add(exportLabel);
    exportPanel.add(exportComboBox);
    exportPanel.add(exportButton);

    panel.add(titleLabel, BorderLayout.NORTH);
    panel.add(exportPanel, BorderLayout.CENTER);

    exportButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String format = (String) exportComboBox.getSelectedItem();
            boolean success = export.exportFinancialReport(currentUserId, format);
            if (success) {
                JOptionPane.showMessageDialog(null, "Export successful.");
            } else {
                JOptionPane.showMessageDialog(null, "Export failed.");
            }
        }
    });

    return panel;
}


  

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        FinanceManagerGUI gui = new FinanceManagerGUI();
        gui.setVisible(true);
      }
    });
  }

public static void updateBudgetTable() {
	    // Get the current user's ID
	    int currentUserId = ((Transaction) currentUser).getId();

	    // Create a new BudgetManager instance
	    BudgetManager budgetManager = new BudgetManager();

	    // Get the list of budgets for the current user
	    @SuppressWarnings("unchecked")
		List<Budget> budgets = (List<Budget>) budgetManager.getBudget(currentUserId);

	    JTable budgetTable = null;
		// Get the table model for the budget table
	    @SuppressWarnings("null")
		DefaultTableModel model = (DefaultTableModel) budgetTable.getModel();

	    // Clear the table
	    model.setRowCount(0);

	    // Loop through the budgets and add each one to the table
	    for (Budget budget : budgets) {
	        Object[] row = {budget.getName(), budget.getAmount(), budget.getIsExpense()};
	        model.addRow(row);
	    }
	}

}



