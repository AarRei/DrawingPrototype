package xv.GUI;

import java.awt.*;
import javax.swing.*;

public class UserListRenderer extends JPanel implements ListCellRenderer {
	private JLabel label = null;

	public UserListRenderer() {
		super(new FlowLayout(FlowLayout.LEFT));

		setOpaque(true);

		label = new JLabel();
		label.setOpaque(false);
		add(label);
	}

	public Component getListCellRendererComponent(JList list, Object value,	int index, boolean iss, boolean chf) {

		label.setIcon(((ListItem) value).getIcon());
		label.setText(((ListItem) value).getText());

		if (iss)
			setBackground(Color.lightGray);
		else
			setBackground(list.getBackground());
		return this;
	}
}
