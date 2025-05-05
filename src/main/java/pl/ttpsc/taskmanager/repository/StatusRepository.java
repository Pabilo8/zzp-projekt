package pl.ttpsc.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ttpsc.taskmanager.model.AppUser;
import pl.ttpsc.taskmanager.model.Status;

import java.util.List;

public interface StatusRepository extends JpaRepository<Status, Long> {
	List<Status> findAllByUser(AppUser user);
	boolean existsByNameAndUser(String name, AppUser user);
}