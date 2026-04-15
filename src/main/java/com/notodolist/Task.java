package com.notodolist;

import jakarta.persistence.*;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private String priority;
    
 // Foreign key to User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // creates user_id column
    private User user;

    // Foreign key to Group
    @ManyToOne
    @JoinColumn(name = "group_id") // creates group_id column
    private Group group;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}

	public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }
}
