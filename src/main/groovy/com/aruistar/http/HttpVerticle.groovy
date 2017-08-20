package com.aruistar.http

import com.aruistar.database.DatabaseService
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router

class HttpVerticle extends AbstractVerticle {

    DatabaseService dbService

    @Override
    void start(Future<Void> startFuture) throws Exception {
        dbService = DatabaseService.createProxy(vertx, "aruistar.database")

        HttpServer server = vertx.createHttpServer()

        Router router = Router.router(vertx)

        router.get("/").handler({ context ->

            dbService.hello(1, { res ->
                context.response().end(res.result().toString())
//                println(res.result())
            })


        })


        server.requestHandler(router.&accept)
                .listen(8080, { ar ->
            if (ar.succeeded()) {
                startFuture.complete()
            } else {
                startFuture.fail(ar.cause())
            }

        })





//        super.start(startFuture)
    }
}
