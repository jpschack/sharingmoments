package com.sharingmoments.resource.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sharingmoments.resource.error.ResourceNotFoundException;
import com.sharingmoments.resource.persistence.model.User;
import com.sharingmoments.resource.persistence.model.UserImage;
import com.sharingmoments.resource.persistence.service.AwsS3Service;
import com.sharingmoments.resource.persistence.service.UserImageService;
import com.sharingmoments.resource.persistence.service.UserService;
import com.sharingmoments.resource.security.CurrentUser;


@RestController
@RequestMapping("/account/userImage")
public class UserImageController {
	
	@Autowired
    private Environment env;
	
	@Autowired
    private AwsS3Service awsS3Service;
	
	@Autowired
    private UserImageService userImageService;
	
	@Autowired
    private UserService userService;
	
	@RequestMapping(value = {"/", ""}, method = RequestMethod.POST)
	public UserImage uploadUserImage(final Locale locale, @RequestParam("file") MultipartFile file, @CurrentUser Authentication authentication) {
		final User user = getUserByAuth(authentication);
		
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
		final User user = getUserByAuth(authentication);
		UserImage userImage = userImageService.getUserImageByUser(user);
		
		if (userImage != null) {
			awsS3Service.deleteFile(userImage.getObjectKey());
			
			user.setUserImage(null);
			userService.saveRegisteredUser(user);
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
		
		String url = env.getProperty("cloud.aws.s3.region.url") + env.getProperty("cloud.aws.s3.bucket.name") + "/" + objectKey;
		
		userImage.setObjectKey(objectKey);
		userImage.setUrl(url);
		
		return userImageService.saveUserImage(userImage);
	}
	
	private User getUserByAuth(Authentication authentication) {
		final User principal = (User) authentication.getPrincipal();
		return userService.getUserByID(principal.getId());
	}
}