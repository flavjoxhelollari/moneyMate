import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class Budget extends JFrame{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private int id;
  private String name;
  private double amount;
  private boolean isExpense;
  private int userId;

  public Budget(String name, double amount, boolean isExpense, int userId) {
    this.name = name;
    this.amount = amount;
    this.isExpense = isExpense;
    this.userId = userId;
  }

  // getters and setters
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public boolean getIsExpense() {
    return isExpense;
  }

  public void setIsExpense(boolean isExpense) {
    this.isExpense = isExpense;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }
  private JLabel nameLabel;
  private JTextField nameField;
  private JLabel limitLabel;
  private JTextField limitField;
  private JCheckBox expenseCheckBox;
  private JButton saveButton;
  private JButton cancelButton;
  
  public Budget(JFrame parent, String title) {
	  super();
	  @SuppressWarnings("unused")
	Budget budget;
	  setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	  setResizable(false);
	  setBounds(100, 100, 350, 200);
	  JPanel contentPane = new JPanel();
	  setContentPane(contentPane);
	  contentPane.setLayout(null);

	  JLabel nameLabel = new JLabel("Name:");
	  nameLabel.setBounds(10, 20, 80, 20);
	  contentPane.add(nameLabel);

	  nameField = new JTextField();
	  nameField.setBounds(100, 20, 200, 20);
	  contentPane.add(nameField);
	  nameField.setColumns(10);

	  JLabel amountLabel = new JLabel("Amount:");
	  amountLabel.setBounds(10, 50, 80, 20);
	  contentPane.add(amountLabel);

	  JTextField amountField = new JTextField();
	  amountField.setBounds(100, 50, 200, 20);
	  contentPane.add(amountField);
	  amountField.setColumns(10);

	  JLabel isExpenseLabel = new JLabel("Type:");
	  isExpenseLabel.setBounds(10, 80, 80, 20);
	  contentPane.add(isExpenseLabel);

	  @SuppressWarnings("rawtypes")
	JComboBox isExpenseComboBox = new JComboBox<>(new String[]{"Expense", "Income"});
	  isExpenseComboBox.setBounds(100, 80, 200, 20);
	  contentPane.add(isExpenseComboBox);

	  JButton saveButton = new JButton("Save");
	  saveButton.addActionListener(new ActionListener() {
		    @SuppressWarnings("unused")
			public void actionPerformed(ActionEvent e) {
		        String name = nameField.getText();
		        double amount = Double.parseDouble(amountField.getText());
		        boolean isExpense = isExpenseComboBox.getSelectedIndex() == 0;
		        BudgetManager budget = new BudgetManager();
		        budget.addBudget(null);
		        dispose();
		        FinanceManagerGUI.updateBudgetTable();
		    }
	  });
	  saveButton.setBounds(140, 120, 70, 25);
	  contentPane.add(saveButton);

	  JButton cancelButton = new JButton("Cancel");
	  cancelButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
	      dispose();
	    }
	  });
	  cancelButton.setBounds(220, 120, 80, 25);
	  contentPane.add(cancelButton);
	}

public JLabel getNameLabel() {
	return nameLabel;
}

public void setNameLabel(JLabel nameLabel) {
	this.nameLabel = nameLabel;
}

public JLabel getLimitLabel() {
	return limitLabel;
}

public void setLimitLabel(JLabel limitLabel) {
	this.limitLabel = limitLabel;
}

public JCheckBox getExpenseCheckBox() {
	return expenseCheckBox;
}

public void setExpenseCheckBox(JCheckBox expenseCheckBox) {
	this.expenseCheckBox = expenseCheckBox;
}

public JButton getSaveButton() {
	return saveButton;
}

public void setSaveButton(JButton saveButton) {
	this.saveButton = saveButton;
}

public JTextField getLimitField() {
	return limitField;
}

public void setLimitField(JTextField limitField) {
	this.limitField = limitField;
}

public JButton getCancelButton() {
	return cancelButton;
}

public void setCancelButton(JButton cancelButton) {
	this.cancelButton = cancelButton;
}

//protected int getId() {
//	// TODO Auto-generated method stub
//	return 0;
////}
//
//protected int getId1() {
//	// TODO Auto-generated method stub
//	return 0;
//}

}
