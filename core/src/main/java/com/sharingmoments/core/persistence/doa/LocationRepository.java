package com.sharingmoments.core.persistence.doa;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.sharingmoments.core.persistence.model.Location;

public interface LocationRepository extends PagingAndSortingRepository<Location, UUID> {
	Location findByGoogleLocationID(String googleLocationID);
}
