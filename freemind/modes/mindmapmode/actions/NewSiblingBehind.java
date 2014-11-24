package freemind.modes.mindmapmode.actions;

import java.awt.event.KeyEvent;

import freemind.modes.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.view.mindmapview.NodeView;

public class NewSiblingBehind extends MindMapChild{
	
	public MindMapNode check(MindMapController c,MindMapNode target,KeyEvent e, NewChildAction action){
		final MindMapNode targetNode = target;
			MindMapNode newNode = null;
		if (!targetNode.isRoot()) {
			MindMapNode parent = targetNode.getParentNode();
			int childPosition = parent.getChildPosition(targetNode);
				childPosition++;
			newNode = action.addNewNode(parent, childPosition, targetNode.isLeft());
			final NodeView nodeView = c.getNodeView(newNode);
			c.select(nodeView);
			c.edit.edit(nodeView, c.getNodeView(target), e, true, false,
					false);
		}
		return newNode;
	}
}
