/*
 * Created by JFormDesigner on Wed Mar 01 16:07:23 EST 2017
 */

package ui.fenetres;

import io.amnam.FichierAMNAM;
import ui.UICommonUtils;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import static ui.UICommonUtils.PATH_MARIACHI_MUSIC;
import static ui.UICommonUtils.PATH_IMG_AMNAM_LOGO_URL;

/**
 * La première fenêtre affichée à l'utilisateur où il est invité à choisir un fichier .amnam pour poursuivre.
 *
 * @author Philippe Rivest
 */
public class EcranAccueilFrame extends JFrame {


	private static final long serialVersionUID = -4663728363401590844L;

	private AudioInputStream audioInputStream;
	private Clip clip;

	/**
	 * Instancies un nouveau EcranAccueilFrame.
	 */
	public EcranAccueilFrame() {
		initComponents();

		try {
			audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource(PATH_MARIACHI_MUSIC));
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Nouveau fichier AMNAM » de l'écran d'accueil.
	 *
	 * @param e Évènement de clic
	 */
	private void buttonNouveauActionPerformed(ActionEvent e) {
		clip.flush();
		UICommonUtils.transfererEtape(this, new CreationFichierAMNAMFrame());
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Ouvrir un fichier AMNAM » de l'écran d'accueil.
	 *
	 * @param e Évènement de clic
	 */
	private void buttonOuvrirActionPerformed(ActionEvent e) {

		FichierAMNAM fichier = null;

		try {
			fichier = FichierAMNAM.chargerUnFichier("Choisir un fichier .amnam", this);
		} catch (IOException excep) {
			excep.printStackTrace();
			JOptionPane.showMessageDialog(this, "Erreur dans le chargement du fichier .amnam.\nVeuillez r\u00e9essayer.", "Erreur", JOptionPane.ERROR_MESSAGE);
		}

		if (fichier != null) {
			clip.flush();
			UICommonUtils.transfererEtape(this, new InspecteurFichierAMNAMFrame(fichier));
		}
	}

	/**
	 * Méthode exécutée lors du clic sur le bouton « Quitter ».
	 *
	 * @param e Évènement de clic
	 */
	private void buttonQuitterActionPerformed(ActionEvent e) {
		System.exit(0);
	}

	/**
	 * Méthode exécutée lors du clic sur le logo. Peut contenir des surprises...
	 *
	 * @param e Évènement de clic
	 */
	private void logoAMNAMMouseClicked(MouseEvent e) {

		if (audioInputStream != null && clip != null) {

			if (e.getClickCount() >= 8 && !clip.isRunning()) {
				clip.start();
			}
		}
	}

	/**
	 * Initialise tous les composants graphiques de la fenêtre.
	 */
	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner non-commercial license
		this.logoAMNAM = new JLabel();
		this.labelTitre = new JLabel();
		this.labelSousTitre = new JLabel();
		this.logoLRIMa = new JLabel();
		this.buttonOuvrir = new JButton();
		this.buttonNouveau = new JButton();
		this.buttonQuitter = new JButton();
		this.textArea1 = new JTextArea();

		//======== this ========
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Accueil | AMNAM"); //$NON-NLS-1$ //NON-NLS
		Container contentPane = getContentPane();
		contentPane.setLayout(null);

		//---- logoAMNAM ----
		this.logoAMNAM.setIcon(null);
		this.logoAMNAM.setHorizontalAlignment(SwingConstants.CENTER);
		this.logoAMNAM.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				logoAMNAMMouseClicked(e);
			}
		});
		contentPane.add(this.logoAMNAM);
		this.logoAMNAM.setBounds(70, 70, 237, 240);

		//---- labelTitre ----
		this.labelTitre.setText("AMNAM"); //$NON-NLS-1$ //NON-NLS
		this.labelTitre.setFont(new Font(".SF NS Text", Font.PLAIN, 26)); //$NON-NLS-1$ //NON-NLS
		contentPane.add(this.labelTitre);
		this.labelTitre.setBounds(new Rectangle(new Point(138, 10), this.labelTitre.getPreferredSize()));

		//---- labelSousTitre ----
		this.labelSousTitre.setText("<html><strong>AM</strong>nam <strong>N</strong>'est <strong>A</strong>ucunement <em><strong>M</strong>aps</em></html>"); //$NON-NLS-1$ //NON-NLS
		this.labelSousTitre.setFont(new Font(".SF NS Text", Font.PLAIN, 18)); //$NON-NLS-1$ //NON-NLS
		contentPane.add(this.labelSousTitre);
		this.labelSousTitre.setBounds(new Rectangle(new Point(46, 40), this.labelSousTitre.getPreferredSize()));

		//---- logoLRIMa ----
		this.logoLRIMa.setIcon(new ImageIcon(getClass().getResource("/img/logo_lrima.png"))); //$NON-NLS-1$ //NON-NLS
		contentPane.add(this.logoLRIMa);
		this.logoLRIMa.setBounds(10, 450, 125, 85);

		//---- buttonOuvrir ----
		this.buttonOuvrir.setText("Ouvrir un fichier AMNAM"); //$NON-NLS-1$ //NON-NLS
		this.buttonOuvrir.addActionListener(e -> buttonOuvrirActionPerformed(e));
		contentPane.add(this.buttonOuvrir);
		this.buttonOuvrir.setBounds(70, 325, 237, 32);

		//---- buttonNouveau ----
		this.buttonNouveau.setText("Nouveau fichier AMNAM"); //$NON-NLS-1$ //NON-NLS
		this.buttonNouveau.addActionListener(e -> buttonNouveauActionPerformed(e));
		contentPane.add(this.buttonNouveau);
		this.buttonNouveau.setBounds(70, 360, 237, this.buttonNouveau.getPreferredSize().height);

		//---- buttonQuitter ----
		this.buttonQuitter.setText("Quitter"); //$NON-NLS-1$ //NON-NLS
		this.buttonQuitter.addActionListener(e -> buttonQuitterActionPerformed(e));
		contentPane.add(this.buttonQuitter);
		this.buttonQuitter.setBounds(70, 405, 237, this.buttonQuitter.getPreferredSize().height);

		//---- textArea1 ----
		this.textArea1.setText("Projet de recherche sur les communications inter-v\u00e9hiculaires. D\u00e9velopp\u00e9 dans le Laboratoire de recherche informatique du Coll\u00e8ge de Maisonneuve (LRIMa)."); //$NON-NLS-1$ //NON-NLS
		this.textArea1.setFont(UIManager.getFont("Label.font")); //$NON-NLS-1$ //NON-NLS
		this.textArea1.setEditable(false);
		this.textArea1.setWrapStyleWord(true);
		this.textArea1.setLineWrap(true);
		this.textArea1.setBackground(UIManager.getColor("Button.background")); //$NON-NLS-1$ //NON-NLS
		contentPane.add(this.textArea1);
		this.textArea1.setBounds(145, 450, 230, 85);

		{ // compute preferred size
			Dimension preferredSize = new Dimension();
			for(int i = 0; i < contentPane.getComponentCount(); i++) {
				Rectangle bounds = contentPane.getComponent(i).getBounds();
				preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
				preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
			}
			Insets insets = contentPane.getInsets();
			preferredSize.width += insets.right;
			preferredSize.height += insets.bottom;
			contentPane.setMinimumSize(preferredSize);
			contentPane.setPreferredSize(preferredSize);
		}
		setSize(380, 570);
		setLocationRelativeTo(null);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents

		UICommonUtils.addIconToLabel(this.logoAMNAM, PATH_IMG_AMNAM_LOGO_URL);

	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner non-commercial license
	private JLabel logoAMNAM;
	private JLabel labelTitre;
	private JLabel labelSousTitre;
	private JLabel logoLRIMa;
	private JButton buttonOuvrir;
	private JButton buttonNouveau;
	private JButton buttonQuitter;
	private JTextArea textArea1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
