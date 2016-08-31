package com.sharingmoments.persistence.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class UserDto {
    
	@NotNull
    @Email
    private String email;
	
    @NotNull
    @Size(min = 1)
    private String name;

    @NotNull
    @Size(min = 6)
    private String password;

    @NotNull
    @Size(min = 1)
    private String username;
    
    private boolean privateAccount;

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

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isPrivateAccount() {
		return privateAccount;
	}

	public void setPrivateAccount(boolean privateAccount) {
		this.privateAccount = privateAccount;
	}

	@Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("User [name=").append(name).append("]").append("[email").append(email).append("]").append("[password").append(password).append("]");
        return builder.toString();
    }
}
