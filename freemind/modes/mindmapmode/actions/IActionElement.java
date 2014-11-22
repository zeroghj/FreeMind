package freemind.modes.mindmapmode.actions;

public interface IActionElement {
	public void accept(IActionVisitor visitor);
}
