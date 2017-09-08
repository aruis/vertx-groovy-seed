import com.aruistar.tool.FileTool;

import java.io.File;

public class DownloadTest {
    public static void main(String[] args) {

        FileTool tool = new FileTool("http://127.0.0.1:6060");

        File file = tool.downloadFile(
                "742f55c4-4618-4154-8d18-5e49ddc0e193"
        );

        System.out.println(file.length());
    }
}
