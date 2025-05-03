package pl.ttpsc.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.ttpsc.taskmanager.model.AppUser;
import pl.ttpsc.taskmanager.model.Category;
import pl.ttpsc.taskmanager.repository.CategoryRepository;


import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService
{
	private final CategoryRepository _categoryRepository;

	@Autowired
	public CategoryService(CategoryRepository categoryRepository)
	{
		this._categoryRepository = categoryRepository;
	}

	public List<Category> getCategoriesByUser(AppUser user)
	{
		return _categoryRepository.findAllByUser(user);
	}

	public Category getCategoryById(Long id)
	{
		return _categoryRepository.findById(id).orElseThrow();
	}


	public boolean saveCategory(Category category)
	{
		if(categoryExistsForUser(category.getName(), category.getUser())){
			return false;
		}

		_categoryRepository.save(category);
		return true;
	}

	public void deleteCategory(Long categoryId, AppUser user) throws AccessDeniedException
	{
		Category category = getCategoryById(categoryId);

		if(category != null)
		{
			if(category.getUser().getId().equals(user.getId())){
				_categoryRepository.delete(category);
			}
			else
			{
				throw new AccessDeniedException("Cannot delete this category - IDs don't match");
			}
		}
	}

	public void editCategory(Long categoryId, String newName, AppUser user) throws AccessDeniedException
	{

		Category category = getCategoryById(categoryId);

		if(category != null)
		{
			if(category.getUser().getId().equals(user.getId()))
			{
				Optional<Category> exists = getCategoriesByUser(user).stream().filter( c -> c
						.getName().equalsIgnoreCase(newName))
						.findFirst();

				if(exists.isPresent())
				{
					throw new AccessDeniedException("Cannot edit this category - another category has this name");
				}

				category.setName(newName);
				_categoryRepository.save(category);
			}
			else
			{
				throw new AccessDeniedException("Cannot edit this category - IDs don't match");
			}
		}
	}

	public boolean categoryExistsForUser(String name, AppUser user)
	{
		return _categoryRepository.findAllByUser(user).stream().anyMatch(c -> c.getName().equalsIgnoreCase(name));
	}
}
