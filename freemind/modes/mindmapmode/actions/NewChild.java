package freemind.modes.mindmapmode.actions;

import java.awt.event.KeyEvent;

import freemind.modes.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.view.mindmapview.NodeView;

public class NewChild extends MindMapChild{

	public MindMapNode check(MindMapController c,MindMapNode target,KeyEvent e, NewChildAction action){
		MindMapNode newNode = null;
		final NodeView nodeView = c.getNodeView(newNode);
		c.select(nodeView);
		return null;
	}
}
