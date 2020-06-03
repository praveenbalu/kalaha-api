package com.kalaha.model.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

/**
 * Customized error response for all the error types.
 *
 * @param timestamp
 *            the timestamp
 * @param status
 *            the status
 * @param error
 *            the error
 */
@AllArgsConstructor

/**
 * Instantiates a new error response.
 */
@NoArgsConstructor
public class ErrorResponse implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -6079401671901909042L;

	/** The timestamp. */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime timestamp;

	/** The status. */
	private int status;

	/** The error. */
	private String error;
}
