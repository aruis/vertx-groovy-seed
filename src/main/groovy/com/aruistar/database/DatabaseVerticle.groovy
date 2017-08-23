package com.aruistar.database

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.json.JsonObject
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.serviceproxy.ProxyHelper

class DatabaseVerticle extends AbstractVerticle {

    @Override
    void start(Future<Void> startFuture) throws Exception {

        JDBCClient dbClient = JDBCClient.createShared(vertx, new JsonObject([
                url          : "jdbc:hsqldb:mem:db/wiki",
                driver_class : "org.hsqldb.jdbcDriver",
                max_pool_size: 30
        ]))



        DatabaseService.create(dbClient, { ready ->

            if (ready.succeeded()) {
                ProxyHelper.registerService(DatabaseService.class, vertx, ready.result(), "aruistar.database")

                super.start(startFuture)
            } else {
                startFuture.fail(ready.cause());
            }
        })


    }
}
