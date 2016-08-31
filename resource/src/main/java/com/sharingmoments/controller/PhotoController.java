package com.sharingmoments.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sharingmoments.persistence.model.Photo;
import com.sharingmoments.persistence.model.User;
import com.sharingmoments.persistence.service.AwsS3Service;
import com.sharingmoments.persistence.service.PhotoService;
import com.sharingmoments.persistence.service.UserService;
import com.sharingmoments.util.GenericResponse;


@RestController
@RequestMapping("/photo")
public class PhotoController {
	
	@Autowired
    private MessageSource messages;
	
	@Autowired
    private Environment env;
	
	@Autowired
    private AwsS3Service awsS3Service;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private PhotoService photoService;
	
	static Logger logger = Logger.getLogger(PhotoController.class.getName());
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public GenericResponse uploadPhoto(final Locale locale, @RequestParam("file") MultipartFile file) throws Exception {
		final User user = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
		
		try {
			InputStream is = file.getInputStream();
			
			final String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
			final String objectKey = UUID.randomUUID().toString() + fileExtension;
			
			awsS3Service.uploadFile(is, objectKey);
			
			final String url = env.getProperty("aws.region.url") + env.getProperty("aws.bucket.name") + "/" + objectKey;
			
			Photo photo = new Photo();
			photo.setObjectKey(objectKey);
			photo.setUrl(url);
			photo.setUser(user);
			//photo.setDescription(description); //has to be filled by json requestParam: http://stackoverflow.com/questions/27294838/how-to-process-a-multipart-request-consisting-of-a-file-and-a-json-object-in-spr/30043173#30043173
			
			return new GenericResponse("", photoService.savePhoto(photo));
		} catch (IOException e) {
			logger.error("Error by trying to upload photo - ", e);
			throw new Exception();
		}
	}
	
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public GenericResponse updatePhoto(final Locale locale, @RequestPart("photo") Photo photo, @RequestPart("file") MultipartFile file) throws Exception {
		final User user = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
		
		try {
			awsS3Service.deleteFile(photo.getObjectKey());
			
			InputStream is = file.getInputStream();
			
			final String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
			final String objectKey = UUID.randomUUID().toString() + fileExtension;
			
			awsS3Service.uploadFile(is, objectKey);
			
			final String url = env.getProperty("aws.region.url") + env.getProperty("aws.bucket.name") + "/" + objectKey;
			
			photo.setObjectKey(objectKey);
			photo.setUrl(url);
			photo.setUser(user);
			
			return new GenericResponse("", photoService.savePhoto(photo));
		} catch (IOException e) {
			logger.error("Error by trying to updating photo - ", e);
			throw new Exception();
		}
	}
	
	@RequestMapping(value = "/", method = RequestMethod.DELETE)
	public GenericResponse deletePhoto(final Locale locale, @RequestParam("photo") Photo photo) {
		final User user = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
		
		if (photo.getUser().equals(user)) {
			awsS3Service.deleteFile(photo.getObjectKey());
			
			photoService.deletePhoto(photo);
			
			return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale));
		} else {
			return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale));
		}
	}
}
