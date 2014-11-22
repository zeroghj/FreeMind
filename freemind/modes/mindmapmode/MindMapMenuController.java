package freemind.modes.mindmapmode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

import freemind.common.XmlBindingTools;
import freemind.controller.MenuBar;
import freemind.controller.StructuredMenuHolder;
import freemind.controller.actions.generated.instance.MenuStructure;
import freemind.modes.mindmapmode.actions.HookAction;
import freemind.modes.mindmapmode.hooks.MindMapHookFactory;

public class MindMapMenuController
{
	private static Logger logger;
	
	private MindMapController mindMapController;
	private MenuStructure mMenuStructure;
	
	public MindMapMenuController(MindMapController mindMapController)
	{
		this.mindMapController = mindMapController;
		
		if (logger == null) {
			logger = mindMapController.getFrame().getLogger(this.getClass().getName());
		}
		
		init();
	}
	
	private void init()
	{	
		logger.info("mindmap_menus");
		// load menus:
		try {
			InputStream in;
			in = mindMapController.getFrame().getResource("mindmap_menus.xml").openStream();
			mMenuStructure = updateMenusFromXml(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			freemind.main.Resources.getInstance().logException(e);
		}
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

	public MenuStructure updateMenusFromXml(InputStream in) {
		// get from resources:
		try {
			IUnmarshallingContext unmarshaller = XmlBindingTools.getInstance()
					.createUnmarshaller();
			MenuStructure menus = (MenuStructure) unmarshaller
					.unmarshalDocument(in, null);
			return menus;
		} catch (JiBXException e) {
			freemind.main.Resources.getInstance().logException(e);
			throw new IllegalArgumentException(
					"Menu structure could not be read.");
		}
	}

	public void addIconsToMenu(StructuredMenuHolder holder,
			String iconMenuString) {
		JMenu iconMenu = holder.addMenu(new JMenu(getText("icon_menu")),
				iconMenuString + "/.");
		holder.addAction(removeLastIconAction, iconMenuString
				+ "/removeLastIcon");
		holder.addAction(removeAllIconsAction, iconMenuString
				+ "/removeAllIcons");
		holder.addSeparator(iconMenuString);
		for (int i = 0; i < iconActions.size(); ++i) {
			JMenuItem item = holder.addAction((Action) iconActions.get(i),
					iconMenuString + "/" + i);
		}
	}
}
