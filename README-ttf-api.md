# ttf-api

Backend em **Java (Spring Boot)** do jogo [Turtle Trash Fighter](https://github.com/pedronjm/Tecnologia-sustentavel-Turtle-trash-fighter), responsável por cadastro e autenticação de jogadores, configurações do usuário e salvamento de progresso em múltiplos slots na nuvem.

> Projeto acadêmico do curso de Desenvolvimento de Sistemas — CEFET-MG, campus Timóteo. Desenvolvido por **Gabriel Dutra** e **Pedro Nicolini**.

## Visão geral

O jogo consome esta API para permitir que o mesmo progresso seja acessado de qualquer dispositivo, autenticado por usuário e senha:

```
Unity (registro/login) → Spring Boot (gera JWT) → Unity guarda o token
   → Unity envia POST /game-saves (autenticado) → API persiste no banco de dados
```

- Autenticação baseada em **JWT** (token retornado no login/registro e reutilizado nas próximas requisições).
- Suporte a **múltiplos slots de save** por usuário.
- Configurações de jogador (volume, controles) persistidas por conta.

## Tecnologias

- Java + **Spring Boot**
- Spring Security (autenticação/JWT)
- Banco de dados relacional (JPA/Hibernate)
- Maven ou Gradle (conforme o `pom.xml`/`build.gradle` do projeto)

## Modelo de dados

O banco é organizado em três tabelas principais, conforme o diagrama de classes do projeto:

### `game_users`
Conta do jogador.

| Campo | Descrição |
|---|---|
| `id` | Identificador único |
| `login` | Nome de usuário |
| `password_hash` | Senha (armazenada com hash) |
| `nome` | Nome de exibição |
| `created_at_utc` | Data de criação da conta |

### `game_saves`
Um slot de save, vinculado a um usuário (`user_id`).

| Campo | Descrição |
|---|---|
| `id`, `slot_index`, `slot_name` | Identificação do slot |
| `selected_character` | Personagem escolhido (Guerreiro/Arqueiro/Mago) |
| `difficulty` | Dificuldade da partida |
| `scene_name`, `checkpoint`, `checkpoint_id` | Onde o jogador está / último checkpoint |
| `current_health`, `max_health` | Vida atual e máxima |
| `score` | Pontuação |
| `qtt_apple_collected`, `qtt_glass_collected`, `qtt_paper_collected`, `qtt_plastic_collected`, `qtt_metal_collected`, `qtt_electronics_collected` | Quantidade coletada por tipo de resíduo |
| `dead_enemy_ids_json`, `collected_ids_json` | Inimigos derrotados e itens já coletados (para não reaparecerem ao carregar o save) |
| `death_count` | Número de mortes |
| `play_tutorial` | Se o tutorial foi/deve ser jogado |
| `completion_percent` | Percentual de conclusão da fase/jogo |
| `last_saved_at_utc` | Data/hora do último salvamento |

### `game_settings`
Configurações do usuário (1 usuário → N configurações, ou 1:1 dependendo da versão).

| Campo | Descrição |
|---|---|
| `id`, `user_id` | Vínculo com o usuário |
| `volume_geral`, `volume_musica`, `volume_sfx` | Volumes configurados |
| `key_dash`, `key_direita`, `key_esquerda`, `key_interagir`, `key_melee`, `key_pular`, `key_ranger` | Teclas remapeadas pelo jogador |
| `last_saved_at_utc` | Data/hora da última atualização |

## Como rodar localmente

1. Clone o repositório:
   ```bash
   git clone https://github.com/pedronjm/ttf-api.git
   cd ttf-api
   ```
2. Configure as credenciais do banco de dados em `src/main/resources/application.properties` (ou `application.yml`).
3. Suba a aplicação:
   ```bash
   ./mvnw spring-boot:run
   ```
   ou, se o projeto usar Gradle:
   ```bash
   ./gradlew bootRun
   ```
4. Por padrão a API sobe em `http://localhost:8080` (ajuste no `application.properties` se necessário).
5. No projeto do jogo (Unity), aponte a URL do backend usado pelo `RemoteSaveService.cs` para o endereço local antes de testar login/registro/save.

## Principais endpoints (visão conceitual)

| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/auth/register` | Cria uma nova conta de jogador |
| `POST` | `/auth/login` | Autentica e retorna um token JWT |
| `GET` | `/game-saves` | Lista os slots de save do usuário autenticado |
| `POST` | `/game-saves` | Cria ou atualiza um slot de save |
| `GET` / `PUT` | `/game-settings` | Lê e atualiza volume e controles do usuário |

> Os nomes exatos das rotas e DTOs podem variar conforme a implementação atual — consulte os `Controller`s do projeto para a referência definitiva.

## Segurança

- Senhas nunca são armazenadas em texto puro (hash).
- Rotas de save/configuração exigem um token JWT válido no cabeçalho `Authorization`.

## Repositórios relacionados

- Jogo (Unity/C#): [Tecnologia-sustentavel-Turtle-trash-fighter](https://github.com/pedronjm/Tecnologia-sustentavel-Turtle-trash-fighter)
- Aplicação complementar / documentação: [ttf-Aplica](https://github.com/pedronjm/ttf-Aplica)

## Licença

Projeto acadêmico sem fins comerciais, desenvolvido para fins educacionais.
