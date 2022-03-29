package com.orphan.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The class for token with enum related to it.
 *
 */
@Getter
@AllArgsConstructor
public enum TokenEnum {
	/**
	 * <p>Token use for create user</p>
	 * code: Token type</br>
	 * value: Token time(<b>Minutes</b>)
	 */
	TOKEN_CREATE_USER	(0, 5),
	/**
	 * <p>Token use for reset password</p>
	 * code: Token type</br>
	 * value: Token time(<b>Minutes</b>)
	 */
	TOKEN_RESET_PASSWORD	(1, 5),

	/**
	 * <p>Token use for reset password</p>
	 * code: Token type</br>
	 * value: Token time(<b>Minutes</b>)
	 */
	TOKEN_CHANGE_PASSWORD	(1, 5),
	/**
	 * <p>Remember me Token expired</p>
	 * code: Token type</br>
	 * value: Token time(<b>Days</b>)
	 */
	
	TOKEN_REMEMBER_ME_EXPIRED	(2, 30),
	
	/**
	 * <p>JWT Token expired</p>
	 * code: Token type</br>
	 * value: Token time(<b>Days</b>)
	 */
	TOKEN_JWT_EXPIRED	(3, 7),

	/**
	 * <p>Refresh token expired</p>
	 * code: Token type</br>
	 * value: Token time(<b>Days</b>)
	 */

	REFRESH_TOKEN_EXPIRED	(2, 60);
	private final int code;
	private final int value;
}