package HomeFashion_api_Gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class HomeFashionAPIPostClientes extends Simulation {


  val httpProtocol = http
    //Definindo URL base
    .baseUrl("http://localhost:8080")
    //sem cache
    .disableCaching

  //Definindo variáveis login e senha para serem usados nas chamadas
  val login = "usuario1"
  val senha = "senha1"

  //Definindo varivável do arquivo csv com a massa de dados, com a estratégia circular, que lê em sequencia e recomeça caso acabem as linhas
  val massaDeDados = csv("../resources/clientesparacadastro.csv").circular

  //Criando objeto ListaTodosOsClientes
  object CadastrarCliente {
    val cadastrarCliente = exec(
      http("Cadastrar cliente")
        .post("/cliente")
        .body(StringBody("{\n\t\"nome\": \"${nome}\",\n\t\"cpf\": \"${cpf}\",\n\t\"usuario\": {\n\t\t\"id\": 155\n\t}\n}")).asJson
        .basicAuth(login, senha))
  }

  //Definindo cenários de teste, e chamando a massa de dados pelo feed
  val scnCadastrarCliente = scenario("scnCadastrarCliente").feed(massaDeDados).exec(CadastrarCliente.cadastrarCliente)
    .exec { session => println(session); session }

  //Adicionando a carga do teste
  setUp(
    scnCadastrarCliente.inject(atOnceUsers(10)).protocols(httpProtocol)
  )

}