package pl.ttpsc.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.ttpsc.taskmanager.model.AppUser;
import pl.ttpsc.taskmanager.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService
{
	private final UserRepository _userRepository;

	@Autowired
	public UserService(UserRepository userRepository)
	{
		this._userRepository = userRepository;
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
		{
			return false;
		}

		AppUser user = new AppUser();
		user.setUsername(username);
		user.setPassword(password);
		user.setRole("USER");

		_userRepository.save(user);

		return true;
	}
}
