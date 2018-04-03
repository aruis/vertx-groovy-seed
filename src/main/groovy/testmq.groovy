import io.vertx.core.Vertx
import io.vertx.rabbitmq.RabbitMQClient

def vertx = Vertx.vertx()


def client = RabbitMQClient.create(vertx, [:])

def message = [
        body: "Hello RabbitMQ, from Vert.x !"
]



client.start({ res ->
    if (res.succeeded()) {
        10.times {
            client.basicGet("my.queue", true, { getResult ->
                if (getResult.succeeded()) {
                    def msg = getResult.result()
                    println("Got message: ${msg?.body}")
                } else {
                    getResult.cause().printStackTrace()
                }
            })
        }


        10.times {
            client.basicPublish("", "my.queue", message, { pubResult ->
                if (pubResult.succeeded()) {
                    println("Message published !")
                } else {
                    pubResult.cause().printStackTrace()
                }
            })
        }

    }


})


