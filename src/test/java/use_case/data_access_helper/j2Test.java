package use_case.data_access_helper;



import data_access.JsonUserDataAccessObject;
import entity.CommonUser;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class j2Test {

    @Test
    void update_putsUserAndPersists() throws IOException {
        // 1. 准备一个临时文件路径
        Path tempFile = Files.createTempFile("users", ".json");

        // 2. 构造 DAO（会 new File(tempFile) 并创建它）
        JsonUserDataAccessObject dao = new JsonUserDataAccessObject(tempFile.toString());

        // 3. 调用 update 分支
        CommonUser newUser = new CommonUser("alice","1");
        dao.update("alice", newUser);

        // 4. 验证 users.put 生效
        assertTrue(dao.existsByName("alice"));
        assertEquals("COMMON", dao.getType("alice"));

        // 5. 验证持久化至少写出了文件，文件非空
        String content = Files.readString(tempFile);
        assertTrue(content.contains("alice"));
    }
}
