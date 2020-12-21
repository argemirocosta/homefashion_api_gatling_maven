package HomeFashion_api_Gatling

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class  HomeFashionAPIGetClientes extends Simulation {


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

  //Criando objeto ListaTodosOsClientes
  object ListaTodosOsClientes {
    val listaTodosOsClientes = exec(
      http("Lista todos os clientes")
        .get("/cliente")
        .basicAuth(login, senha))
  }

  //Criando objeto ListarUmCliente
  object ListarUmCliente {
    val listarUmCliente = exec(
      http("Buscar cliente específico")
        .get("/cliente/234")
        .basicAuth(login, senha))
  }

  //Definindo cenários de teste
  val scnTodosOsClientes = scenario("scnTodosOsClientes").exec(ListaTodosOsClientes.listaTodosOsClientes)

  val scnUmCliente = scenario("scnUmCliente").exec(ListarUmCliente.listarUmCliente)

  //Adicionando a carga do teste
  setUp(
    scnTodosOsClientes.inject(atOnceUsers(10)).protocols(httpProtocol),
    scnUmCliente.inject(atOnceUsers(20)).protocols(httpProtocol)
    )

}