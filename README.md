# task_manager

Gerenciador de Tarefas feito em Java durante o curso de Java proposto pelo IREDE.

Aplicação de console que permite cadastrar tarefas, listá-las, marcá-las como
concluídas e removê-las, com os dados mantidos em memória durante a execução.

## Requisitos

- JDK 17 ou superior (o projeto usa *text blocks*)

## Como compilar e executar

Pela linha de comando, na raiz do projeto:

```bash
javac -d bin $(find src -name "*.java")
java -cp bin com.br.taskmanager.app.App
```

No Windows (PowerShell):

```powershell
javac -d bin (Get-ChildItem -Recurse -Filter *.java src).FullName
java -cp bin com.br.taskmanager.app.App
```

No Eclipse, basta importar a pasta como projeto existente e executar a classe
`App`.

## Estrutura

```
src/com/br/taskmanager/
├── app/App.java                      # menu e interação com o usuário
├── controllers/TaskManager.java      # regras de negócio da lista de tarefas
├── exceptions/ListaVaziaException.java
└── models/Tarefa.java                # entidade Tarefa
```

## Licença

Distribuído sob a licença MIT. Veja [LICENSE](LICENSE).
