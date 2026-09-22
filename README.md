# task_manager

Gerenciador de Tarefas feito em Java durante o curso de Java proposto pelo IREDE.

Aplicação desktop em JavaFX para cadastrar, editar, concluir e remover tarefas,
com persistência em SQLite.

## Funcionalidades

- Tela principal com a lista de tarefas, filtro (todas, pendentes, concluídas)
  e ordenação (pendentes primeiro, título A-Z)
- Tela de cadastro e edição de tarefas (duplo clique na tarefa também edita)
- Confirmação antes de remover (tecla Delete também remove)
- Títulos duplicados são bloqueados, sem diferenciar maiúsculas de minúsculas

## Requisitos

- JDK 17 ou superior

Não é preciso instalar o Maven: o projeto traz o Maven Wrapper (`mvnw`), que
baixa a versão certa na primeira execução.

## Build

```bash
./mvnw clean package
```

No Windows (PowerShell ou CMD), use `mvnw.cmd clean package`.

Gera `target/task-manager.jar`, já com JavaFX e o driver do SQLite incluídos.
O jar gerado contém as bibliotecas nativas do JavaFX do sistema em que foi
compilado (Windows, Linux ou macOS).

## Execução

```bash
java -jar target/task-manager.jar
```

No IntelliJ, execute a classe `com.br.taskmanager.app.Main`.

O banco de dados é criado automaticamente em `~/.taskmanager/tarefas.db`
(no Windows, `C:\Users\<usuário>\.taskmanager\tarefas.db`). Para usar outro
arquivo:

```bash
java -Dtaskmanager.db=caminho/para/tarefas.db -jar target/task-manager.jar
```

## Testes

```bash
./mvnw test
```

Os testes ficam em `test/`, separados de `src/`, e cobrem o modelo, os filtros
e ordenações, os métodos genéricos, as regras do service (com um DAO em
memória) e o DAO contra um SQLite temporário, sem tocar no banco real.

## Estrutura

```
src/com/br/taskmanager/
├── app/          Main (ponto de entrada) e TaskManagerApp (Application do JavaFX)
├── views/        Telas em FXML, estilo.css e o desenho de cada item da lista
├── controllers/  Controllers das telas FXML
├── services/     TarefaService: regras de negócio
├── dao/          GenericDao<T, ID> e TarefaDao: acesso ao banco
├── database/     ConnectionFactory e DatabaseInitializer
├── models/       Tarefa, FiltroTarefa e OrdenacaoTarefa
├── utils/        ListaUtils: métodos genéricos com ?, ? extends T e ? super T
└── exceptions/   Exceções da aplicação

test/com/br/taskmanager/   Testes JUnit 5, na mesma estrutura de pacotes
```

As telas (`views/*.fxml`) podem ser abertas e editadas no Scene Builder.

## Generics no projeto

| Recurso | Onde |
|---|---|
| Interface genérica com dois parâmetros | `GenericDao<T, ID>` |
| Métodos genéricos | `ListaUtils.filtrar` e `ListaUtils.contar` |
| `?` | `ListaUtils.estaVazia(Collection<?>)` e `Telas.aplicarEstilo(Dialog<?>)` |
| `? extends T` | origem em `ListaUtils.filtrar` / `contar` e em `TarefaService.organizar` |
| `? super T` | critério em `ListaUtils`, filtro e ordem em `TarefaService.organizar` |

## Licença

Distribuído sob a licença MIT. Veja [LICENSE](LICENSE).
