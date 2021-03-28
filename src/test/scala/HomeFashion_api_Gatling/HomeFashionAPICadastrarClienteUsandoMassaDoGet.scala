package HomeFashion_api_Gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class HomeFashionAPICadastrarClienteUsandoMassaDoGet extends Simulation {

  val httpProtocol = http
    //Definindo URL base
    .baseUrl("http://localhost:8080")
    //sem cache
    .disableCaching
  //Se não ver relatórios no terminal
  //.silentResources

  //Definindo variáveis login e senha para serem usados nas chamadas
  val login = "usuario1"
  val senha = "senha1"

  //Criando objeto ListarUmCliente
  object CadastrarCliente {
    val listarUmCliente = exec(
      http("Buscar cliente específico")
        .get("/cliente/234")
        .basicAuth(login, senha)
        .check(status.is(200))
        .check(jsonPath("$.cpf").is("90451718054"))
        .check(jsonPath("$.cpf").saveAs("cpf"))
        .check(jsonPath("$.nome").saveAs("nome"))
        .check(bodyString.saveAs("responseBodyGet"))
    )
      .exec { session => println(session("responseBodyGet").as[String]); session }

    val cadastrarCliente = exec(
      http("Cadastrar cliente")
        .post("/cliente")
        .body(StringBody("{\n\t\"nome\": \"${nome}\",\n\t\"cpf\": \"${cpf}\",\n\t\"usuario\": {\n\t\t\"id\": 155\n\t}\n}")).asJson
        .basicAuth(login, senha))
  }

  //Definindo cenários de teste
  val scnCadastrarCliente = scenario("scnCadastrarCliente").exec(CadastrarCliente.listarUmCliente, CadastrarCliente.cadastrarCliente)

  //Adicionando a carga do teste
  setUp(
    scnCadastrarCliente.inject(atOnceUsers(10)).protocols(httpProtocol)
  )

}