package com.aruistar

import com.aruistar.database.DatabaseVerticle
import com.aruistar.http.HttpVerticle
import com.aruistar.other.AruisLog
import io.vertx.core.*

class MainVerticle extends AbstractVerticle implements AruisLog {

//    static {
//        Launcher.metaClass.beforeStartingVertx = { options ->
//            log.error("hahahaha")
//        }
//    }

    static void main(String[] args) {

        Vertx.vertx().deployVerticle(MainVerticle.newInstance())

    }

    @Override
    void start(Future<Void> startFuture) throws Exception {

        log.info("config:" + config().toString())

        vertx.deployVerticle(DatabaseVerticle.newInstance(), { dbHandler ->
            if (dbHandler.succeeded()) {

                vertx.deployVerticle(HttpVerticle.newInstance(), new DeploymentOptions().setConfig(config()), { httpHandler ->
                    if (httpHandler.succeeded()) {
                        startFuture.complete()
                    }

                })


            }

        })


    }
}
