package db6.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import db6.domain.AppUser;
import db6.domain.Person;
import db6.domain.repository.AppUserRepository;
import db6.domain.repository.PersonRepository;
import db6.dto.UserRegistrationRequest;

// Service managing user registration
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
        Person person = new Person(request.getFirstName(), null, request.getLastName(), request.getGender(), null, request.getDateOfBirth(), null, null);
        person = personRepository.save(person);
        assert(person != null);

        // Create a new user
        AppUser user = new AppUser(request.getEmail(), passwordService.encode(request.getPassword()), "USER", person);
        userRepository.save(user);

        // Set logged user ID in the person entity
        person.setUserId(user.getId());
        personRepository.save(person);
    }

    public boolean isEmailTaken(String email) {
        return userRepository.existsByEmail(email);
    }

    public Long getUserPersonId(String email) {
        Optional<AppUser> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPerson() != null) {
            return user.get().getPerson().getId();
        }
        return null;
    }

}
