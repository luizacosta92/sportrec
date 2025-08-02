# SportRec API - Sistema de Gestão Esportiva

## 📖 Sobre o Projeto

**SportRec** é uma API REST robusta para gestão de partidas de futubol, projetada para oferecer funcionalidades de retrospecto e ranking de clubes. O nome "SportRec" deriva de **Sport Record**, refletindo seu propósito de registrar e analisar estatísticas esportivas de forma abrangente.

### 🎯 Visão do Produto

O sistema foi pensado com **escalabilidade** em mente, podendo ser facilmente adaptado para:
- ⚽ **Futebol** (implementação atual)
- 🏀 **Basquete, Vôlei, Handebol**
- 🏆 **Campeonatos** e competições organizadas
- 📊 **Diferentes modalidades esportivas**

---

## 🚀 Tecnologias e Stack Utilizadas

### **Backend Core**
- ☕ **Java 21** - Linguagem principal
- 🌱 **Spring Boot 3.5.3** - Framework principal
- 🗃️ **Spring Data JPA** - Persistência de dados
- 🔍 **JPA Specification Executor** - Consultas dinâmicas avançadas
- ✅ **Spring Validation** - Validação de dados

### **Banco de Dados**
- 🐬 **MySQL** - Sistema de gerenciamento de banco de dados

### **Arquitetura e Padrões**
- 🏗️ **Arquitetura em Camadas** (Controller → Service → Repository)
- 📦 **DTOs (Data Transfer Objects)** - Transferência segura de dados
- 🔧 **Dependency Injection** - Baixo acoplamento entre componentes
- 📋 **Specification Pattern** - Consultas dinâmicas e reutilizáveis

---

## 🏛️ Estrutura da API

### **Módulos Principais**

```
📁 sportrec/
├── 🏟️ clube/           # Gestão de clubes
├── ⚽ partida/         # Gestão de partidas  
├── 🏟️ estadio/         # Gestão de estádios
├── 📊 retrospecto/     # Análise de retrospecto
└── 🏆 ranking/         # Sistema de ranking
```

### **Estrutura por Módulo**

```
📁 retrospecto/
├── controller/     # Endpoints REST
├── service/        # Regras de negócio
├── specification/  # Consultas dinâmicas  
├── util/          # Utilitários de cálculo
└── *.dto.java     # Objetos de transferência

📁 ranking/
├── controller/    # Endpoints REST
├── service/       # Regras de negócio  
├── validator/     # Validações e ordenação
├── util/         # Utilitários de cálculo
├── enums/        # Tipos de ranking
└── *.dto.java    # Objetos de transferência
```

---

## 🔌 Endpoints da API

### **🏟️ Clubes**
```http
GET    /clube              # Listar clubes (com filtros)
GET    /clube/{id}         # Buscar clube específico
POST   /clube              # Criar novo clube
PUT    /clube/{id}         # Atualizar clube
DELETE /clube/{id}         # Remover clube
```

### **⚽ Partidas**
```http
GET    /partida            # Listar partidas (com filtros)
GET    /partida/{id}       # Buscar partida específica
POST   /partida            # Criar nova partida
PUT    /partida/{id}       # Atualizar partida
DELETE /partida/{id}       # Remover partida
```

### **📊 Retrospecto**
```http
GET /retrospecto/{clubeId}/geral                    # Retrospecto geral do clube
GET /retrospecto/{clubeId}/adversarios              # Retrospecto contra adversários
GET /retrospecto/confronto/{clube1Id}/{clube2Id}   # Confronto direto entre clubes
```

### **🏆 Ranking**
```http
GET /ranking?ordenarPor=PONTOS        # Ranking por pontos (padrão)
GET /ranking?ordenarPor=GOLS          # Ranking por gols feitos
GET /ranking?ordenarPor=VITORIAS      # Ranking por vitórias
GET /ranking?ordenarPor=TOTAL_JOGOS   # Ranking por jogos disputados
```

---

## 🧠 Conceitos e Padrões Implementados

### **1. Separation of Concerns (SoC)**
- **Controllers**: Apenas gerenciam requisições HTTP
- **Services**: Contêm regras de negócio
- **Utilities**: Lógica de cálculos matemáticos
- **Validators**: Validações e ordenações

### **2. JPA Specification Pattern**
```java
// Consultas dinâmicas e reutilizáveis
Specification<PartidaModel> spec = RetrospectoSpecification
    .clubeParticipou(clubeId)
    .and(RetrospectoSpecification.confrontoDireto(clube1Id, clube2Id));
```

### **3. Optional Pattern**
```java
// Tratamento seguro de valores nulos
public Optional<RetrospectoGeralDto> buscarRetrospecto(Long clubeId) {
    return clubeExiste(clubeId) ? Optional.of(retrospecto) : Optional.empty();
}
```

### **4. DTO Pattern**
- **Segurança**: Exposição controlada de dados
- **Performance**: Redução de dados trafegados
- **Flexibilidade**: Estruturas específicas por funcionalidade

### **5. Strategy Pattern**
```java
// Diferentes estratégias de ordenação
switch (tipoRanking) {
    case PONTOS -> ordenarPorPontos();
    case GOLS -> ordenarPorGols();
    // ...
}
```

---

## ✨ Pontos Positivos

### **🏗️ Arquitetura**
- ✅ **Modular**: Cada módulo é independente e coeso
- ✅ **Extensível**: Fácil adição de novos esportes/funcionalidades  
- ✅ **Testável**: Classes pequenas com responsabilidades bem definidas
- ✅ **Manutenível**: Código limpo e bem organizado

### **⚡ Performance**
- ✅ **Lazy Loading**: Carregamento sob demanda de entidades relacionadas
- ✅ **JPA Specifications**: Consultas otimizadas e dinâmicas
- ✅ **HikariCP**: Pool de conexões de alta performance
- ✅ **DTOs**: Transferência eficiente de dados

### **🛡️ Segurança e Robustez**  
- ✅ **Validation**: Validação automática de entrada de dados
- ✅ **Exception Handling**: Tratamento consistente de erros
- ✅ **Optional**: Prevenção de NullPointerExceptions
- ✅ **Type Safety**: Uso de Enums para tipos seguros

### **📈 Escalabilidade**
- ✅ **Multi-esporte**: Estrutura preparada para diferentes modalidades
- ✅ **Configurável**: Rankings e filtros facilmente extensíveis
- ✅ **RESTful**: API padronizada e bem documentada

---

## 🔄 Pontos de Melhoria

### **📊 Funcionalidades**
- 🔄 **Paginação no Ranking**: Implementar Pageable para grandes volumes
- 🔄 **Cache**: Implementar cache para consultas frequentes (Redis)
- 🔄 **Filtros Avançados**: Período de datas, local, tipo de competição
- 🔄 **Estatísticas Avançadas**: Médias, tendências, comparações históricas

### **🛡️ Segurança**
- 🔄 **Autenticação**: Implementar Spring Security + JWT
- 🔄 **Autorização**: Controle de acesso por perfil (admin, usuário)
- 🔄 **Rate Limiting**: Controle de requisições por usuário
- 🔄 **CORS**: Configuração adequada para frontend

### **🧪 Qualidade**
- 🔄 **Testes**: Ampliar testes unitários, integração e end-to-end
- 🔄 **Documentação**: OpenAPI/Swagger para documentação interativa  
- 🔄 **Logging**: Sistema estruturado de logs (ELK Stack)
- 🔄 **Monitoramento**: Métricas e health checks

### **⚡ Performance**
- 🔄 **Database Indexing**: Índices otimizados para consultas frequentes
- 🔄 **Query Optimization**: Análise e otimização de queries N+1
- 🔄 **Async Processing**: Processamento assíncrono para operações pesadas
- 🔄 **CDN**: Cache de assets estáticos

### **🏗️ Arquitetura**
- 🔄 **Microservices**: Separação em serviços independentes
- 🔄 **Message Queue**: Processamento assíncrono (RabbitMQ/Kafka)
- 🔄 **API Gateway**: Centralizador de APIs
- 🔄 **Container**: Docker para deployment

---

## 🚀 Como Executar

### **Pré-requisitos**
- Java 21+
- MySQL 8.0+
- Maven 3.8+

### **Configuração**
1. Clone o repositório
2. Configure `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/sportrec
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
   ```
3. Execute: `mvn spring-boot:run`

### **Acesso**
- **API Base**: `http://localhost:8080`
- **Health Check**: `http://localhost:8080/actuator/health`

---

## 🤝 Contribuição

Contribuições são bem-vindas! Por favor:

1. 🍴 Faça um fork do projeto
2. 🌿 Crie uma branch para sua feature
3. ✅ Adicione testes para novas funcionalidades  
4. 📝 Documente mudanças relevantes
5. 🔄 Abra um Pull Request

---

## 📋 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

---

## 👨‍💻 Autor

Desenvolvido com ❤️ pensando em **escalabilidade**, **performance** e **boas práticas** de desenvolvimento.

---

*SportRec - Transformando dados esportivos em insights valiosos* ⚽📊🏆
