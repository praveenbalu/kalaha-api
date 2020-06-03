package com.kalaha.exception;

/**
 * Throws if the rule instance creation/execution is not valid 
 * @author Praveen
 */
public class KalahaInvalidRuleException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3962981825871891164L;

	/**
	 * Instantiates a new kalaha invalid rule exception.
	 *
	 * @param message
	 *            the message
	 */
	public KalahaInvalidRuleException(String message) {
		super(message);
	}
}
