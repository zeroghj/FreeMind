package freemind.modes.mindmapmode.actions;

import java.awt.Color;

import freemind.controller.actions.generated.instance.CloudColorXmlAction;
import freemind.controller.actions.generated.instance.EdgeColorFormatAction;
import freemind.main.Tools;
import freemind.modes.EdgeAdapter;
import freemind.modes.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.actions.xml.ActionPair;

public class SetColorActionVisitor implements IActionVisitor{
	private MindMapController controller;
	
	public void visit(CloudColorAction cloudColorAct){
		this.controller = cloudColorAct.getController();
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
		this.controller = edgeColorAct.getController();
		MindMapNode node = edgeColorAct.getNode();
		Color color = edgeColorAct.getColor();
		
		EdgeColorFormatAction doAction = createEdgeColorFormatAction(node,
				color);
		EdgeColorFormatAction undoAction = createEdgeColorFormatAction(node,
				((EdgeAdapter) node.getEdge()).getRealColor());
		controller.doTransaction(this.getClass().getName(),
				new ActionPair(doAction, undoAction));
	}
	
	public EdgeColorFormatAction createEdgeColorFormatAction(MindMapNode node,
			Color color) {
		EdgeColorFormatAction edgeAction = new EdgeColorFormatAction();
		edgeAction.setNode(node.getObjectId(controller));
		if (color != null) {
			edgeAction.setColor(Tools.colorToXml(color));
		}
		return edgeAction;
	}
	
	public void visit(NodeColorAction nodeColorAct){
		
	}
}
