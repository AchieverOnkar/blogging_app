package com.backend.blog.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.backend.blog.exceptions.ResourceNotFoundException;
import com.backend.blog.services.FileService;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public String uploadImage(String path, MultipartFile file) throws IOException {

		// create folder if not present
		File folder = new File(path);
		if (!folder.exists()) {
			boolean created = folder.mkdir();
			if (!created) {
				throw new ResourceNotFoundException(path, "not found", 0);
			}
		}
		// get original file name
		String originalFilename = file.getOriginalFilename();
		// generate randomId
		String randomId = UUID.randomUUID().toString();
		// get originalFile Extension
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		// create modified new file name
		String modifiedFileName = randomId.concat(extension);
		// create full path
		String fullPath = path + File.separator + modifiedFileName;
		// copy file
		Files.copy(file.getInputStream(), Paths.get(fullPath));

		return modifiedFileName;
	}

	@Override
	public InputStream getResource(String fileName, String path) throws FileNotFoundException {
		// get full path
		String fullPath = path + File.separator + fileName;

		// the the source from the path
		FileInputStream inputStream = new FileInputStream(fullPath);

		return inputStream;

	}

}
