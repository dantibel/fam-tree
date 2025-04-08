package db6.service;

import org.springframework.stereotype.Service;

import db6.domain.AppUser;
import db6.domain.Person;
import db6.domain.repository.AppUserRepository;
import db6.domain.repository.PersonRepository;
import db6.dto.UserRegistrationRequest;

@Service
public class AppUserService {

    private final AppUserRepository userRepository;

    private final PersonRepository personRepository;

    private final PasswordService passwordService;

    public AppUserService(AppUserRepository userRepository, 
                          PersonRepository personRepository, 
                          PasswordService passwordService) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.passwordService = passwordService;
    }

    public void registerUser(UserRegistrationRequest request) {
        // Create a new person associated with the user
        Person person = new Person(request.getFirstName(), null, request.getLastName(), request.getGender(), null, request.getDateOfBirth(), null);
        person = personRepository.save(person);
        assert(person != null);

        // Create a new user
        AppUser user = new AppUser(request.getEmail(), passwordService.encode(request.getPassword()), "USER", person);
        userRepository.save(user);
    }

    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

}
