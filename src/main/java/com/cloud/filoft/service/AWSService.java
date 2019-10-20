package com.cloud.filoft.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

@Service
public class AWSService {
	
	private AmazonS3 s3client;
	private String awsS3Bucket;
	private String path;
	
	@Autowired
	public AWSService(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider, String awsS3Bucket,
			String path) {
		this.s3client = AmazonS3ClientBuilder.standard().withCredentials(awsCredentialsProvider)
				.withRegion(awsRegion.getName()).build();
		this.awsS3Bucket = awsS3Bucket;
		this.path = path;
	}
	
	public String uploadFileToS3(File s3File, String emailid) {

		try {
			String keyPath = emailid + "/" + s3File.getName();
			s3client.putObject(awsS3Bucket, keyPath, s3File);
			s3client.putObject(new PutObjectRequest(awsS3Bucket, keyPath, s3File)
					.withCannedAcl(CannedAccessControlList.PublicRead));
			return path + "/" + awsS3Bucket + "/" + keyPath;

		} catch (Exception e) {

			throw new RuntimeException("FAIL!");
		}
	}
	
	public byte[] downloadFile(String fileName, String emailid) {

		try {
			String keyPath = emailid + "/" + fileName;
			S3Object s3Object = s3client.getObject(new GetObjectRequest(awsS3Bucket, keyPath));
			S3ObjectInputStream inputStream = s3Object.getObjectContent();
			byte[] bytes = IOUtils.toByteArray(inputStream);
			return bytes;
			} catch (IOException e) {
			throw new RuntimeException("FAIL!");
		}

	}

}
