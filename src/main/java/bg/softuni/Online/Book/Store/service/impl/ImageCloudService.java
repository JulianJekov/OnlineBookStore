package bg.softuni.Online.Book.Store.service.impl;

import com.cloudinary.Cloudinary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.UUID;

@Service
public class ImageCloudService {

    private final Cloudinary cloudinary;

    public ImageCloudService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadImg(MultipartFile multipartFile) {
        String imageId = UUID.randomUUID().toString();
        Map<String, String> params = Map.of(
                "public_id", imageId,
                "overwrite", "true",
                "resource_type", "image"
        );

        File tempFile = new File(imageId);

        try {
            Files.write(tempFile.toPath(), multipartFile.getBytes());
            String url = cloudinary.uploader().upload(tempFile, params).get("url").toString();
            Files.delete(tempFile.toPath());
            return url;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
