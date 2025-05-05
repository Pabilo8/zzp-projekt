package pl.ttpsc.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ttpsc.taskmanager.model.AppUser;
import pl.ttpsc.taskmanager.model.Status;
import pl.ttpsc.taskmanager.model.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long>
{
	List<Task> findAllByUser(AppUser user);

	boolean existsByStatus(Status status); // zmiana z Task.TaskStatus na Status

	boolean existsByStatusId(Long statusId); // dodana metoda
}