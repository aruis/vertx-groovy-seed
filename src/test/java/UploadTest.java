import com.aruistar.tool.FileTool;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

import java.io.File;
import java.util.HashMap;

public class UploadTest {
    static FileTool tool = new FileTool("http://127.0.0.1:6060/");

    public static void main(String[] args) {
        uploadFile();
    }


    static void uploadFile() {
        HashMap result = tool.uploadFile(
                new File("/Users/liurui/Desktop/Package.PNG")
        );


        System.out.println(result.get("uuid"));
    }


    static void asyncUploadFile() {
        Future<JsonArray> fu = tool.asyncUploadFile(
                "/Users/liurui/Desktop/Package.PNG"
        );

        fu.setHandler(res -> {
            if (res.succeeded()) {
                System.out.println(res.result().toString());
            } else {
                System.out.println(res.cause().getMessage());
            }
        });

    }
}
