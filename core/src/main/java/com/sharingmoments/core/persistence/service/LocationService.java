package com.sharingmoments.core.persistence.service;

import java.util.UUID;

import com.sharingmoments.core.persistence.model.Location;

public interface LocationService {
	Location getLocationByID(UUID id);
	
	Location getLocationByGoogleLocationID(String googleLocationID);
	
	Location saveLocation(Location location);
}
