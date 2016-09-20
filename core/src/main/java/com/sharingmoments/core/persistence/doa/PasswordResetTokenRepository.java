package com.sharingmoments.core.persistence.doa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sharingmoments.core.persistence.model.PasswordResetToken;
import com.sharingmoments.core.persistence.model.User;


public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUser(User user);

}
