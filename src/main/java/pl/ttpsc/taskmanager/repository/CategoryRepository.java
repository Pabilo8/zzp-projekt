package pl.ttpsc.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ttpsc.taskmanager.model.AppUser;
import pl.ttpsc.taskmanager.model.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>
{

	List<Category> findAllByUser(AppUser user);

}
