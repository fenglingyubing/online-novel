package com.fengling;

import com.fengling.common.util.OSSUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
class OnlineNovelApplicationTests {

    @Autowired
    private OSSUtil ossUtil;

    @Test
    void contextLoads() {
    }

    @Test
    void uploadPhoto() throws IOException {
        Path path = Path.of("C:\\Users\\风铃\\Pictures\\Saved Pictures\\fengling.jpeg");
        try (InputStream inputStream = Files.newInputStream(path)){
            MockMultipartFile file = new MockMultipartFile(
                    "file",
                    "fengling.jpeg",
                    "image/jpeg",
                    inputStream
            );

            String url = ossUtil.upload(file, "upload");
            System.out.println(url);
        }
    }

}
