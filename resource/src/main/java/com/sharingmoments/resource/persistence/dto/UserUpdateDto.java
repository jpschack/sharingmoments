package com.sharingmoments.resource.persistence.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class UserUpdateDto {
    
	@NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 1)
    private String username;
    
    private String name;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    private Integer role;

    public Integer getRole() {
        return role;
    }

    public void setRole(final Integer role) {
        this.role = role;
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("User [email=").append(email).append("]").append("[username").append(username).append("]");
        return builder.toString();
    }
}
