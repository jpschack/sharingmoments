package com.sharingmoments.persistence.model;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String email;

    private String username;

    private String name;

    @Column(length = 60)
    private String password;

    private Date createdAt;

    private Date updatedAt;

    private boolean privateAccount;

    private boolean enabled;

    private boolean tokenExpired;
    
    @OneToOne(fetch=FetchType.LAZY, mappedBy="user")
    @JsonManagedReference
    private UserImage userImage;

    @ManyToMany
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id") , inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id") )
    @JsonBackReference
    private Collection<Role> roles;

    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private Collection<Photo> photos;
    
    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private Collection<Event> events;
    
    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private Collection<Like> likes;
    
    @OneToMany(mappedBy = "user")
    @JsonBackReference
    private Collection<Comment> comments;

    public User() {
        super();
        this.enabled = false;
        this.tokenExpired = false;
        this.createdAt = new Date();
        this.updatedAt = new Date();
        this.privateAccount = false;
    }
    
    public User(User user) {
    	super();
    	
    	this.id = user.getId();
    	this.email = user.getEmail();
		this.username = user.getUsername();
		this.name = user.getName();
		this.password = user.getPassword();
		this.createdAt = user.getCreatedAt();
		this.updatedAt = user.getUpdatedAt();
		this.privateAccount = user.isPrivateAccount();
		this.enabled = user.isEnabled();
		this.tokenExpired = user.isTokenExpired();
		this.roles = user.getRoles();
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public UserImage getUserImage() {
		return userImage;
	}

	public void setUserImage(UserImage userImage) {
		this.userImage = userImage;
	}

	@JsonIgnore
	public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
    
    @JsonIgnore
    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(final Collection<Role> roles) {
        this.roles = roles;
    }

    @JsonIgnore
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    @JsonIgnore
    public boolean isTokenExpired() {
        return tokenExpired;
    }

    public void setTokenExpired(final boolean expired) {
        this.tokenExpired = expired;
    }

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public boolean isPrivateAccount() {
		return privateAccount;
	}

	public void setPrivateAccount(boolean privateAccount) {
		this.privateAccount = privateAccount;
	}

	public Collection<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(Collection<Photo> photos) {
		this.photos = photos;
	}

	public Collection<Event> getEvents() {
		return events;
	}

	public void setEvents(Collection<Event> events) {
		this.events = events;
	}

	public Collection<Like> getLikes() {
		return likes;
	}

	public void setLikes(Collection<Like> likes) {
		this.likes = likes;
	}

	public Collection<Comment> getComments() {
		return comments;
	}

	public void setComments(Collection<Comment> comments) {
		this.comments = comments;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User user = (User) obj;
        if (!email.equals(user.email)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("User {id:").append(id).append(",").append("email:").append(email).append(",").append("username:").append(username).append(",").append("createdAt:").append(createdAt).append(",").append("updatedAt:").append(updatedAt).append("}");
        return builder.toString();
    }
}
