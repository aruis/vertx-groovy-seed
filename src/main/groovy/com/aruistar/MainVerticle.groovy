package com.aruistar

import com.aruistar.database.DatabaseVerticle
import com.aruistar.http.HttpVerticle
import io.vertx.core.AbstractVerticle
import io.vertx.core.DeploymentOptions
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject

class MainVerticle extends AbstractVerticle {

    static void main(String[] args) {

        Vertx.vertx().deployVerticle(MainVerticle.newInstance())

    }

    @Override
    void start(Future<Void> startFuture) throws Exception {


        def did

        vertx.deployVerticle(DatabaseVerticle.newInstance(), { dbHandler ->
            if (dbHandler.succeeded()) {

                did = dbHandler.result()
                println(did)
                vertx.deployVerticle(HttpVerticle.newInstance(), new DeploymentOptions().setConfig(new JsonObject([port: 8080])), { httpHandler ->
                    if (httpHandler.succeeded()) {
                        startFuture.complete()
                    }

                })


            }

        })

        vertx.setTimer(10 * 1000, {
            vertx.undeploy(did, {

            })
        })

        vertx.setTimer(15 * 1000, {
            vertx.deployVerticle(DatabaseVerticle.newInstance())
        })

    }
}
