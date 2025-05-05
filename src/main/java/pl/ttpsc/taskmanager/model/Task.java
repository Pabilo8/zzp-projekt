package pl.ttpsc.taskmanager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	private String description;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TaskStatus status;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private AppUser user;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	public enum TaskStatus {
		NEW, IN_PROGRESS, COMPLETED
	}

	// gettery i settery
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }

	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }

	public TaskStatus getStatus() { return status; }
	public void setStatus(TaskStatus status) { this.status = status; }

	public AppUser getUser() { return user; }
	public void setUser(AppUser user) { this.user = user; }

	public Category getCategory() { return category; }
	public void setCategory(Category category) { this.category = category; }
}