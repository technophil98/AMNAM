package utils.inspection;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.function.Function;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import utils.Counter;
import utils.Utils;
import utils.tuple.Pair;

/**
 * Une classe permettant l'inspection de d'autre classe marquée par l'annotation {@link Inspectable}.
 * @author Pascal Dally-Bélanger
 *
 */
public class Inspector {
	
	private String name;
	private JPanel panel;
	private HashMap<String, HashMap<String, Element>> elementMap;
	private HashMap<String, HashMap<String, Pair<JLabel, Element>>> labelMap;
	private Inspectable inspectable;


	/**
	 * Initialise un inspecteur vide.
	 */
	private Inspector() {
		
	}

	/**
	 * Génere un inspecteur pour la classe.
	 * @param inspectable La classe à inspecter.
	 * @return L'inspecteur généré.
	 */
	public static Inspector generateInspector(Class<?> inspectable) {
		return generateInspector(inspectable, o -> o);
	}
	
	/**
	 * Génere un inspecteur pour la classe.
	 * @param inspectable La classe à inspecter.
	 * @param objectProvider Une fonction qui permet d'obtenir un objet du bon type pour lors de l'inspection d'un objet.
	 * @return L'inspecteur généré.
	 */
	private static Inspector generateInspector(Class<?> inspectable, Function<Object, Object> objectProvider) {
		Inspector inspector = new Inspector();
		inspector.elementMap = new HashMap<String, HashMap<String, Element>>();
		Inspectable inspectableInfo = inspectable.getAnnotation(Inspectable.class);
		inspector.inspectable = inspectableInfo;
		
		for(String group : inspectableInfo.groups())
			inspector.elementMap.put(group, new HashMap<String, Element>());
		for(Field field : inspectable.getDeclaredFields())
			if(field.isAnnotationPresent(InspectionElement.class)) {
				InspectionElement inspection = field.getAnnotation(InspectionElement.class);
				Class<?> temp = field.getType();
				if(temp.isAnnotationPresent(Inspectable.class))
					generateInspector(temp, Utils.ignorerException(o -> field.get(o))).elementMap.forEach((group, subMap) -> {
						if(inspector.elementMap.get(group) != null)
							inspector.elementMap.get(group).putAll(subMap);
						else
							inspector.elementMap.put(group, subMap);
					});
				else {
					String name = inspection.name().equals("") ? field.getName() : inspection.name();
					Element element = new Element(name, o -> String.format(inspection.format(), o),
							Utils.ignorerException(o -> field.get(objectProvider.apply(o))),
							inspection.description(),
							inspection.displayed());
					inspector.elementMap.computeIfPresent(inspection.group(), (group, subMap) -> {
						subMap.put(name, element);
						return subMap;
					});
					
				}
			}
		for(Method method : inspectable.getDeclaredMethods())
			if(method.isAnnotationPresent(InspectionElement.class)) {
				InspectionElement inspection = method.getAnnotation(InspectionElement.class);
				Class<?> temp = method.getReturnType();
				if(temp.isAnnotationPresent(Inspectable.class))
					generateInspector(temp, Utils.ignorerException(o -> method.invoke(o))).elementMap.forEach((group, subMap) -> {
						if(inspector.elementMap.get(group) != null)
							inspector.elementMap.get(group).putAll(subMap);
						else
							inspector.elementMap.put(group, subMap);
					});
				else {
					String name = inspection.name().equals("") ? method.getName() : inspection.name();
					Element element = new Element(name, o -> String.format(inspection.format(), o),
							Utils.ignorerException(o -> method.invoke(objectProvider.apply(o))),
							inspection.description(),
							inspection.displayed());
					inspector.elementMap.computeIfPresent(inspection.group(), (group, subMap) -> {
						subMap.put(name, element);
						return subMap;
					});
					
				}
			}
		inspector.name = inspectableInfo.name();
		if(inspector.name.equals(""))
			inspector.name = inspectable.getSimpleName();
		return inspector;
	}

	/**
	 * Génere les informations d'inspection sur un objet.
	 * @param object l'objet à inspecter.
	 */
	public void inspect(Object object) {
		if(panel == null)
			generatePanel(object);
		else
			updatePanel(object);
	}
	
	/**
	 * Génere le panneau d'inspection de l'objet.
	 * @param object l'objet à inspecter.
	 */
	private void generatePanel(Object object) {
		labelMap = new HashMap<String, HashMap<String, Pair<JLabel, Element>>>();
		JPanel header = null;
		if(!inspectable.header().equals(JPanel.class)) {
			try {
				Object[] args = new Object[inspectable.args().length/2];
				for(int i = 0; i < args.length; i++)
					try {
						args[i] = elementMap.get(inspectable.args()[2*i]).get(inspectable.args()[2*i+1]).objectProvider.apply(object);
					} catch(NullPointerException e) {
						args[i] = null;
					}
				header = inspectable.header().getConstructor(Object[].class).newInstance((Object)args);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
		}
		
		JPanel mainPanel = new JPanel();
		
		mainPanel.setBounds(10, 55, 330, 572);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 55, 330, 572);
		GridBagLayout layout = new GridBagLayout();
		layout.columnWidths = new int[1];
		layout.rowHeights = new int[elementMap.size() + 1];
		layout.columnWeights = new double[]{1.0};
		layout.rowWeights = new double[elementMap.size() + 1];
		layout.rowWeights[layout.rowWeights.length - 1] = Double.MIN_VALUE;
		panel.setLayout(layout);

		scrollPane.setViewportView(panel);
		scrollPane.setBounds(10, 55, 330, 572);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		mainPanel.setLayout(new BorderLayout(0, 5));
		
		if(header != null)
			mainPanel.add(header, BorderLayout.NORTH);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
		
		Counter counter = new Counter();
		
		elementMap.forEach((group, subMap) -> {
			HashMap<String, Pair<JLabel, Element>> subPanelMap = new HashMap<String, Pair<JLabel, Element>>();
			boolean isEmpty = true;
			for(Element element : subMap.values()) {
				if(element.displayed) {
					isEmpty = false;
					break;
				}
			}
			if(isEmpty)
				return;
			labelMap.put(group, subPanelMap);
			JPanel temp = new JPanel();
			GridBagConstraints constraints = new GridBagConstraints();
			constraints.insets = new Insets(0, 0, 5, 0);
			constraints.fill = GridBagConstraints.BOTH;
			constraints.gridx = 0;
			constraints.gridy = counter.getCount();
			counter.increment();
			panel.add(temp, constraints);
			temp.setBorder(new TitledBorder(null, group, TitledBorder.LEADING, TitledBorder.TOP, null, null));
			temp.setLayout(new GridLayout(0, 2, 0, 0));
			subMap.forEach((elementName, element) -> {
				if(!element.displayed)
					return;
				String toolTip = element.description;
				JLabel lblName = new JLabel(elementName);
				JLabel lblValue = new JLabel(element.toString.apply(object));
				lblName.setToolTipText(toolTip);
				lblValue.setToolTipText(toolTip);
				subPanelMap.put(elementName, new Pair<JLabel, Element>(lblValue, element));
				temp.add(lblName);
				temp.add(lblValue);
			});
		});
		
		this.panel = mainPanel;
	}
	
	/**
	 * Met a jour le panneau d'inspection.
	 * @param object l'objet sur lequel mettre à jour l'inspection.
	 */
	private void updatePanel(Object object) {
		labelMap.forEach((group, submap) -> submap.forEach((name, pair) -> pair.getFirst().setText(pair.getSecond().toString.apply(object))));
	}
	
	/**
	 * Retourne le panneau d'inspection.
	 * @return le panneau d'inspection.
	 */
	public JPanel getPanel() {
		return panel;
	}
	
	/**
	 * Retourne le nom d'inspection de la classe, tel défini par {@link Inspectable#name()}.
	 * @return le nom d'inspection de la classe.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Classe représentant un élément inspectable, tel défini par {@link InspectionElement}.
	 * @author Pascal Dally-Bélanger
	 *
	 */
	public static class Element {
		private String name;
		private Function<Object, String> toString;
		private Function<Object, Object> objectProvider;
		private String description;
		private boolean displayed;
		
		/**
		 * Initialise un élément.
		 * @param name Le nom de la propriété ou de la méthode inspecté.
		 * @param toString Une fonction qui permet d'extraire une chaîne d'information à partir de l'objet.
		 * @param objectProvider Une fonction qui permet d'extraire l'objet à partir d'un autre.
		 * @param description La description de l'élément.
		 * @param displayed Vrai, si l'élément apparait sur le panneau d'inspection.
		 */
		public Element(String name, Function<Object, String> toString, Function<Object, Object> objectProvider, String description, boolean displayed) {
			this.name = name;
			this.toString = objectProvider.andThen(toString);
			this.description = description;
			this.displayed = displayed;
			this.objectProvider = objectProvider;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Element other = (Element) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			return name;
		}
	}
	
}
