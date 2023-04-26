package kr.ac.kumoh.allimi.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public String upload(MultipartFile multipartFile) throws IOException {
        // S3에 저장되는 파일의 이름이 중복되지 않게 UUID로 생성한 랜덤 값과 파일 이름을 연결
        String s3FileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

        // 파일의 사이즈를 ContentLength로 S3에 알려주기 위해서 ObjectMetadata 사용
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(multipartFile.getContentType()); // 타입 지정 안하면 이미지 링크가 바로 다운받아짐
        objMeta.setContentLength(multipartFile.getInputStream().available());

        // S3 API 메소드인 putObject를 이용하여 파일 Stream을 열어서 S3에 파일을 업로드
        amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

        // getUrl 메소드를 통해서 S3에 업로드된 사진 URI을 가져오기
        return amazonS3.getUrl(bucket, s3FileName).toString();
    }

    public void delete(String imageUrl) {
        amazonS3.deleteObject(bucket, imageUrl);
    }
}
