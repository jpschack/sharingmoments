package com.sharingmoments.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sharingmoments.error.ResourceNotFoundException;
import com.sharingmoments.persistence.model.User;
import com.sharingmoments.persistence.model.UserImage;
import com.sharingmoments.persistence.service.AwsS3Service;
import com.sharingmoments.persistence.service.UserImageService;
import com.sharingmoments.security.CurrentUser;
import com.sharingmoments.security.UserDetailsImpl;


@RestController
@RequestMapping("/account/userImage")
public class UserImageController {
	
	@Autowired
    private Environment env;
	
	@Autowired
    private AwsS3Service awsS3Service;
	
	@Autowired
    private UserImageService userImageService;
	
	static Logger logger = Logger.getLogger(UserImageController.class.getName());
	
	@RequestMapping(value = {"/", ""}, method = RequestMethod.POST)
	public UserImage uploadUserImage(final Locale locale, @RequestParam("file") MultipartFile file, @CurrentUser Authentication authentication) {
		User user = (User) authentication.getPrincipal();
		
		try {
			UserImage userImage = userImageService.getUserImageByUser(user);
			
			if (userImage != null) {
				awsS3Service.deleteFile(userImage.getObjectKey());
				return updateFileToS3(file, userImage);
			} else {
				userImage = new UserImage();
				userImage.setUser(user);
				return updateFileToS3(file, userImage);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = {"/", ""}, method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUserImage(final Locale locale, @CurrentUser Authentication authentication) {
		UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
		UserImage userImage = userImageService.getUserImageByUser(user);
		
		if (userImage != null) {
			awsS3Service.deleteFile(userImage.getObjectKey());
			userImageService.deleteUserImage(userImage);
			return ResponseEntity.ok(null);
		} else {
			throw new ResourceNotFoundException();
		}
	}
	
	private UserImage updateFileToS3(MultipartFile file, UserImage userImage) throws IOException {
		InputStream is = file.getInputStream();
		
		final String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
		final String objectKey = UUID.randomUUID().toString() + fileExtension;
		
		awsS3Service.uploadFile(is, objectKey);
		
		String url = env.getProperty("aws.region.url") + env.getProperty("aws.bucket.name") + "/" + objectKey;
		
		userImage.setObjectKey(objectKey);
		userImage.setUrl(url);
		
		return userImageService.saveUserImage(userImage);
	}
}