package com.sharingmoments.core.persistence.service;

import java.io.InputStream;

public interface AwsS3Service {
	void uploadFile(InputStream is, String objectKey);
	void deleteFile(String objectKey);
}
