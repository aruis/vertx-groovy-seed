package com.aruistar.database;

import com.aruistar.entity.User;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.asyncsql.AsyncSQLClient;

@ProxyGen
public interface DatabaseService {

    @Fluent
    DatabaseService hello(int id, Handler<AsyncResult<User>> resultHandler);

    // (...)
    // end::interface[]

    // tag::create[]
    static DatabaseService create(AsyncSQLClient dbClient, Handler<AsyncResult<DatabaseService>> readyHandler) {
        return new DatabaseServiceImpl(dbClient, readyHandler);
    }
    // end::create[]

    // tag::proxy[]
    static DatabaseService createProxy(Vertx vertx, String address) {
        return new DatabaseServiceVertxEBProxy(vertx, address);
    }
    // end::proxy[]
}
