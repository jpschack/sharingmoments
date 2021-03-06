package com.sharingmoments.core.persistence.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sharingmoments.core.persistence.dto.UserDto;
import com.sharingmoments.core.persistence.model.PasswordResetToken;
import com.sharingmoments.core.persistence.model.User;
import com.sharingmoments.core.persistence.model.VerificationToken;
import com.sharingmoments.core.validation.EmailExistsException;
import com.sharingmoments.core.validation.UsernameExistsException;


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
    
    Page<User> findUserByUsernameOrName(String searchString, UUID currentUserId, Pageable pageable);

    void changeUserPassword(User user, String password);

    boolean checkIfValidOldPassword(User user, String password);
    
    boolean emailExists(String email);
    
    boolean usernameExists(String username);
}
