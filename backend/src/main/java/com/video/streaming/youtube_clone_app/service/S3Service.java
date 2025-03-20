package com.video.streaming.youtube_clone_app.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

/**
 * Service for handling file upload operations to AWS S3.
 * This implementation works with buckets that have ACLs disabled.
 */
@Service
public class S3Service implements FileService {

    /**
     * The name of the S3 bucket where files will be stored.
     */
    public static final String YOUTUBE_CLONE = "sksneha-youtube-clone";

    /**
     * The AmazonS3 client that will be injected by Spring.
     */
    private final AmazonS3 amazonS3;

    /**
     * Constructor for S3Service that takes an AmazonS3 client.
     *
     * @param amazonS3 The AmazonS3 client to use for S3 operations
     */
    @Autowired
    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    /**
     * Uploads a file to the S3 bucket and returns the URL.
     * This method works with buckets that have ACLs disabled.
     *
     * @param file The MultipartFile to upload
     * @return The URL of the uploaded file
     * @throws ResponseStatusException if an error occurs during upload
     */
    @Override
    public String uploadFile(MultipartFile file) {
        // Extract file extension from the original filename
        var filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        // Generate a unique key for the file using UUID plus the original extension
        var key = UUID.randomUUID() + "." + filenameExtension;

        // Create and configure metadata for the file
        var metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            // Create a PutObjectRequest without ACL settings
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    YOUTUBE_CLONE,
                    key,
                    file.getInputStream(),
                    metadata
            );

            // Upload the file to S3
            amazonS3.putObject(putObjectRequest);
        } catch (IOException e) {
            // If an error occurs during upload, throw a meaningful exception
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An exception occurred while uploading the file");
        }

        // Return the URL of the uploaded file
        return amazonS3.getUrl(YOUTUBE_CLONE, key).toString();
    }
}