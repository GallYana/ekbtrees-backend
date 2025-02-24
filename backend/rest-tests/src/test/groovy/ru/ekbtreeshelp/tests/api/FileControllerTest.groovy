package ru.ekbtreeshelp.tests.api

import org.apache.http.entity.ContentType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.ekbtreeshelp.tests.data.TestContext
import ru.ekbtreeshelp.tests.data.TestUser

import static org.hamcrest.Matchers.*

class FileControllerTest extends ApiTest {

    @BeforeEach
    void initContext() {
        String email = "${UUID.randomUUID()}@test.mail"
        TestUser user = new TestUser(
                email: email,
                password: 'volunteer'
        )

        createUser(user)

        testContext = new TestContext(
                user: user
        )
    }

    @Test
    void testUnauthenticatedUserCantUploadFile() {
        testContext.user = null

        sendUploadFileRequest()
                .then()
                .statusCode(403)
    }

    @Test
    void testUpload() {
        sendUploadFileRequest()
                .then()
                .statusCode(201)
    }

    @Test
    void testGetById() {
        Long newFileId = uploadFile()

        get("/api/file/${ newFileId }")
                .then()
                .statusCode(200)
                .body('id as Long', equalTo(newFileId))
                .body('uri', stringContainsInOrder("/api/file/download"))
    }

    @Test
    void testDeleteById() {
        Long newFileId = uploadFile()

        delete("/api/file/${ newFileId }")
                .then()
                .statusCode(200)
    }

    @Test
    void testGetFilesByTree() {
        Long newTreeId = createTree()

        File file = File.createTempFile('testAttachFile', null)
        file.write('testFileContent')

        post("/api/tree/attachFile/${ newTreeId }", file)
                .then()
                .statusCode(200)

        get("/api/file/byTree/${ newTreeId }")
                .then()
                .statusCode(200)
                .body('find { it.id }', not(null))
    }

    @Test
    void testDownloadFile() {
        Long newFileId = uploadFile()

        get("/api/file/download/${ newFileId }")
                .then()
                .statusCode(200)
                .contentType(ContentType.APPLICATION_OCTET_STREAM.toString())
    }
}
