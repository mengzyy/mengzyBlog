package com.mzy.blog;

import com.mzy.blog.utils.fastDfsFileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    fastDfsFileUtil fastDfsFileUtil;
    @Test
    public void test() {
        File file = new File("C:\\Users\\18306\\Pictures\\Saved Pictures\\2.jpg");
        try {
            String s = fastDfsFileUtil.uploadFile(file);
            System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    void contextLoads() {
    }

}
