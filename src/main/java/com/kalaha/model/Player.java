package com.kalaha.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/**
 * 
 * Instantiates a new player.
 *
 * @param id
 * @param name
 * 
 */
@AllArgsConstructor
@NoArgsConstructor
public class Player implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	private Integer id;

	/** The name. */
	@NotNull
	private String name;

}
