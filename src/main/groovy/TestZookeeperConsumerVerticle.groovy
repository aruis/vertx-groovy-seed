import groovy.util.logging.Slf4j
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.core.json.JsonObject
import io.vertx.core.spi.cluster.ClusterManager
import io.vertx.spi.cluster.zookeeper.ZookeeperClusterManager


@Slf4j
class TestZookeeperConsumerVerticle extends AbstractVerticle {

    static void main(String[] args) {
        JsonObject zkConfig = new JsonObject();
        zkConfig.put("zookeeperHosts", "192.168.0.88:2181,192.168.0.88:2182,192.168.0.88:2183");
        zkConfig.put("rootPath", "io.vertx");
        zkConfig.put("retry", new JsonObject()
                .put("initialSleepTime", 3000)
                .put("maxTimes", 3));

        ClusterManager mgr = new ZookeeperClusterManager(zkConfig);
        VertxOptions options = new VertxOptions().setClusterManager(mgr);
        Vertx.clusteredVertx(options, { res ->
            if (res.succeeded()) {
                log.info("connect zookeeper success.")
                Vertx vertx = res.result()
                vertx.deployVerticle(TestZookeeperConsumerVerticle.newInstance())
            } else {
                log.error("connect zookeeper error.")
            }
        });
    }

    @Override
    void start(Future<Void> startFuture) throws Exception {
        def eb = vertx.eventBus()

        eb.consumer("news.uk.sport", { message ->
            println("I have received a message: ${message.body()}")
            message.reply("hello , ${message.body()}")
        })
    }
}
