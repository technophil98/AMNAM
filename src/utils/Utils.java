package utils;

import java.util.function.Function;

/**
 * Classe de méthode utilitaire.
 * @author Pascal Dally-Bélanger
 */
public final class Utils {

	
	public static final String REGEX_DECIMAL = "[+-]?\\d+\\.?\\d*(?:e[+-]\\d+)?";
	
	/**
	 * Contructeur privé afin d'empêcher toute instanciation.
	 */
	private Utils(){}

	/**
	 * Génère un Runnable qui n'émet pas d'exception à partir d'un RunnableException.
	 * @param runnable Le RunnableException.
	 * @return un Runnable qui n'émet pas d'exception.
	 */
	public static Runnable ignorerException(RunnableException runnable) {
		return () -> {
			try {
				runnable.run();
			} catch(Throwable e) {
				e.printStackTrace();
			}
		};
	}

	/**
	 * Génère une Function qui n'émet pas d'exception à partir d'une FunctionException.
	 * @param <T> Le type d'entrée de la fonction.
	 * @param <R> Le type de sortie de la fonction.
	 * @param function La fonction de base.
	 * @return une Runnable qui n'émet pas d'exception.
	 */
	public static <T, R> Function<T, R> ignorerException(FunctionException<T, R> function) {
		return t -> {
			try {
				return function.evaluate(t);
			} catch(Throwable e) {
				return null;
			}
		};
	}
	
	/**
	 * Interface fonctionelle qui a la même fonction que l'interface Runnable,
	 * mais qui permet que la méthode run lance une exception.
	 * @author Pascal Dally-Bélanger
	 */
	@FunctionalInterface
	public static interface RunnableException {
		/**
		 * Execute la méthode.
		 * @throws Throwable Si une exception survient lors de l'execution.
		 */
		public void run() throws Throwable;
	}
	
	/**
	 * Interface fonctionelle qui a la même fonction que l'interface Function,
	 * mais qui permet que la méthode run lance une exception.
	 * @param <T> Le type d'entrée de la fonction.
	 * @param <R> Le type de sortie de la fonction.
	 * @author Pascal Dally-Bélanger
	 */
	@FunctionalInterface
	public static interface FunctionException<T, R> {
		/**
		 * Évalue la fonction.
		 * @param t Le paramètre de la fonction.
		 * @return La valeur de retour de la méthode
		 * @throws Throwable Si une exception survient lors de l'execution.
		 */
		public R evaluate(T t) throws Throwable;
	}
}
