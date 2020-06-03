package com.kalaha.exception;

/**
 * Exception for Client side problems.
 * @author Praveen
 * 
 */
public class KalahaClientException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2921960666921268083L;

	/**
	 * Instantiates a new kalaha client exception.
	 *
	 * @param message
	 */
	public KalahaClientException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new kalaha client exception.
	 *
	 * @param message
	 * @param exception
	 */
	public KalahaClientException(String message, Throwable ex) {
		super(message, ex);
	}
}
