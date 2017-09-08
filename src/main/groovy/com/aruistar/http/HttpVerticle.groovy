package com.aruistar.http

import com.aruistar.other.AruisLog
import groovy.json.JsonBuilder
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServer
import io.vertx.core.json.JsonArray
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.CorsHandler

class HttpVerticle extends AbstractVerticle implements AruisLog {


    String fileUploadsPath

    static whiteHosts = [
            "127.0.0.1"
    ]

    @Override
    void start(Future<Void> startFuture) throws Exception {
        int port = config().getInteger("port", 6060)
        fileUploadsPath = config().getString("file-upload", "file-uploads")
        config().getJsonArray("white-list", new JsonArray([])).each {
            whiteHosts << it
        }

        HttpServer server = vertx.createHttpServer()

        Router router = Router.router(vertx)

        router.route().handler({ routingContext ->
            if (routingContext.request().remoteAddress().host() in whiteHosts) {
                routingContext.next()
            } else {
                routingContext.response().end("-1")
            }

        })
        router.route().handler(CorsHandler.create("*").allowedMethod(HttpMethod.GET).allowedMethod(HttpMethod.POST).allowedMethod(HttpMethod.OPTIONS).allowedHeader('Content-Type'))
        router.route().handler(BodyHandler.create().setDeleteUploadedFilesOnEnd(true))

        router.post("/upload").handler({ routingContext ->

            def uploads = routingContext.fileUploads()
            def list = []
            uploads.each { fu ->
                def fullPath = fu.uploadedFileName()
                def fileName = fu.fileName()
                def postfix
                def uuid = UUID.randomUUID().toString()


                if (fileName.contains(".")) {
                    postfix = fileName.substring(fileName.lastIndexOf('.') + 1, fileName.size())
                }

                new File(fullPath).renameTo("$fileUploadsPath/$uuid.$fileName")

                list << [
                        uuid    : uuid,
                        fileName: fileName,
                        postfix : postfix
                ]
            }

            routingContext.response().putHeader('content-type', 'application/json').end(new JsonBuilder(list).toString())
        })

        router.get("/download/:uuid").handler({ routingContext ->
            def uuid = routingContext.request().getParam("uuid")

            File file = catchFile(uuid)
            if (file) {
                def name = file.name
                log.info("response file : $name")
                routingContext.response().putHeader("file-name", name.substring(name.indexOf(".") + 1))
                routingContext.response().sendFile("$fileUploadsPath/${name}")
            } else {
                routingContext.response().end("-1")
            }


        })

        router.get("/info/:uuid").handler({ routingContext ->
            def uuid = routingContext.request().getParam("uuid")

            File file = catchFile(uuid)
            if (file) {
                def name = file.name
                log.info("info file : $name")
                routingContext.response()
                        .putHeader('content-type', 'plain/text')
                        .end(name.substring(name.indexOf(".") + 1))
            } else {
                routingContext.response().end("-1")
            }

        })

        router.get("/del/:uuid").handler({ routingContext ->
            def uuid = routingContext.request().getParam("uuid")

            File file = catchFile(uuid)
            if (file) {
                def name = file.name
                file.delete()
                log.info("del file : $name")
                routingContext.response()
                        .end("ok")
            } else {
                routingContext.response().end("-1")
            }

        })


        server.requestHandler(router.&accept)
                .listen(port, { ar ->
            if (ar.succeeded()) {
                log.info("HTTP server running on port " + port)
                startFuture.complete()
            } else {
                log.error("Could not start a HTTP server", ar.cause())
                startFuture.fail(ar.cause())
            }

        })
    }

    File catchFile(String uuid) {
        def dir = new File("$fileUploadsPath/")
        File file
        dir.eachFileMatch(~/$uuid.*/) { file = it }
        return file
    }
}
