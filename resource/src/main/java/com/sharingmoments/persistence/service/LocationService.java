package com.sharingmoments.persistence.service;

import java.util.UUID;

import com.sharingmoments.persistence.model.Location;

public interface LocationService {
	Location getLocationByID(UUID id);
	
	Location getLocationByGoogleLocationID(String googleLocationID);
	
	Location saveLocation(Location location);
}
