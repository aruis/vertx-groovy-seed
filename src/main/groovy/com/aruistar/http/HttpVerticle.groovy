package com.aruistar.http

import com.aruistar.database.DatabaseService
import com.aruistar.entity.User
import com.aruistar.other.AruisLog
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router

class HttpVerticle extends AbstractVerticle implements AruisLog {

    DatabaseService dbService

    @Override
    void start(Future<Void> startFuture) throws Exception {
        dbService = DatabaseService.createProxy(vertx, "aruistar.database")

        int port = config().getInteger("port", 8080)

        HttpServer server = vertx.createHttpServer()

        Router router = Router.router(vertx)

        router.get("/").handler({ context ->

            dbService.list({ res ->
                context.response().end(res.result().toString())
//                println(res.result())
            })


        })

        router.get("/add").handler({ context ->

            dbService.addUser(new User("hello", 1), { res ->
                context.response().end(res.result().toString())
            })

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
}
