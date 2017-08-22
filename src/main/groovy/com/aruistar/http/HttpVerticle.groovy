package com.aruistar.http

import com.aruistar.database.DatabaseService
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class HttpVerticle extends AbstractVerticle {

    DatabaseService dbService
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpVerticle.class);

    @Override
    void start(Future<Void> startFuture) throws Exception {
        dbService = DatabaseService.createProxy(vertx, "aruistar.database")

        int port = config().getValue("port")

        HttpServer server = vertx.createHttpServer()

        Router router = Router.router(vertx)

        router.get("/").handler({ context ->

            dbService.hello(1, { res ->
                context.response().end(res.result().toString())
//                println(res.result())
            })


        })


        server.requestHandler(router.&accept)
                .listen(port, { ar ->
            if (ar.succeeded()) {
                LOGGER.info("HTTP server running on port " + port)
                startFuture.complete()
            } else {
                LOGGER.error("Could not start a HTTP server", ar.cause())
                startFuture.fail(ar.cause())
            }

        })

//        super.start(startFuture)
    }
}
