package com.aruistar;

import io.vertx.core.Launcher;
import io.vertx.core.VertxOptions;

public class AruisLauncher extends Launcher {

    public static void main(String[] args) {
        new AruisLauncher().dispatch(args);
    }

    @Override
    public void beforeStartingVertx(VertxOptions options) {
        super.beforeStartingVertx(options);
    }
}
