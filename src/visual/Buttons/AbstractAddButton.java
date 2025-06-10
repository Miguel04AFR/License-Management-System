package visual.Buttons;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public abstract class AbstractAddButton extends JButton {
	private static final long serialVersionUID = 1L;
    protected JFrame parentFrame;
    protected Runnable refreshCallback;

    public AbstractAddButton(String text, JFrame parent, Runnable refreshCallback) {
        super(text);
        this.parentFrame = parent;
        this.refreshCallback = refreshCallback;
        configureButton();
        addActionListener(new ButtonActionListener());
    }
protected void handleException(Exception e, String userMessage) {
    JOptionPane.showMessageDialog(parentFrame,
        userMessage + "\nDetails: " + e.getMessage(),
        "Error",
        JOptionPane.ERROR_MESSAGE);
    e.printStackTrace(); // Log the exception for debugging
}
    private void configureButton() {
        this.setFocusable(false);
        this.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
    }

    protected abstract void showFormDialog();
    protected abstract boolean validateForm();
    protected abstract void saveToDatabase();

    private class ButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showFormDialog();
        }
    }
}