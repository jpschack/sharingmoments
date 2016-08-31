package com.sharingmoments.persistence.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sharingmoments.persistence.dto.UserDto;
import com.sharingmoments.persistence.model.PasswordResetToken;
import com.sharingmoments.persistence.model.User;
import com.sharingmoments.persistence.model.VerificationToken;
import com.sharingmoments.validation.EmailExistsException;
import com.sharingmoments.validation.UsernameExistsException;

public interface UserService {

    User registerNewUserAccount(UserDto accountDto) throws EmailExistsException, UsernameExistsException;

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void deleteUser(User user);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken generateNewVerificationToken(String token);

    void createPasswordResetTokenForUser(User user, String token);

    User findUserByEmail(String email);

    PasswordResetToken getPasswordResetToken(String token);

    User getUserByPasswordResetToken(String token);

    User getUserByID(UUID id);
    
    Page<User> findUserByUsernameOrName(String searchString, Pageable pageable);

    void changeUserPassword(User user, String password);

    boolean checkIfValidOldPassword(User user, String password);
    
    boolean emailExists(String email);
    
    boolean usernameExists(String username);
}
