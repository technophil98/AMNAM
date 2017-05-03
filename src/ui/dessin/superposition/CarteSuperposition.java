package ui.dessin.superposition;

import scenario.map.MapEdge;
import scenario.map.SimulationMap;
import ui.composants.ColorChooserPanel;
import ui.dessin.Clickable;
import ui.dessin.Superposition;
import ui.event.UpdateRequestListener;
import ui.panneaux.side_panels.SidePanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

/**
 * La superposition où la carte est dessinée.
 *
 * @author Pascal Dally-Bélanger
 */
public class CarteSuperposition implements Superposition {
	
	public static final Color EDGE_COLOR = Color.black;
	public static final Color SIDEWALK_COLOR = new Color(98,98,98,140);
	
	private final EventListenerList EVENT_LISTENERS = new EventListenerList();
	private MapEdge[] edges;
	private MapEdge[] sidewalks;
	private boolean isEnabled = true;
	private AffineTransform transform;
	private Color edgeColor = EDGE_COLOR;
	private Color sidewalkColor = SIDEWALK_COLOR;
	private boolean renderEdge = true;
	private boolean renderSidewalks = true;
	
	/**
	 * Initialise une nouvelle CarteSuperposition avec la carte.
	 * @param map La carte.
	 */
	public CarteSuperposition(SimulationMap map) {
		edges = map.getEdges();
		sidewalks = map.getSidewalks();
	}
	
	@Override
	public void dessiner(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(1.5f));
		if(renderEdge) {
			Path2D pathEdges = new Path2D.Double();
			for(int i = 0; i < edges.length; i++) {
				pathEdges.moveTo(edges[i].getPoints()[0].getX(), edges[i].getPoints()[0].getY());
				for(int j = 1; j < edges[i].getPoints().length; j++)
					pathEdges.lineTo(edges[i].getPoints()[j].getX(), edges[i].getPoints()[j].getY());
			}
			g2d.setColor(edgeColor);
			g2d.draw(transform.createTransformedShape(pathEdges));
		}
		if(renderSidewalks) {
			Path2D pathSidewalks = new Path2D.Double();
			for(int i = 0; i < sidewalks.length; i++) {
				pathSidewalks.moveTo(sidewalks[i].getPoints()[0].getX(), sidewalks[i].getPoints()[0].getY());
				for(int j = 1; j < sidewalks[i].getPoints().length; j++)
					pathSidewalks.lineTo(sidewalks[i].getPoints()[j].getX(), sidewalks[i].getPoints()[j].getY());
			}
			g2d.setColor(sidewalkColor);
			g2d.draw(transform.createTransformedShape(pathSidewalks));
		}
		g2d.setStroke(new BasicStroke(1f));
	}

	@Override
	public void setTransform(AffineTransform transform) {
		this.transform = transform;
	}

	@Override
	public boolean isConfigurable() {
		return true;
	}

	@Override
	public SidePanel getPanneauConfiguration() {
		return new CarteSuperpositionConfig();
	}

	@Override
	public void setTemps(double t) {
		
	}

	@Override
	public Clickable[] getClickables() {
		return null;
	}

	@Override
	public boolean isClickable() {
		return false;
	}
	
	@Override
	public String getName() {
		return "Carte";
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		isEnabled = enabled;
	}

	/**
	 * Demande une mise à jour du panneaux contenant cette superposition.
	 */
	private void requestUpdate() {
		for(UpdateRequestListener listener : EVENT_LISTENERS.getListeners(UpdateRequestListener.class))
			listener.requestUpdate();
	}
	
	/**
	 * Un écouteur permettant d'écouter les requêtes de mise à jour de la superposition.
	 * @param listener L'écouteur à ajouter.
	 */
	public void addUpdateRequestHandler(UpdateRequestListener listener) {
		EVENT_LISTENERS.add(UpdateRequestListener.class, listener);
	}
	
	/**
	 * Le panneau latéral de configuration de la carte.
	 * @author Pascal Dally-Bélanger
	 */
	private class CarteSuperpositionConfig extends SidePanel {

		private static final long serialVersionUID = 5577183988784162855L;

		/**
		 * Instancie un panneau de configuration pour la superposition de la carte.
		 */
		public CarteSuperpositionConfig() {
			JPanel pnlMain = new JPanel();
			pnlMain.setBounds(10, 55, 330, 572);
			
			setTitle("Configuration de la carte");
			
			add(pnlMain);
			GridBagLayout gbl_pnlMain = new GridBagLayout();
			gbl_pnlMain.columnWidths = new int[] {0, 2};
			gbl_pnlMain.rowHeights = new int[] {16, 0, 0, 0, 2};
			gbl_pnlMain.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_pnlMain.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
			pnlMain.setLayout(gbl_pnlMain);
			
			JPanel pnlColor = new JPanel();
			pnlColor.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Couleur des diff\u00E9rents composants", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_pnlColor = new GridBagConstraints();
			gbc_pnlColor.fill = GridBagConstraints.BOTH;
			gbc_pnlColor.insets = new Insets(0, 0, 5, 0);
			gbc_pnlColor.gridx = 0;
			gbc_pnlColor.gridy = 0;
			pnlMain.add(pnlColor, gbc_pnlColor);
			GridBagLayout gbl_pnlColor = new GridBagLayout();
			gbl_pnlColor.columnWidths = new int[]{0, 0};
			gbl_pnlColor.rowHeights = new int[]{16, 0};
			gbl_pnlColor.columnWeights = new double[]{1.0, Double.MIN_VALUE};
			gbl_pnlColor.rowWeights = new double[]{0.0, Double.MIN_VALUE};
			pnlColor.setLayout(gbl_pnlColor);
			
			JPanel pnlInnerColor = new JPanel();
			GridBagConstraints gbc_pnlInnerColor = new GridBagConstraints();
			gbc_pnlInnerColor.fill = GridBagConstraints.HORIZONTAL;
			gbc_pnlInnerColor.gridx = 0;
			gbc_pnlInnerColor.gridy = 0;
			pnlColor.add(pnlInnerColor, gbc_pnlInnerColor);
			pnlInnerColor.setLayout(new GridLayout(0, 2, 0, 0));
			
			JLabel lblRoutes = new JLabel("Routes :");
			pnlInnerColor.add(lblRoutes);
			
			JPanel pnlColorContainerRoutes = new JPanel();
			pnlInnerColor.add(pnlColorContainerRoutes);
			
			ColorChooserPanel ccpRoutes = new ColorChooserPanel();
			ccpRoutes.addActionListener(e -> {
				edgeColor = ccpRoutes.getColor();
				requestUpdate();
			});
			ccpRoutes.setColor(edgeColor);
			pnlColorContainerRoutes.add(ccpRoutes);
			JLabel lblSidewalks = new JLabel("Trottoirs :");
			pnlInnerColor.add(lblSidewalks);
			
			JPanel pnlColorContainerSidewalks = new JPanel();
			pnlInnerColor.add(pnlColorContainerSidewalks);
			
			ColorChooserPanel ccpSidewalks = new ColorChooserPanel();
			ccpSidewalks.addActionListener(e -> {
				sidewalkColor = ccpSidewalks.getColor();
				requestUpdate();
			});
			ccpSidewalks.setColor(sidewalkColor);
			pnlColorContainerSidewalks.add(ccpSidewalks);
			
			JPanel pnlRender = new JPanel();
			pnlRender.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Rendu des diff\u00E9rents composants de la carte", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			GridBagConstraints gbc_pnlRender = new GridBagConstraints();
			gbc_pnlRender.insets = new Insets(0, 0, 5, 0);
			gbc_pnlRender.fill = GridBagConstraints.BOTH;
			gbc_pnlRender.gridx = 0;
			gbc_pnlRender.gridy = 1;
			pnlMain.add(pnlRender, gbc_pnlRender);
			pnlRender.setLayout(new BoxLayout(pnlRender, BoxLayout.X_AXIS));
			
			JPanel pnlInnterRender = new JPanel();
			pnlRender.add(pnlInnterRender);
			pnlInnterRender.setLayout(new GridLayout(0, 1, 0, 0));
			
			JCheckBox ckbRoutes = new JCheckBox("Routes");
			ckbRoutes.setSelected(renderEdge);
			ckbRoutes.addChangeListener(e -> {
				renderEdge = ckbRoutes.isSelected();
				requestUpdate();
			});
			pnlInnterRender.add(ckbRoutes);
			
			JCheckBox ckbSidewalks = new JCheckBox("Trottoirs");
			ckbSidewalks.setSelected(renderSidewalks);
			ckbSidewalks.addChangeListener(e -> {
				renderSidewalks = ckbSidewalks.isSelected();
				requestUpdate();
			});
			pnlInnterRender.add(ckbSidewalks);
		}
		
	}
}
