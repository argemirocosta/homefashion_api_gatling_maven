package computerdatabase


import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RecordedSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("https://computer-database.gatling.io")
    .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
    .acceptEncodingHeader("gzip, deflate")
    .acceptLanguageHeader("pt-BR,pt;q=0.8,en-US;q=0.5,en;q=0.3")
    .upgradeInsecureRequestsHeader("1")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.15; rv:81.0) Gecko/20100101 Firefox/81.0")

  object Search {
    val search = exec(http("Carregar página principal")
      .get("/computers"))
      .pause(5)
      // Search
      .exec(http("Procurar computador")
        .get("/computers?f=macbook"))
      .pause(3)
      // select a computer
      .exec(http("Selecionar computador")
        .get("/computers/6"))
      .pause(5)
  }

  object Browse {
    val browse = {
			repeat(5, "i") {
				exec(http("Página principal_${i}")
					.get("/computers"))
					.pause(5)
					// create Computer
					.exec(http("Opção para criar computador")
						.get("/computers/new"))
					.pause(5)
			}
    }
  }

  object Create {
    val create = exec(http("Criar computador")
      .post("/computers")
      .formParam("name", "Macbook 2")
      .formParam("introduced", "2020-12-13")
      .formParam("discontinued", "2021-12-13")
      .formParam("company", "1"))
  }

  val admins = scenario("Admins").exec(Search.search, Browse.browse, Create.create)

	val users = scenario("Users").exec(Search.search, Browse.browse)


  setUp(admins.inject(atOnceUsers(1)),
		users.inject(atOnceUsers(1))).protocols(httpProtocol)

}