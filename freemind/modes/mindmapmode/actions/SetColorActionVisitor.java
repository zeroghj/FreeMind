package freemind.modes.mindmapmode.actions;

import java.awt.Color;

import freemind.controller.actions.generated.instance.CloudColorXmlAction;
import freemind.main.Tools;
import freemind.modes.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class SetColorActionVisitor implements IActionVisitor{
	private MindMapController controller;
	
	public void visit(CloudColorAction cloudColorAct){
		MindMapController controller = cloudColorAct.getController();
		MindMapNode node = cloudColorAct.getNode();
		Color color = cloudColorAct.getColor();
		
		CloudColorXmlAction doAction = createCloudColorXmlAction(node, color);
		CloudColorXmlAction undoAction = createCloudColorXmlAction(node,
				(node.getCloud() == null) ? null : node.getCloud().getColor());
		controller.doTransaction(this.getClass().getName(),
				new ActionPair(doAction, undoAction));
	}
	
	public CloudColorXmlAction createCloudColorXmlAction(MindMapNode node,
			Color color) {
		CloudColorXmlAction nodeAction = new CloudColorXmlAction();
		nodeAction.setNode(node.getObjectId(controller));
		nodeAction.setColor(Tools.colorToXml(color));
		return nodeAction;
	}
	
	public void visit(EdgeColorAction edgeColorAct){
		
	}
	
	public void visit(NodeColorAction nodeColorAct){
		
	}
}
