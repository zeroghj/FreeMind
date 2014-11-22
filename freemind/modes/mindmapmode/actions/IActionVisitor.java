package freemind.modes.mindmapmode.actions;

public interface IActionVisitor {
	public void visit(CloudColorAction cloudColorAct);
	public void visit(EdgeColorAction edgeColorAct);
	public void visit(NodeColorAction nodeColorAct);
}
