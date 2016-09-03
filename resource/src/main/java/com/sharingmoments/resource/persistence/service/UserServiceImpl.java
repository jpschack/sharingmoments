package com.sharingmoments.resource.persistence.service;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sharingmoments.resource.persistence.doa.PasswordResetTokenRepository;
import com.sharingmoments.resource.persistence.doa.RoleRepository;
import com.sharingmoments.resource.persistence.doa.UserRepository;
import com.sharingmoments.resource.persistence.doa.VerificationTokenRepository;
import com.sharingmoments.resource.persistence.dto.UserDto;
import com.sharingmoments.resource.persistence.model.PasswordResetToken;
import com.sharingmoments.resource.persistence.model.User;
import com.sharingmoments.resource.persistence.model.VerificationToken;
import com.sharingmoments.resource.validation.EmailExistsException;
import com.sharingmoments.resource.validation.UsernameExistsException;


@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private PasswordResetTokenRepository passwordTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User registerNewUserAccount(final UserDto accountDto) throws EmailExistsException, UsernameExistsException {
        if (emailExists(accountDto.getEmail())) {
            throw new EmailExistsException("Account with this email address already exists", "message.regError.email.exists");
        }
        
        if (usernameExists(accountDto.getUsername())) {
        	throw new UsernameExistsException("Account with this username already exists", "message.regError.username.exists");
        }
        
        final User user = new User();

        user.setName(accountDto.getName());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());
        user.setUsername(accountDto.getUsername());
        
        if (accountDto.isPrivateAccount()) {
        	user.setPrivateAccount(accountDto.isPrivateAccount());
        }

        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        return repository.save(user);
    }

    @Override
    public User getUser(final String verificationToken) {
        final User user = tokenRepository.findByToken(verificationToken).getUser();
        return user;
    }

    @Override
    public VerificationToken getVerificationToken(final String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public void saveRegisteredUser(final User user) {
    	user.setUpdatedAt(new Date());
        repository.save(user);
    }

    @Override
    public void deleteUser(final User user) {
        repository.delete(user);
    }

    @Override
    public void createVerificationTokenForUser(final User user, final String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken generateNewVerificationToken(final String existingVerificationToken) {
        VerificationToken vToken = tokenRepository.findByToken(existingVerificationToken);
        vToken.updateToken(UUID.randomUUID().toString());
        vToken = tokenRepository.save(vToken);
        return vToken;
    }

    @Override
    public void createPasswordResetTokenForUser(final User user, final String token) {
        final PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

    @Override
    public User findUserByEmail(final String email) {
        return repository.findByEmail(email);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token);
    }

    @Override
    public User getUserByPasswordResetToken(final String token) {
        return passwordTokenRepository.findByToken(token).getUser();
    }

    @Override
    public User getUserByID(final UUID id) {
        return repository.findOne(id);
    }

    @Override
    public void changeUserPassword(final User user, final String password) {
        user.setPassword(passwordEncoder.encode(password));
        user.setUpdatedAt(new Date());
        repository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public boolean emailExists(final String email) {
        final User user = repository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }
    
    @Override
    public boolean usernameExists(final String username) {
        final User user = repository.findByUsername(username);
        if (user != null) {
            return true;
        }
        return false;
    }

	@Override
	public Page<User> findUserByUsernameOrName(String searchString, UUID currentUserId, Pageable pageable) {
		return repository.findByUsernameOrName(searchString, currentUserId, pageable);
	}
}