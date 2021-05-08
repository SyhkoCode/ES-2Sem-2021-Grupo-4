### Projeto realizado para a UC Engenharia de Software pelo **Grupo 4**:

Nome do aluno | Número do aluno
------------ | -------------
Diogo Graça |	87938
Pedro Pereira	| 88721
Pedro Pinheiro | 88657
Sofia Chaves | 73433
Susana Polido |	87871
Tiago Mendes |	88647

#### **Trello:** https://trello.com/es2sem2021grupo4/

***
# CodeQualityAssessor

![Logo](https://i.imgur.com/QGYPSGx.png)
1. [O que é o CodeQualityAssessor e para que serve?](#what)
2. [O que são Code Smells?](#smells)
3. [Que métricas são usadas?](#metricas)
4. [Funcionalidades Implementadas](#funcionalidades)
5. [Manual do Utilizador](#userguide)
6. [Bibliotecas](#bibliotecas)

##  [O que é o CodeQualityAssessor e para que serve?](#what)
A aplicação **CodeQualityAssessor** tem como objetivo auxiliar as equipas de desenvolvimento de software na
**deteção de defeitos de código**. Estes defeitos de código não são bugs (pois não impedem a execução do software),
mas sim representam violações das boas práticas na implementação do software e dificultam a manutenção e
evolução do software por parte das equipas de desenvolvimento.

##  [O que são Code Smells?](#smells)
Estes defeitos são designados na área de engenharia de software por _code smells_ (cheiros no código).
Para a deteção de _code smells_ é necessário utilizar um conjunto de métricas extraídas sobre o código fonte de um
projeto Java.

##  [Que métricas são usadas?](#metricas)
<table role="table">
<thead>
 <tr>
<th>
Métricas</th>
<th>Descrição</th>
</tr>
</thead>
<tbody>
<tr>
<td><b><code>NOM_class</code></b></td>
<td>Representa o número de métodos por classe, incluindo inner classes. </td>
</tr>
<tr>
<td><b><code>LOC_class</code></b></td>
<td>Representa o número de linhas de código da classe, desde a declaração da classe ao último bracket, incluindo linhas vazias e comentários.</td>
</tr>
 <tr>
<td><b><code>WMC_class</code></b></td>
<td>Representa a complexidade ciclomática da classe: somatório dos <code>CYCLO_method</code> respetivos a todos os métodos da classe. 
</td>
</tr>
 <tr>
<td><b><code>LOC_method</code></b></td>
<td>Representa o número de linhas de código do método, incluindo linhas vazias e comentários. </td>
</tr>
 <tr>
<td><b><code>CYCLO_method</code></b></td>
<td>Representa a complexidade ciclomática do método. Considera que cada método começa com complexidade de 1. Aumenta a complexidade por 1 por cada <code>if</code> , <code>for</code>, <code>while</code>, <code>catch</code>, <code>? :</code>, <code>case</code>, <code>continue</code>, <code>&&</code> e <code>||</code> que aparece no código, ignorando comentários.  </td>
</tr>
</tbody>
</table>

Iremos considerar que, sempre que o valor de
uma métrica ou conjunto de métricas ultrapassar um determinado limite, estamos na presença de um sintoma de
defeito de desenho de software (_code smell_).

##  [Funcionalidades Implementadas](#funcionalidades)
#### 1) _Extração das métricas de código_.
Através da GUI, o utilizador pode indicar a pasta de um projeto Java e é gerado um ficheiro Excel com as métricas extraídas do código do respetivo projeto Java. O nome deste ficheiro Excel gerado é igual ao nome da pasta, indicada pelo utilizador, acrescentando o sufixo “_metrics”.

#### 2) _Importação e visualização das métricas_.
Além de ser gerado o ficheiro Excel no ponto 1, o utilizador poderá visualizar imediatamente na GUI o mesmo, bem como um resumo das características gerais do respetivo projeto Java. Estas características gerais incluem o número total de packages, número total de classes, número total de métodos e o  número total de linhas de código do projeto.

#### 3) _Editor de Regras para deteção de code smells_.
Através da GUI, o utilizador também pode:
* **definir regras e limites (thresholds) para deteção de code smells**, isto é, escolher as métricas a
serem usadas na regra (entre as métricas identificadas no ficheiro Excel do ponto 1), os thresholds das métricas e os operadores lógicos (AND ou OR) que pretende usar para formar expressões lógicas com as métricas.
* **modificar as regras definidas**, nomeadamente os valores limites (thresholds) para as métricas
envolvidas na deteção dos code smells. Esses limites podem ser configurados como parâmetros das
regras, criando assim a possibilidade de as equipas de desenvolvimento de software definirem as
suas próprias regras de deteção de code smells. A aplicação deverá no mínimo suportar a deteção
dos code smells Long Method e God Class.

#### 4) _Guardar as regras definidas pelo utilizador de forma persistente_.
As regras são guardadas em suporte não volátil (disco rígido) para reutilização futura.

#### 5) _Deteção de code smells_.
A aplicação identifica a existência (ou não) dos code smells Long Method e God Class, para cada
método e classe presente no projeto Java. Assim, a deteção de code smells é baseada nas regras e
thresholds definidos no ponto 3, utilizando as métricas extraídas do código (NOM_class, LOC_class,
WMC_class, LOC_method, CYCLO_method). Os resultados da deteção de code smells são apresentados na GUI,
organizados da seguinte forma: 1) para as classes, indicação do nome da classe e da presença ou não
do(s) code smell(s); 2) para os métodos, indicação do MethodID e da presença ou não do(s) code
smell(s).

#### 6) _Avaliação da qualidade de deteção de code smells_.
A aplicação permite visualizar a qualidade da classificação resultante da aplicação de uma regra
escolhida pelo utilizador. São apresentados na GUI, de forma textual e gráfica:
*  os indicadores de acerto: 
* * code smells corretamente identificados ou verdadeiros positivos – VP
* * ausências de code smells corretamente identificadas ou verdadeiros negativos – VN 
* os indicadores de erro:
* * code smells incorretamente identificados ou falsos positivos – FP 
* * ausências de code smells incorretamente identificadas ou falsos negativos - FN 

## [Manual do Utilizador](#userguide)

Para instalar a aplicação, o utilizador pode fazer uma das seguintes maneiras:
* simplesmente clicar no CodeQualityAssessor.jar apresentado. 
* correr na linha de comandos: `java -jar CodeQualityAssessor.jar`. 
* construir a imagem do Dockerfile apresentado (fazendo `docker build -t CodeQualityAssessor/APP .`) e criar o respetivo container (fazendo` docker run --name CodeQualityAssessorAPP -d -it -e DISPLAY=$USERIP CodeQualityAssessor/APP`) onde poderá testar o CodeQualityAssessor.jar num ambiente controlado, fazendo partido de um X11 display server, como por exemplo o Xming ou XLauncher.

O utilizador ao abrir a aplicação depara-se com menu em baixo ilustrado, onde poderá então indicar o projeto Java e onde pretende guardar o ficheiro Excel com as métricas extraídas gerado, de acordo com o ponto 1 das funcionalidades implementadas. Além disso, consegue também visualizar imediatamente o Excel gerado no próprio submenu onde se encontra e o resumo das características no submenu Resumo, cumprindo o ponto 2 das funcionalidades implementadas.

<img src="https://i.imgur.com/mAzfe6z.png" width="591.6" height="415.8"> <img src="https://i.imgur.com/JLBJTd6.png" width="591.6" height="415.8">

<br>
<br>

No menu _Regras_, o utilizador tem acesso a todas as utilidades que um Editor de Regras (como mencionado no ponto 3) oferece. Pode criar regras para cada code smell (**LongMethod** e **GodClass**), tendo a possibilidade de completa personalização: acrescentar múltiplas condições para métrica à escolha dentro dos parâmetros dos code smells, usando operadores lógicos e aritméticos à escolha e definir os limites.  Ao clicar no _icon da Disquete_ pode guardar as regras que criou em simultâneo de ambos os code smells num ficheiro .txt (como indicado no ponto 4). Ao clicar no _icon da Pasta_ pode importar o ficheiro .txt das regras. O _icon do Lixo_ serve para limpar as regras do code smell onde o utilizador se encontra, caso pretenda começar de novo.

<img src="https://i.imgur.com/qksFEIv.png" width="591.6" height="415.8"> 
<br>
<br>
<br>
<br>

No menu _Correr Regras_, o utilizador tem a possibilidade de detetar code smells de acordo com as regras indicadas. Para tal, o utilizador deve indicar o ficheiro Excel com as métricas extraídas gerado no ponto 1 e o ficheiro .txt das regras gerado no ponto 4. Tem a escolha de guardar ou não os resultados da deteção de code smells. De qualquer forma, irá visualizá-los imediamente no próprio menu onde se encontra por **classes** e **métodos** (conforme o ponto 5). Se decidir guardar estes resultados, será gerado o respetivo Ficheiro Excel da Deteção dos Code Smells, com as colunas dos code smells preenchidas de acordo, e guardado na localização escolhida pelo utilizador.

<img src="https://i.imgur.com/oDR3mvg.png" width="591.6" height="415.8"> 
<br>
<br>
<br>
<br>

No menu _Avaliar Regras_, o utilizador poderá então avaliar a qualidade da deteção dos code smells para a sua regra escolhida, cumprindo o ponto 6 das funcionalidades implementadas. Para tal, tem duas possibilidades: pode fazê-lo usando dois ou três ficheiros específicos. Na primeira, o utilizador deve indicar o ficheiro Excel Teórico e o ficheiro Excel gerado no menu _Correr Regras_ que já contém os resultados da deteção de code smells. Na segunda, o utilizador deve indicar o Ficheiro Excel Teórico, o Ficheiro Excel com as métricas extraídas gerado no ponto 1 e o ficheiro .txt das regras. Após clicar em _Avaliar_ verá então, de forma textual e em diagramas pieChart, os resultantes indicadores de acerto e de erro.

<img align="center" src="https://i.imgur.com/V8ECVXG.png" width="591.6" height="415.8">

<br>
<br>

#### <ins>AVISO: O utilizador não deve ter os ficheiros Excel indicados abertos noutra aplicação durante o processo de _correr_ e _avaliar regras_.</ins>



***



### Exemplos Ilustrativos de ficheiros usados e/ou gerados

* **Ficheiro Excel Teórico** é um ficheiro Excel que deve conter, pelo menos, os campos package (correspondente à identificação do package onde se encontra o método), class (correspondente à identificação da classe onde se encontra o método), method (correspondente ao nome do método), e os respetivos campos dos code smells preenchidos. Portanto, pode por exemplo ser um ficheiro Excel com as métricas extraídas gerado no ponto 1 ao qual o utilizador tenha manualmente acrescentado as colunas dos code smells preenchidas com booleans ou um ficheiro que já tenha obtido ao guardar os resultados da deteção dos code smells baseada nas regras indicadas no menu _Correr Regras_ (Ficheiro Excel da Deteção dos Code Smells).

<img src="https://i.imgur.com/ezfJL91.png" width="591.6" height="415.8"> 

<br>
<br>
<br>

* **Ficheiro Excel das Métricas** é o ficheiro Excel gerado (no ponto 1) a partir da extração das métricas sob o projeto Java indicado pelo utilizador.
 
<img src="https://i.imgur.com/z6ePaGj.png" width="591.6" height="415.8"> 

<br>
<br>
<br>

* **Ficheiro Excel da Deteção dos Code Smells** é o ficheiro Excel gerado no menu _Correr Regras_ caso o utilizador escolha _Guardar Code Smells_.
 
<img src="https://i.imgur.com/xp5Dcc4.png" width="591.6" height="415.8"> 

<br>
<br>
<br>

* **Ficheiro .txt das Regras** é o ficheiro .txt gerado quando o utilizador guarda as regras criadas no menu _Regras_.
 
<img src="https://i.imgur.com/gplgjdP.png"> 

<br>

## [Bibliotecas](#bibliotecas)
As bibliotecas usadas neste projeto foram:
* Para API de ficheiros Excel:
* * [Apache POI](https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml)
* Para gerar gráficos/diagramas:
* * [JFreeChart](https://mvnrepository.com/artifact/org.jfree/jfreechart)
* Para modelação da GUI:
* * [WindowBuilder](https://www.eclipse.org/windowbuilder/)
* Para as regras:
* * [Nashorn](https://docs.oracle.com/javase/8/docs/technotes/guides/scripting/nashorn/api.html)
* Para testes e coberturas:
* * [JUnit5](https://junit.org/)
* * [Eclemma](https://projects.eclipse.org/projects/technology.eclemma/downloads)
* * [JDeodorant](https://marketplace.eclipse.org/content/jdeodorant)
