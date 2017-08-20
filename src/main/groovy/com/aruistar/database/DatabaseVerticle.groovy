package com.aruistar.database

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.serviceproxy.ProxyHelper

class DatabaseVerticle extends AbstractVerticle {
    @Override
    void start(Future<Void> startFuture) throws Exception {

        DatabaseService.create(null, { ready ->

            if (ready.succeeded()) {
                ProxyHelper.registerService(DatabaseService.class, vertx, ready.result(), "aruistar.database")

                super.start(startFuture)
            } else {
                startFuture.fail(ready.cause());
            }
        })



    }
}
