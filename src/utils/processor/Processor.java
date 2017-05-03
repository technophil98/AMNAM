package utils.processor;

import javax.swing.event.EventListenerList;

/**
 * Un processeur est un objet permettant l'exécution d'une méthode dans un {@link Thread} séparé et de suivre sa progression.
 * @author Pascal Dally-Bélanger
 *
 * @param <T> Le type de résultat produit par le processus.
 */
public abstract class Processor<T> {

	private double progress;
	private boolean isDone = false;

	private final EventListenerList LISTENERS = new EventListenerList();
	private Object callback;
	
	/**
	 * Initialise un nouveau processus.
	 */
	public Processor() {}
	
	/**
	 * Initialise un nouveau processus.
	 * @param callback un objet qui est notifier une fois la fin de l'exécution du {@link Thread}.
	 */
	public Processor(Object callback) {
		this.callback = callback;
	}
	
	/**
	 * Exécute le processus dans un {@link Thread} séparé.
	 */
	public void start() {
		new Thread(() -> {
			process();
			isDone = true;
			if(callback != null) {
				synchronized (callback) {
					callback.notifyAll();
				}
			}
			fireCompletionEvent();
		}, getClass().getName()).start();
	}
	
	/**
	 * Exécute le processus.
	 */
	protected abstract void process();
	
	/**
	 * Méthode permettant d'obtenir le résultat du processus.
	 * @return le résultat du processus.
	 */
	public abstract T getResult();
	
	/**
	 * Exécute le processus dans ce {@link Thread}.
	 * @return Le résultat du processus.
	 */
	public T execute() {
		process();
		return getResult();
	}
	
	/**
	 * Permet à un objet de mettre à jour sa progression.
	 * @param progress La nouvelle progression.
	 */
	protected void updateProgress(double progress) {
		fireUpdateProgressEvent(progress);
	}
	
	/**
	 * Retourne la progression (entre 0 et 1) du processus.
	 * @return la progression (entre 0 et 1) du processus.
	 */
	public double getProgress() {
		return progress;
	}

	/**
	 * Retourne vrai si le processus est terminé.
	 * @return Vrai si le processus est terminé
	 */
	public boolean isDone() {
		return isDone;
	}
	
	/**
	 * Lance un évènement de progrès.
	 * @param progress Le progrès de la génération.
	 */
	private void fireUpdateProgressEvent(double progress) {
		for(ProgressListener listener : LISTENERS.getListeners(ProgressListener.class))
			listener.updateProgress(progress);
	}
	
	/**
	 * Lance un évènement de completion.
	 */
	private void fireCompletionEvent() {
		for(ProgressListener listener : LISTENERS.getListeners(ProgressListener.class))
			listener.onCompletion();
	}
	
	/**
	 * Ajoute un écouteur d'évènement.
	 * @param listener L'écouteur d'évènement.
	 */
	public void addProgressListener(ProgressListener listener) {
		LISTENERS.add(ProgressListener.class, listener);
	}
}
