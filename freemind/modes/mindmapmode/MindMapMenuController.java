package freemind.modes.mindmapmode;

import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

import freemind.common.XmlBindingTools;
import freemind.controller.MenuBar;
import freemind.controller.StructuredMenuHolder;
import freemind.controller.actions.generated.instance.MenuActionBase;
import freemind.controller.actions.generated.instance.MenuCategoryBase;
import freemind.controller.actions.generated.instance.MenuCheckedAction;
import freemind.controller.actions.generated.instance.MenuRadioAction;
import freemind.controller.actions.generated.instance.MenuSeparator;
import freemind.controller.actions.generated.instance.MenuStructure;
import freemind.controller.actions.generated.instance.MenuSubmenu;
import freemind.main.Tools;
import freemind.modes.ButtonAdapter;
import freemind.modes.ControllerAdapter;
import freemind.modes.MindMapNode;
import freemind.modes.MindMap.MapSourceChangedObserver;
import freemind.modes.common.GotoLinkNodeAction;
import freemind.modes.mindmapmode.actions.ApplyPatternAction;
import freemind.modes.mindmapmode.actions.ChangeArrowsInArrowLinkAction;
import freemind.modes.mindmapmode.actions.ColorArrowLinkAction;
import freemind.modes.mindmapmode.actions.HookAction;
import freemind.modes.mindmapmode.actions.MindMapActions;
import freemind.modes.mindmapmode.actions.RemoveArrowLinkAction;
import freemind.modes.mindmapmode.hooks.MindMapHookFactory;

public class MindMapMenuController
{
	private static Logger logger;
	
	private MindMapController mindMapController;
	private MenuStructure mMenuStructure;
	private MindMapPopupMenu popupmenu;
	private MindMapToolBar toolbar;
	private Vector hookActions;
	private Vector iconActions;
	private ApplyPatternAction patterns[];
	private ButtonAdapter adapter = new ButtonAdapter(null);
	
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
		hookActions = mindMapController.getHookActions();
		iconActions = mindMapController.iconActions;
		toolbar = mindMapController.getToolBar();
		patterns = mindMapController.patterns;
		
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
		
		logger.info("MindMapPopupMenu");
		popupmenu = new MindMapPopupMenu(mindMapController);
	}
	
	public void updateMenus(StructuredMenuHolder holder) {

		processMenuCategory(holder, mMenuStructure.getListChoiceList(), ""); /*
																			 * MenuBar
																			 * .
																			 * MENU_BAR_PREFIX
																			 */
		// add hook actions to this holder.
		// hooks, fc, 1.3.2004:
		MindMapHookFactory hookFactory = (MindMapHookFactory) mindMapController.getHookFactory();
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

	public void addIconsToMenu(StructuredMenuHolder holder,
			String iconMenuString) {
		JMenu iconMenu = holder.addMenu(new JMenu(mindMapController.getText("icon_menu")),
				iconMenuString + "/.");
		holder.addAction(mindMapController.removeLastIconAction, iconMenuString
				+ "/removeLastIcon");
		holder.addAction(mindMapController.removeAllIconsAction, iconMenuString
				+ "/removeAllIcons");
		holder.addSeparator(iconMenuString);
		for (int i = 0; i < iconActions.size(); ++i) {
			JMenuItem item = holder.addAction((Action) iconActions.get(i),
					iconMenuString + "/" + i);
		}
	}
	
	public void createPatternSubMenu(StructuredMenuHolder holder,
			String formatMenuString) {
		for (int i = 0; i < patterns.length; ++i) {
			JMenuItem item = holder.addAction(patterns[i], formatMenuString
					+ "patterns/patterns/" + i);
			item.setAccelerator(KeyStroke
					.getKeyStroke(mindMapController.getFrame().getAdjustableProperty(
							"keystroke_apply_pattern_" + (i + 1))));
		}
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

	public void processMenuCategory(StructuredMenuHolder holder, List list,
			String category) {
		String categoryCopy = category;
		ButtonGroup buttonGroup = null;
		for (Iterator i = list.iterator(); i.hasNext();) {
			Object obj = (Object) i.next();
			if (obj instanceof MenuCategoryBase) {
				MenuCategoryBase cat = (MenuCategoryBase) obj;
				String newCategory = categoryCopy + "/" + cat.getName();
				holder.addCategory(newCategory);
				if (cat instanceof MenuSubmenu) {
					MenuSubmenu submenu = (MenuSubmenu) cat;
					holder.addMenu(new JMenu(mindMapController.getText(submenu.getNameRef())),
							newCategory + "/.");
				}
				processMenuCategory(holder, cat.getListChoiceList(),
						newCategory);
			} else if (obj instanceof MenuActionBase) {
				MenuActionBase action = (MenuActionBase) obj;
				String field = action.getField();
				String name = action.getName();
				if (name == null) {
					name = field;
				}
				String keystroke = action.getKeyRef();
				try {
					Action theAction = (Action) Tools.getField(new Object[] {
							this, mindMapController.getController() }, field);
					String theCategory = categoryCopy + "/" + name;
					if (obj instanceof MenuCheckedAction) {
						adapter.addCheckBox(holder, theCategory, theAction, keystroke);
					} else if (obj instanceof MenuRadioAction) {
						final JRadioButtonMenuItem item = (JRadioButtonMenuItem) adapter.addRadioItem(
								holder, theCategory, theAction, keystroke,
								((MenuRadioAction) obj).getSelected());
						if (buttonGroup == null)
							buttonGroup = new ButtonGroup();
						buttonGroup.add(item);

					} else {
						adapter.add(holder, theCategory, theAction, keystroke);
					}
				} catch (Exception e1) {
					freemind.main.Resources.getInstance().logException(e1);
				}
			} else if (obj instanceof MenuSeparator) {
				holder.addSeparator(categoryCopy);
			} /* else exception */
		}
	}

	public JPopupMenu getPopupMenu() {
		return popupmenu;
	}

	/**
	 * Link implementation: If this is a link, we want to make a popup with at
	 * least removelink available.
	 */
	public JPopupMenu getPopupForModel(java.lang.Object obj) {
		if (obj instanceof MindMapArrowLinkModel) {
			// yes, this is a link.
			MindMapArrowLinkModel link = (MindMapArrowLinkModel) obj;
			JPopupMenu arrowLinkPopup = new JPopupMenu();
			// block the screen while showing popup.
			arrowLinkPopup.addPopupMenuListener(mindMapController.popupListenerSingleton);
			mindMapController.removeArrowLinkAction.setArrowLink(link);
			arrowLinkPopup.add(new RemoveArrowLinkAction(mindMapController, link));
			arrowLinkPopup.add(new ColorArrowLinkAction(mindMapController, link));
			arrowLinkPopup.addSeparator();
			/* The arrow state as radio buttons: */
			JRadioButtonMenuItem itemnn = new JRadioButtonMenuItem(
					new ChangeArrowsInArrowLinkAction(mindMapController, "none",
							"images/arrow-mode-none.png", link, false, false));
			JRadioButtonMenuItem itemnt = new JRadioButtonMenuItem(
					new ChangeArrowsInArrowLinkAction(mindMapController, "forward",
							"images/arrow-mode-forward.png", link, false, true));
			JRadioButtonMenuItem itemtn = new JRadioButtonMenuItem(
					new ChangeArrowsInArrowLinkAction(mindMapController, "backward",
							"images/arrow-mode-backward.png", link, true, false));
			JRadioButtonMenuItem itemtt = new JRadioButtonMenuItem(
					new ChangeArrowsInArrowLinkAction(mindMapController, "both",
							"images/arrow-mode-both.png", link, true, true));
			itemnn.setText(null);
			itemnt.setText(null);
			itemtn.setText(null);
			itemtt.setText(null);
			arrowLinkPopup.add(itemnn);
			arrowLinkPopup.add(itemnt);
			arrowLinkPopup.add(itemtn);
			arrowLinkPopup.add(itemtt);
			// select the right one:
			boolean a = !link.getStartArrow().equals("None");
			boolean b = !link.getEndArrow().equals("None");
			itemtt.setSelected(a && b);
			itemnt.setSelected(!a && b);
			itemtn.setSelected(a && !b);
			itemnn.setSelected(!a && !b);

			arrowLinkPopup.addSeparator();

			arrowLinkPopup.add(new GotoLinkNodeAction(mindMapController, link.getSource()));
			arrowLinkPopup.add(new GotoLinkNodeAction(mindMapController, link.getTarget()));

			arrowLinkPopup.addSeparator();
			// add all links from target and from source:
			HashSet NodeAlreadyVisited = new HashSet();
			NodeAlreadyVisited.add(link.getSource());
			NodeAlreadyVisited.add(link.getTarget());
			Vector links = mindMapController.getMindMapMapModel().getLinkRegistry().getAllLinks(
					link.getSource());
			links.addAll(mindMapController.getMindMapMapModel().getLinkRegistry().getAllLinks(
					link.getTarget()));
			for (int i = 0; i < links.size(); ++i) {
				MindMapArrowLinkModel foreign_link = (MindMapArrowLinkModel) links
						.get(i);
				if (NodeAlreadyVisited.add(foreign_link.getTarget())) {
					arrowLinkPopup.add(new GotoLinkNodeAction(mindMapController,
							foreign_link.getTarget()));
				}
				if (NodeAlreadyVisited.add(foreign_link.getSource())) {
					arrowLinkPopup.add(new GotoLinkNodeAction(mindMapController,
							foreign_link.getSource()));
				}
			}
			return arrowLinkPopup;
		}
		return null;
	}

}
