import com.aruistar.tool.FileTool

def tool = new FileTool("http://192.168.0.202:6060/")

tool.uploadFile(
        "/Users/liurui/Desktop/Package.PNG"
).setHandler({ res ->
    if (res.succeeded()) {
        println(res.result())
    } else {
        println(res.cause())
    }

})