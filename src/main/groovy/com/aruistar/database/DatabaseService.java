package com.aruistar.database;

import com.aruistar.entity.User;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ProxyGen
public interface DatabaseService {

    @Fluent
    DatabaseService hello(int id, Handler<AsyncResult<User>> resultHandler);

    @Fluent
    DatabaseService list(Handler<AsyncResult<JsonArray>> resultHandler);


    @Fluent
    DatabaseService addUser(User user, Handler<AsyncResult<Boolean>> resultHandler);


    static DatabaseService create(Vertx vertx, JsonObject dbConfig, Handler<AsyncResult<DatabaseService>> readyHandler) {
        return new DatabaseServiceImpl(vertx, dbConfig, readyHandler);
    }

    static DatabaseService createProxy(Vertx vertx, String address) {
        return new DatabaseServiceVertxEBProxy(vertx, address);
    }
}
