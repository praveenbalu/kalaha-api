package com.kalaha.exception;

/**
 * Throws during validation
 * @author Praveen
 */
public class KalahaValidationException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2424729157845096813L;

	/**
	 * Instantiates a new kalaha validation exception.
	 *
	 * @param message
	 */
	public KalahaValidationException(String message) {
		super(message);
	}
}
