package freemind.modes;

import java.awt.event.MouseEvent;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import freemind.controller.StructuredMenuHolder;
import freemind.extensions.HookFactory;
import freemind.main.XMLElement;

public class ButtonAdapter extends ControllerAdapter{
	
	private static Logger logger;

	public ButtonAdapter(Mode mode) {
		super(mode);
	}
	
	public JMenuItem addCheckBox(StructuredMenuHolder holder,
			String category, Action action, String keystroke) {
		JCheckBoxMenuItem item = (JCheckBoxMenuItem) holder.addMenuItem(
				new JCheckBoxMenuItem(action), category);
		if (keystroke != null) {
			item.setAccelerator(KeyStroke.getKeyStroke(getFrame()
					.getAdjustableProperty(keystroke)));
		}
		return item;
	}
	public JMenuItem add(StructuredMenuHolder holder, String category,
			Action action, String keystroke) {
		JMenuItem item = holder.addAction(action, category);
		if (keystroke != null) {
			String keyProperty = getFrame().getAdjustableProperty(keystroke);
			logger.finest("Found key stroke: " + keyProperty);
			item.setAccelerator(KeyStroke.getKeyStroke(keyProperty));
		}
		return item;
	}
	public JMenuItem addRadioItem(StructuredMenuHolder holder,
			String category, Action action, String keystroke, boolean isSelected) {
		JRadioButtonMenuItem item = (JRadioButtonMenuItem) holder.addMenuItem(
				new JRadioButtonMenuItem(action), category);
		if (keystroke != null) {
			item.setAccelerator(KeyStroke.getKeyStroke(getFrame()
					.getAdjustableProperty(keystroke)));
		}
		item.setSelected(isSelected);
		return item;
	}

	@Override
	public void doubleClick(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void plainClick(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean extendSelection(MouseEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFolded(MindMapNode node, boolean folded) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMenus(StructuredMenuHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JPopupMenu getPopupMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HookFactory getHookFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MindMapNode newNode(Object userObject, MindMap map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XMLElement createXMLElement() {
		// TODO Auto-generated method stub
		return null;
	}


}
