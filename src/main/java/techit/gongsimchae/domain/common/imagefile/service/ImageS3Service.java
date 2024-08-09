package techit.gongsimchae.domain.common.imagefile.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import techit.gongsimchae.domain.common.imagefile.entity.ImageFile;
import techit.gongsimchae.domain.common.imagefile.repository.ImageFileRepository;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.groupbuying.item.entity.Item;
import techit.gongsimchae.domain.groupbuying.post.entity.Post;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;
import techit.gongsimchae.global.exception.CustomWebException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageS3Service {
    private final AmazonS3 amazonS3;

    private final UserRepository userRepository;
    private final ImageFileRepository imageFileRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<ImageFile> storeFiles(List<MultipartFile> files, String directory, Long userId, Object object) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomWebException("해당 유저를 찾을 수 없습니다."));

        List<ImageFile> uploadFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            if(!file.isEmpty()) {
                uploadFiles.add(storeFile(file, directory, object));
            }

        }

        imageFileRepository.saveAll(uploadFiles);

        return uploadFiles;
    }
    @Transactional
    public ImageFile storeFile(MultipartFile file, String directory, Object object) {
        if (file.isEmpty()) {
            return null;
        }
        String storeFileName = storeFileName(file);
        String originalFilename = file.getOriginalFilename();
        String uploadUrl = getUrl(directory);
        ImageFile imageFile;

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentDisposition(file.getContentType());
            metadata.setContentLength(file.getSize());
            amazonS3.putObject(uploadUrl, storeFileName, file.getInputStream(), metadata);

            if (object instanceof Post) {
                imageFile = new ImageFile(originalFilename, getFullPath(directory, storeFileName), (Post) object);
            } else if (object instanceof Item) {
                imageFile = new ImageFile(originalFilename, getFullPath(directory, storeFileName), (Item) object);
            } else if (object instanceof Subdivision) {
                imageFile = new ImageFile(originalFilename, getFullPath(directory, storeFileName), (Subdivision) object);
            } else if (object instanceof User) {
                imageFile = new ImageFile(originalFilename, getFullPath(directory, storeFileName), (User) object);
            } else {
                throw new CustomWebException("이미지를 저장할 수 없는 객체입니다.");
            }
            return imageFileRepository.save(imageFile);
        } catch (Exception e) {
            throw new CustomWebException(e.getMessage());
        }
    }



    private String getUrl(String directory) {
        return bucket + "/" + directory;
    }

    private String getFullPath(String directory, String fileName) {
        return amazonS3.getUrl(bucket, directory+"/"+fileName).toString();
    }

    private String storeFileName(MultipartFile file) {
        if (file.isEmpty()) return null;

        String originalFilename = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        int pos = originalFilename.lastIndexOf(".");
        String ext = originalFilename.substring(pos + 1);

        List<String> allowedExtensions = Arrays.asList("jpg", "png", "gif", "jpeg", "PNG", "JPG","JPEG");

        if (!allowedExtensions.contains(ext)) {
            throw new IllegalArgumentException("지원하지 않는 포맷입니다.");
        }
        return uuid + "." + ext;

    }
    @Transactional
    public void deleteFile(String fileName) {
        String deleteFile = fileName.substring(fileName.indexOf("/", 10)+1);
        try {
            amazonS3.deleteObject(bucket, deleteFile);
        } catch (Exception e) {
            throw new CustomWebException(e.getMessage());
        }
    }
}