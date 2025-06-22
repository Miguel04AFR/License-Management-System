package visual;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public  class AlertListRenderer extends DefaultListCellRenderer {
	private static final long serialVersionUID = 1L;
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
			boolean isSelected, boolean cellHasFocus) {
		Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (value.toString().contains("[Urgent]")) {
			component.setForeground(new Color(255, 100, 100));
		} else if (value.toString().contains("[Important]")) {
			component.setForeground(new Color(255, 175, 90));
		}
		return component;
	}
}