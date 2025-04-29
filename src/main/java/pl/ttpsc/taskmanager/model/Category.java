package pl.ttpsc.taskmanager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "categories",
		uniqueConstraints = {
			@UniqueConstraint(columnNames = {"user_id", "name"})
	}
)
public class Category
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "name", nullable = false)
	private String name;
	@JoinColumn(name = "user_id", nullable = false)
	@ManyToOne
	private AppUser user;

	public Long getId(){
		return this.id;
	}

	public String getName(){
		return this.name;
	}

	public void setName(String name){
		this.name = name;
	}

	public AppUser getUser(){
		return this.user;
	}

	public void setUser(AppUser user){
		this.user = user;
	}
}
