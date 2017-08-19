package com.aruistar.database

import com.aruistar.entity.User
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.ext.asyncsql.AsyncSQLClient

class DatabaseServiceImpl implements DatabaseService {
    DatabaseServiceImpl(AsyncSQLClient dbClient, Handler<AsyncResult<DatabaseService>> readyHandler) {
        readyHandler.handle(Future.succeededFuture(this))
    }

    @Override
    DatabaseService hello(int id, Handler<AsyncResult<User>> resultHandler) {
        def user = new User("aruis", 29)
        resultHandler.handle(Future.succeededFuture(user))
        return this
    }
}
