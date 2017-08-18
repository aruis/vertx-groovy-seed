package com.aruistar.http

import com.aruistar.database.DatabaseService
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future

class HttpVerticle extends AbstractVerticle {

    DatabaseService dbService

    @Override
    void start(Future<Void> startFuture) throws Exception {
        dbService = DatabaseService.createProxy(vertx, "aruistar.database")

        dbService.hello(1, { res ->
            println(res.result())
        })


        super.start(startFuture)
    }
}
