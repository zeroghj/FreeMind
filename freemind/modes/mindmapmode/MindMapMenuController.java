package freemind.modes.mindmapmode;

import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;

import freemind.controller.MenuBar;
import freemind.controller.StructuredMenuHolder;
import freemind.modes.mindmapmode.actions.HookAction;
import freemind.modes.mindmapmode.hooks.MindMapHookFactory;

public class MindMapMenuController
{
	private MindMapController mindMapController;
	
	public MindMapMenuController(MindMapController mindMapController)
	{
		this.mindMapController = mindMapController;
	}
	
	public void updateMenus(StructuredMenuHolder holder) {

		processMenuCategory(holder, mMenuStructure.getListChoiceList(), ""); /*
																			 * MenuBar
																			 * .
																			 * MENU_BAR_PREFIX
																			 */
		// add hook actions to this holder.
		// hooks, fc, 1.3.2004:
		MindMapHookFactory hookFactory = (MindMapHookFactory) getHookFactory();
		for (int i = 0; i < hookActions.size(); ++i) {
			AbstractAction hookAction = (AbstractAction) hookActions.get(i);
			String hookName = ((HookAction) hookAction).getHookName();
			hookFactory.decorateAction(hookName, hookAction);
			List hookMenuPositions = hookFactory.getHookMenuPositions(hookName);
			for (Iterator j = hookMenuPositions.iterator(); j.hasNext();) {
				String pos = (String) j.next();
				holder.addMenuItem(
						hookFactory.getMenuItem(hookName, hookAction), pos);
			}
		}
		// update popup and toolbar:
		popupmenu.update(holder);
		toolbar.update(holder);

		// editMenu.add(getExtensionMenu());
		String formatMenuString = MenuBar.FORMAT_MENU;
		createPatternSubMenu(holder, formatMenuString);

		// editMenu.add(getIconMenu());
		addIconsToMenu(holder, MenuBar.INSERT_MENU + "icons");

	}
}
