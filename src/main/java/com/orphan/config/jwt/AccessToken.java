package com.orphan.config.jwt;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class AccessToken {
	@ApiModelProperty(value = "Access token", readOnly = true)
	private String token;
	@ApiModelProperty(value = "Access token expried date", readOnly = true)
	private Date expired;

	@ApiModelProperty(value = "Refresh token")
	@NotNull(message = "Refresh token {validation.not-null}")
	@NotEmpty(message = "Refresh token {validation.not-empty}")
	private String refreshToken;

	@ApiModelProperty(value = "User Id", readOnly = true)
	private Integer userId;

	@ApiModelProperty(readOnly = true)
	private String tokenType;
}
