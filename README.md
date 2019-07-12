# mapping-requirements

Projeto de iniciação científica

### Dependências do projeto

- JDK 1.7 ou superior
- Apache Maven 3 ou superior

### Utilizando o projeto

Primeiramente é necessário construir a aplicação, e para isso basta executar o seguinte comando:

```mvn clean compile package```

Este comando irá limpar e compilar o projeto, e ao final serão gerado dois arquivos ```.jar```, onde um destes contém apenas o código compilado do projeto sem a dependência do [`Gson`](https://github.com/google/gson), que foi utilizado para fazer a conversão dos objetos Java par JSON, enquanto que o segundo ```.jar``` contém a dependência do ```Gson```, e este tem o sufixo ```jar-with-dependencies```. É recomendável utilizar o ```.jar``` que contém todas as dependências para não haver problema com ```classpath```.

Após isso, basta compilar suas classes adicionando o ```.jar``` ao classpath. Exemplo:

```java
package com.example;

import mappingrequirements.annotation.CustomTypeAnnotation;

@CustomTypeAnnotation public class Main {

    public static void main(String[] args) {
        System.out.println("Hello!");
    }

}
```

Execute o comando para compilar a classe adicionando o ```.jar``` ao ```classpath```:

```javac -cp mapping-requirements-1.0.0-SNAPSHOT-jar-with-dependencies.jar Main.java```

Isso deverá produzir um arquivo ```output.json``` com o seguinte resultado:

```json
[
  {
    "name": "Main",
    "package_name": "com.example",
    "modifiers": [
      "public"
    ],
    "priority": "MEDIUM",
    "created_by": "Gabriel",
    "last_modified": "02/05/2019",
    "tags": [
      ""
    ],
    "variables": [],
    "methods": []
  }
]
```