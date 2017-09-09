package com.aruistar.tool

import com.aruistar.other.AruisLog
import groovy.json.JsonSlurper
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.client.WebClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients

class FileTool implements AruisLog {

    private Vertx vertx
    String host = "http://127.0.0.1:6060"

    FileTool(String host) {
        if (host.lastIndexOf("/") == host.length() - 1)
            host = host.substring(0, host.length() - 1)

        this.host = host
        this.vertx = Vertx.vertx()
    }


    FileTool(Vertx vertx, String host) {
        this.vertx = vertx
        this.host = host
    }

    File downloadFile(String fileuuid, String filePath = "") {

        def filename = new URL("$host/info/$fileuuid").text
        if (filename == "-1") {
            return null
        }

        if (filePath != "") {
            filePath += "/"
            File _f = new File(filePath)
            if (!_f.exists()) _f.mkdir()
        }


        File _f = new File("$filePath$fileuuid/")
        if (!_f.exists()) _f.mkdir()


        def file = new File("$filePath$fileuuid/$filename")
        file.withOutputStream { out ->
//            out << new URL("$url/$filePath").openStream()
            new URL("$host/download/$fileuuid").withInputStream { from -> out << from; }
        }

        return file
    }

    HashMap uploadFile(File file) {

        HttpPost post = new HttpPost("$host/upload");
        MultipartEntityBuilder meb = MultipartEntityBuilder.create()

        meb.addBinaryBody(file.name, file)

        post.setEntity(meb.build())

        CloseableHttpClient httpClient = HttpClients.createDefault()

        def response = httpClient.execute(post)
        def json = response.getEntity().getContent().getText()
        return new JsonSlurper().parseText(json)
    }

    Future asyncUploadFile(String filePath) {

        Future future = Future.future()
        def client = WebClient.create(vertx)
        def post = client.postAbs("$host/upload").timeout(10 * 60 * 1000)

        def buffer = Buffer.buffer()
        String contentType = "application/octet-stream";
        String boundary = UUID.randomUUID().toString()
        String filename = new File(filePath).name
        String header =
                """--$boundary
Content-Disposition: form-data; name="$filename"; filename="$filename"
Content-Type: $contentType
Content-Transfer-Encoding: binary

"""


        buffer.appendString(header.toString())

        vertx.fileSystem().readFile(filePath, { res ->
            if (res.succeeded()) {
                buffer.appendBuffer(res.result())
                String footer = "\r\n--$boundary--\r\n";
                buffer.appendString(footer);

                post.putHeader("content-length", String.valueOf(buffer.length()))
                post.putHeader("content-type", "multipart/form-data; boundary=" + boundary)
                post.sendBuffer(buffer, { ar ->
                    if (ar.succeeded()) {
                        // Obtain response
                        def response = ar.result()
                        def body = response.bodyAsString()
                        def json = new JsonSlurper().parseText(body)
                        log.info("body:" + body)
                        future.complete(json)
                    } else {
                        log.info(ar.cause().message)
                        future.fail('upload fail')
                    }
                    client.close()
                })


            } else {

            }

        })





        return future
    }
}
