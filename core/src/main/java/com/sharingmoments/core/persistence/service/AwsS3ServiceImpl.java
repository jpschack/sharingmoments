package com.sharingmoments.core.persistence.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;


@PropertySource({ "classpath:application.properties" })
@Service
public class AwsS3ServiceImpl implements AwsS3Service {
	
	@Autowired
	private AmazonS3 s3client;
	
	@Autowired
    private Environment env;
	
	public void uploadFile(InputStream is, String objectKey) {
		ObjectMetadata metaData = new ObjectMetadata();
		
		s3client.putObject(new PutObjectRequest(env.getProperty("cloud.aws.s3.bucket.name"), objectKey, is, metaData).withCannedAcl(CannedAccessControlList.PublicRead));
	}
	
	public void deleteFile(String objectKey) {
		s3client.deleteObject(new DeleteObjectRequest(env.getProperty("cloud.aws.s3.bucket.name"), objectKey));
	}
}