package com.aruistar.database

import com.aruistar.entity.User
import com.aruistar.other.AruisLog
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.jdbc.JDBCClient
import io.vertx.ext.sql.SQLClient
import io.vertx.ext.sql.SQLConnection

class DatabaseServiceImpl implements DatabaseService, AruisLog {

    SQLClient dbClient
    Vertx vertx

    DatabaseServiceImpl(Vertx vertx, JsonObject dbConfig, Handler<AsyncResult<DatabaseService>> readyHandler) {


        this.vertx = vertx
        dbClient = JDBCClient.createShared(vertx, dbConfig)


        dbClient.getConnection({ res ->
            if (res.succeeded()) {
                SQLConnection connection = res.result()
                connection.execute("create table if not exists User (id integer identity primary key, name varchar(80) , age integer )", { create ->
                    connection.close();
                    if (create.failed()) {
                        log.error("Database preparation error", create.cause());
                        readyHandler.handle(Future.failedFuture(create.cause()));
                    } else {
                        readyHandler.handle(Future.succeededFuture(this));
                    }
                })

            }

        })


    }

    @Override
    DatabaseService hello(int id, Handler<AsyncResult<User>> resultHandler) {

        def user = new User("aruis", 29)
        resultHandler.handle(Future.succeededFuture(user))
        return this
    }

    //TODO  优化connection的获取方法增加断路器
    @Override
    DatabaseService list(Handler<AsyncResult<JsonArray>> resultHandler) {

        dbClient.query("select * from user", {
            resultHandler.handle(Future.succeededFuture(new JsonArray(it.result().rows)))
        })

        return this
    }

    @Override
    DatabaseService addUser(User user, Handler<AsyncResult<Boolean>> resultHandler) {
        dbClient.updateWithParams("insert into user values(NULL,?,?)", [user.name, user.age], {
            resultHandler.handle(Future.succeededFuture(it.succeeded()))
        })

        return this
    }

}
