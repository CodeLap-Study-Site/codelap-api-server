package com.codelap.api.service.s3upload;

import com.codelap.api.support.AwsS3MockConfig;
import com.codelap.common.user.domain.UserFile;
import com.codelap.integration.s3.FileUpload;
import io.findify.s3mock.S3Mock;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static com.codelap.common.user.domain.UserFile.create;
import static org.assertj.core.api.Assertions.assertThat;

@Import(AwsS3MockConfig.class)
@SpringBootTest
@Transactional
public class AwsS3UploadTest {

    @Autowired
    protected S3Mock s3Mock;

    @Autowired
    protected FileUpload FileUpload;

    @AfterEach
    public void tearDown() {
        s3Mock.stop();
    }

    @Test
    void S3_파일_업로드() throws IOException {
        String path = "test.png";
        String contentType = "image/png";
        String dirName = "test";

        MockMultipartFile file = new MockMultipartFile("test", path, contentType, "test".getBytes());

        UserFile urlPath = (UserFile) FileUpload.upload(file, dirName, create());

        assertThat(urlPath.getOriginalName()).isNotNull();
        assertThat(urlPath.getSavedName()).isNotNull();
        assertThat(urlPath.getSize()).isNotNull();
    }
}
