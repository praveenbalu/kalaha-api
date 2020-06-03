package com.kalaha.model.request;

import java.io.Serializable;

import org.springframework.lang.NonNull;

import com.kalaha.model.Player;

import lombok.Data;
import lombok.NoArgsConstructor;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data

/**
 * Instantiates a new play.
 */
@NoArgsConstructor
public class Play implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4427068787541928417L;

	/** The player. */
	@NonNull
	private Player player;

	/** The current move. */
	@NonNull
	private Integer currentMove;
}
