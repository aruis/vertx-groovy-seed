import com.aruistar.tool.FileTool;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

public class UploadTest {
    public static void main(String[] args) {
        FileTool tool = new FileTool("http://127.0.0.1:6060/");

        Future<JsonArray> fu = tool.uploadFile(
                "/Users/liurui/Desktop/Package.PNG"
        );

        fu.setHandler(res -> {
            if (res.succeeded()) {
                System.out.println(res.result().toString());
            } else {
                System.out.println(res.cause().getMessage());
            }
        });

//                .setHandler(res -> {
//            if (res.succeeded()) {
//                println(res.result());
//            } else {
//                println(res.cause());
//            }
//
//        })
    }
}
