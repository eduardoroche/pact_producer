package io.reflectoring;

import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class User {

	@NotNull
	private String id;

	@NotNull
	private String name;

}
