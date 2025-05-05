package pl.ttpsc.taskmanager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "statuses")
public class Status {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private AppUser user;

	// gettery i settery
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public AppUser getUser() { return user; }
	public void setUser(AppUser user) { this.user = user; }
}