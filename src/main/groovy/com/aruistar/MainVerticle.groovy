package com.aruistar

import com.aruistar.database.DatabaseService
import com.aruistar.http.HttpVerticle
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.serviceproxy.ProxyHelper

class MainVerticle extends AbstractVerticle {

    static void main(String[] args) {

        Vertx.vertx().deployVerticle(MainVerticle.newInstance())

    }

    @Override
    void start(Future<Void> startFuture) throws Exception {

        DatabaseService.create(null, { ready ->

            if (ready.succeeded()) {
                ProxyHelper.registerService(DatabaseService.class, vertx, ready.result(), "aruistar.database"); // <1>

                vertx.deployVerticle(HttpVerticle.newInstance(), { res ->
                    if (res.succeeded()) {
                        super.start(startFuture)
                    } else {
                        startFuture.fail(res.cause())
                    }

                })

            } else {
                startFuture.fail(ready.cause());
            }
        });


    }
}
