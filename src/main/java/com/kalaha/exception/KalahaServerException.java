package com.kalaha.exception;
/**
 * Throws Exception on Server related problem
 * @author Praveen
 */
public class KalahaServerException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6404051741366452443L;

	/**
	 * Instantiates a new kalaha server exception.
	 *
	 * @param message
	 */
	public KalahaServerException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new kalaha server exception.
	 *
	 * @param message
	 * @param exception
	 */
	public KalahaServerException(String message, Throwable ex) {
		super(message, ex);
	}
}
