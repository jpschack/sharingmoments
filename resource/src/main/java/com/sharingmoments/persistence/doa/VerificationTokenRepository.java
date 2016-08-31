package com.sharingmoments.persistence.doa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sharingmoments.persistence.model.User;
import com.sharingmoments.persistence.model.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

}
