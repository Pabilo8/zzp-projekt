package pl.ttpsc.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.ttpsc.taskmanager.model.AppUser;
import pl.ttpsc.taskmanager.model.Status;
import pl.ttpsc.taskmanager.repository.UserRepository;

@Service
public class UserService implements UserDetailsService
{
	private final UserRepository _userRepository;
	private final StatusService _statusService;

	@Autowired
	public UserService(UserRepository userRepository, StatusService statusService)
	{
		this._userRepository = userRepository;
		this._statusService = statusService;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		return _userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: "+username));
	}

	public boolean userExists(String username)
	{
		return _userRepository.findByUsername(username).isPresent();
	}

	public boolean saveUser(String username, String password)
	{
		if(userExists(username))
			return false;

		//Add a new user
		AppUser user = new AppUser();
		user.setUsername(username);
		user.setPassword(password);
		user.setRole("USER");
		_userRepository.save(user);

		//Add default status values for the user
		addDefaultStatus(user, "NEW");
		addDefaultStatus(user, "IN_PROGRESS");
		addDefaultStatus(user, "COMPLETED");

		return true;
	}

	private void addDefaultStatus(AppUser user, String statusName)
	{
		Status status = new Status();
		status.setUser(user);
		status.setName(statusName);
		_statusService.saveStatus(status);
	}
}
