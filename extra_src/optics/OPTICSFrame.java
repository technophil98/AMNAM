package optics;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Une JFrame qui est utilisée pour les tests de K-moyenne.
 * @author Pascal Dally-Bélanger
 *
 */
public class OPTICSFrame extends JFrame {
	
	
	private static final long serialVersionUID = -5272573222264777809L;
	private OPTICSPanel moyennePanel;
	private JButton btnIteration;
	private JButton btnReinit;
	private JButton btnAnimer;
	private JButton btnPause;
	
	/**
	 * Initialise un KMoyenneFrame.
	 */
	public OPTICSFrame() {

		setBounds(0, 0, 885, 830);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(500, 420));
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		getContentPane().setLayout(null);
		
		moyennePanel = new OPTICSPanel();
		moyennePanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		moyennePanel.setBounds(109, 11, 750, 770);
		getContentPane().add(moyennePanel);
		
		btnIteration = new JButton("Iteration");
		btnIteration.addActionListener(arg0 -> moyennePanel.prochain());
		btnIteration.setBounds(10, 11, 89, 23);
		getContentPane().add(btnIteration);
		
		btnReinit = new JButton("Reinitialiser");
		btnReinit.addActionListener(arg0 -> moyennePanel.reinitialiser());
		btnReinit.setBounds(10, 45, 89, 23);
		getContentPane().add(btnReinit);
		
		btnAnimer = new JButton("Animer");
		btnAnimer.addActionListener(arg0 -> moyennePanel.animer());
		btnAnimer.setBounds(10, 79, 89, 23);
		getContentPane().add(btnAnimer);
		
		btnPause = new JButton("Pause");
		btnPause.addActionListener(e -> moyennePanel.arreter());
		btnPause.setBounds(10, 112, 89, 23);
		getContentPane().add(btnPause);
		
	}
}
