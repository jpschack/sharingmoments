package com.sharingmoments.resource.persistence.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "photos")
public class Photo {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	private String objectKey;

	private String url;

	private String description;

	private Date createdAt;

    private Date updatedAt;

    @ManyToOne
	@JoinColumn(name="user_id")
    @JsonManagedReference
	private User user;
    
    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL)
    @JsonBackReference
    private Collection<Like> likes;
    
    @OneToMany(mappedBy = "photo", cascade = CascadeType.ALL)
    @JsonBackReference
    private Collection<Comment> comments;

    public Photo() {
    	super();
    	this.createdAt = new Date();
    	this.updatedAt = new Date();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getObjectKey() {
		return objectKey;
	}

	public void setObjectKey(String objectKey) {
		this.objectKey = objectKey;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Collection<Like> getLikes() {
		return likes;
	}

	public void setLikes(Collection<Like> likes) {
		this.likes = likes;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        final Photo photo = (Photo) obj;
        if (!id.equals(photo.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Photo {id:").append(id).append(",").append("objectKey:").append(objectKey).append(",").append("createdAt:").append(createdAt).append(",").append("updatedAt:").append(updatedAt).append("}");
        return builder.toString();
    }
}
