package freemind.modes.mindmapmode.actions;

import java.awt.event.KeyEvent;

import freemind.modes.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.view.mindmapview.NodeView;

public class NewChildWithoutFocus extends MindMapChild{
	
	
	
	public MindMapNode check(MindMapController c,MindMapNode target,KeyEvent e, NewChildAction action){
		final MindMapNode targetNode = target;
			MindMapNode newNode = null;
	final boolean parentFolded = targetNode.isFolded();
	if (parentFolded) {
		c.setFolded(targetNode, false);
	}
	int position = c.getFrame().getProperty("placenewbranches")
			.equals("last") ? targetNode.getChildCount() : 0;
	newNode = action.addNewNode(targetNode, position);
	final NodeView nodeView = c.getNodeView(newNode);
	c.edit.edit(nodeView, c.getNodeView(target), e, true, parentFolded,
			false);
	return newNode;
}
}
