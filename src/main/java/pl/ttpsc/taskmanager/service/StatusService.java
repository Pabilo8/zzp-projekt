package pl.ttpsc.taskmanager.service;

import org.springframework.stereotype.Service;
import pl.ttpsc.taskmanager.model.AppUser;
import pl.ttpsc.taskmanager.model.Status;
import pl.ttpsc.taskmanager.repository.StatusRepository;
import pl.ttpsc.taskmanager.repository.TaskRepository;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class StatusService
{
	private final StatusRepository statusRepository;
	private final TaskRepository taskRepository;

	public StatusService(StatusRepository statusRepository, TaskRepository taskRepository)
	{
		this.statusRepository = statusRepository;
		this.taskRepository = taskRepository;
	}

	public List<Status> getStatusesByUser(AppUser user)
	{
		return statusRepository.findAllByUser(user);
	}

	public Status getStatusById(Long id)
	{
		return statusRepository.findById(id).orElse(null);
	}

	public boolean saveStatus(Status status)
	{
		try
		{
			if(statusRepository.existsByNameAndUser(status.getName(), status.getUser()))
			{
				return false;
			}
			statusRepository.save(status);
			return true;
		} catch(Exception e)
		{
			return false;
		}
	}

	public boolean editStatus(Long id, String name, AppUser user) throws AccessDeniedException
	{
		Status status = statusRepository.findById(id).orElse(null);
		if(status==null)
		{
			return false;
		}

		if(!status.getUser().getId().equals(user.getId()))
		{
			throw new AccessDeniedException("Brak dostępu do tego statusu");
		}

		if(statusRepository.existsByNameAndUser(name, user)&&
				!status.getName().equalsIgnoreCase(name))
		{
			return false;
		}

		status.setName(name);
		statusRepository.save(status);
		return true;
	}

	public boolean deleteStatus(Long id, AppUser user) throws AccessDeniedException
	{
		Status status = statusRepository.findById(id).orElse(null);
		if(status==null)
		{
			return false;
		}

		if(!status.getUser().getId().equals(user.getId()))
		{
			throw new AccessDeniedException("Brak dostępu do tego statusu");
		}

		if(taskRepository.existsByStatusId(id))
		{
			return false;
		}

		statusRepository.delete(status);
		return true;
	}
}