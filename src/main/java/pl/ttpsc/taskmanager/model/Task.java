package pl.ttpsc.taskmanager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	private String description;

	// Usuwamy enum TaskStatus i dodajemy relację do encji Status
	@ManyToOne
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private AppUser user;

	// Usunięcie enuma TaskStatus
	// public enum TaskStatus {
	//     NEW, IN_PROGRESS, COMPLETED
	// }

	// Gettery i settery
	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public Status getStatus()
	{
		return status;
	}

	public void setStatus(Status status)
	{
		this.status = status;
	}

	public Category getCategory()
	{
		return category;
	}

	public void setCategory(Category category)
	{
		this.category = category;
	}

	public AppUser getUser()
	{
		return user;
	}

	public void setUser(AppUser user)
	{
		this.user = user;
	}
}