import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class BudgetDialog extends JDialog {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

public enum ButtonClicked { OK, CANCEL };
  private ButtonClicked buttonClicked = ButtonClicked.CANCEL;
  private JTextField nameField;
  private JTextField amountField;
  private boolean isExpense;
  private int currentUserId;

  public BudgetDialog(FinanceManagerGUI parent, String title) {
    super(parent, title, true);
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    JLabel nameLabel = new JLabel("Name:");
    nameField = new JTextField(10);

    JLabel amountLabel = new JLabel("Amount:");
    amountField = new JTextField(10);

    JPanel inputPanel = new JPanel();
    inputPanel.add(nameLabel, BorderLayout.WEST);
    inputPanel.add(nameField, BorderLayout.CENTER);
    inputPanel.add(amountLabel, BorderLayout.SOUTH);
    inputPanel.add(amountField, BorderLayout.EAST);

    JPanel buttonPanel = new JPanel(new FlowLayout());
    JButton okButton = new JButton("OK");
    okButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        buttonClicked = ButtonClicked.OK;
        dispose();
      }
    });

    JButton cancelButton = new JButton("Cancel");
    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });

    buttonPanel.add(okButton);
    buttonPanel.add(cancelButton);

    add(inputPanel, BorderLayout.CENTER);
    add(buttonPanel, BorderLayout.SOUTH);

    pack();
    setLocationRelativeTo(parent);
  }

  public void setAmount(double amount) {
    amountField.setText(Double.toString(amount));
  }

  public void setName(String name) {
    nameField.setText(name);
  }

  public double getAmount() {
    try {
      return Double.parseDouble(amountField.getText());
    } catch (NumberFormatException e) {
      return 0.0;
    }
  }

  public String getName() {
    return nameField.getText();
  }

  public boolean isExpense() {
    return isExpense;
  }

  public void setExpense(boolean isExpense) {
    this.isExpense = isExpense;
  }

  public int getCurrentUserId() {
    return currentUserId;
  }

  public void setCurrentUserId(int currentUserId) {
    this.currentUserId = currentUserId;
  }

  public ButtonClicked getButtonClicked() {
    return buttonClicked;
  }
	//  private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
	//	    String name = nameTextField.getText();
	//	    double amount = Double.parseDouble(amountTextField.getText());
	//	    boolean isExpense = isExpenseCheckBox.isSelected();
	//	    int currentUserId = financeManagerGUI.getCurrentUserId();
	//
	//	    Budget budget = new Budget(name, amount, isExpense, currentUserId);
	//	    financeManagerGUI.getBudgetManager().addBudget(budget);
	//	    setButtonClicked(ButtonClicked.OK);
	//	    dispose();
	//	}
  
  
}
