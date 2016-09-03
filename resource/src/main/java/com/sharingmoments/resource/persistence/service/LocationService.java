package com.sharingmoments.resource.persistence.service;

import java.util.UUID;

import com.sharingmoments.resource.persistence.model.Location;

public interface LocationService {
	Location getLocationByID(UUID id);
	
	Location getLocationByGoogleLocationID(String googleLocationID);
	
	Location saveLocation(Location location);
}
