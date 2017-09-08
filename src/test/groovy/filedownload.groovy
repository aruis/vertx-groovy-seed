import com.aruistar.tool.FileTool

def tool = new FileTool("http://192.168.0.202:6060")

def file = tool.downloadFile(
        "75d22181-9516-4dee-8584-42483bef498d"
)

println(file.length())