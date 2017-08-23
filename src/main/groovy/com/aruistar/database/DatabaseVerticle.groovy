package com.aruistar.database

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import io.vertx.serviceproxy.ProxyHelper

class DatabaseVerticle extends AbstractVerticle {

    @Override
    void start(Future<Void> startFuture) throws Exception {


        JsonObject dbConfig = new JsonObject([
                url          : "jdbc:hsqldb:mem:db/wiki",
                driver_class : "org.hsqldb.jdbcDriver",
                max_pool_size: 30
        ])

        DatabaseService.create(vertx, dbConfig, { ready ->

            if (ready.succeeded()) {
                ProxyHelper.registerService(DatabaseService.class, vertx, ready.result(), "aruistar.database")

                super.start(startFuture)
            } else {
                startFuture.fail(ready.cause());
            }
        })


    }
}
