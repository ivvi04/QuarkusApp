# QuarkusApp — REST API для управления заказами (Orders)

Микросервис на Java с использованием фреймворка **Quarkus**, реализующий управление заказами по принципам **Clean
Architecture**. Приложение предоставляет RESTful API и поддерживает асинхронную коммуникацию через **RabbitMQ**.

## Технологии

| Компонент  | Технология                                    |
|------------|-----------------------------------------------|
| Язык       | Java 25                                       |
| Фреймворк  | Quarkus 3.39.3                                |
| БД         | PostgreSQL (Liquibase для миграций)           |
| ORM        | Hibernate ORM Panache                         |
| Мессенджер | RabbitMQ 4.3.6                                |
| Сервер     | встроенный в Quarkus (JAX-RS)                 |
| Тесты      | JUnit 5, Mockito, RestAssured, Testcontainers |

## Архитектура

Проект реализует **Clean Architecture** с разделением на слои:

```
ru.lakeevda
├── domain/           — Бизнес-модели (Order, OrderName, OrderStatus)
├── application/      — Use Case порты и имплементации, DTO, мапперы
│   ├── dto/          — Domain DTO для передачи между слоями
│   ├── mapper/       — Маппинг между domain и application DTO
│   ├── port/in/      — Входные порты (UseCase интерфейс)
│   │   └── usecase/  — OrderUseCase
│   ├── port/out/     — Выходные порты (репозитории, провайдеры)
│   │   ├── repository/
│   │   └── producer/
│   └── usecase/      — Реализация Use Case
├── infrastructure/   — Инфраструктурная реализация
│   ├── adapter/      — Адаптеры внешних систем (RabbitMQ consumer/producer)
│   └── persistence/  — JPA entity, mapper, repository для БД
├── presentation/     — REST-контроллеры и DTO для API
│   ├── dto/          — Presentation DTO
│   ├── mapper/       — Маппинг между application и presentation DTO
│   └── rest/resource/— REST resource + exception handling
```

## Структура базы данных

Таблица `orders` (схема `quarkus_app`):

| Поле   | Тип          | Описание                           |
|--------|--------------|------------------------------------|
| id     | bigserial PK | Уникальный идентификатор заказа    |
| name   | text         | Название заказа                    |
| status | varchar      | Статус: CREATED, EDITED, COMPLETED |

## REST API Endpoints

### `GET /orders` — Получить все заказы

```json
[
  {
    "id": 1,
    "name": "Заказ 1",
    "status": "CREATED"
  }
]
```

### `GET /orders/{id}` — Получить заказ по ID

```json
{
  "id": 1,
  "name": "Заказ 1",
  "status": "CREATED"
}
```

### `POST /orders` — Создать новый заказ

**Request:**

```json
{
  "name": "Новый заказ",
  "status": "CREATED"
}
```

**Response (201):**

```json
{
  "id": 5,
  "name": "Новый заказ",
  "status": "CREATED"
}
```

### `DELETE /orders/{id}` — Удалить заказ

**Response (204 No Content)**

## Асинхронная коммуникация (RabbitMQ)

Приложение использует **RabbitMQ** для асинхронной публикации событий:

- **Consumer** (`order-events-create`): подписывается на очередь `order-events-create` с routing key `order.create`.
  Обработка входящих заказов без публикации события.
- **Producer** (`order-events-created`): публикует события в очередь `order-events-created` с routing key
  `order.created`, когда заказ создан через REST API (параметр `publish=true`).

## Запуск приложения

### Dev-режим
```shell script
./mvnw quarkus:dev
```

Приложение будет доступно по адресу <http://localhost:8080>.

> **Примечание:** Quarkus Dev UI доступен по адресу <http://localhost:8080/q/dev/>.

### Сборка и запуск JAR
```shell script
./mvnw package
java -jar target/quarkus-app/quarkus-run.jar
```

Для создания _über-jar_:
```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
java -jar target/*-runner.jar
```

### Сборка нативного бинарного файла
```shell script
./mvnw package -Dnative
./target/quarkus-app-1.0.0-SNAPSHOT-runner
```

## Запуск с Docker Compose

Для запуска приложения вместе с PostgreSQL и RabbitMQ:

```shell script
docker-compose up --build
```

Сервисы будут доступны по следующим адресам:

| Сервис                 | Адрес     | Порт  |
|------------------------|-----------|-------|
| QuarkusApp (native)    | localhost | 8080  |
| PostgreSQL             | localhost | 5433  |
| RabbitMQ Management UI | localhost | 15672 |

## Конфигурация

Основные настройки в `src/main/resources/application.properties`:

- **PostgreSQL** — подключение к БД, Liquibase миграции
- **RabbitMQ** — конфигурация consumer/producer каналов
- **Логирование** — уровень INFO (настраиваемый)

## Тесты

Проект содержит три уровня тестирования:

| Тип         | Пакет                     | Описание                                                                              |
|-------------|---------------------------|---------------------------------------------------------------------------------------|
| Unit        | `ru.lakeevda.unit`        | Тесты отдельных классов (domain, application, infrastructure, presentation) с Mockito |
| Integration | `ru.lakeevda.integration` | Интеграционные тесты с RabbitMQ и PostgreSQL через Testcontainers                     |
| IT (E2E)    | `ru.lakeevda.it`          | End-to-end тесты REST API через RestAssured                                           |

### Запуск тестов

```shell script
# Unit-тесты
./mvnw test

# Integration-тесты
./mvnw verify

# Все тесты
./mvnw clean verify
```

## Структура папок с тестами

```
src/test/java/ru/lakeevda
├── unit/                    — Unit-тесты (JUnit 5 + Mockito)
│   ├── application/         — Тесты Use Case и мапперов
│   ├── domain/model/order/  — Тесты бизнес-моделей
│   ├── infrastructure/persistence/mapper/ — Тесты мappers БД
│   └── presentation/mapper/ — Тесты REST мапперов
├── integration/infrastructure/adapter/
│   ├── consumer/            — Тест RabbitMQ consumer
│   └── producer/            — Тест RabbitMQ producer
└── it/presentation/rest/resource/
    └── OrderResourceIT.java — E2E тесты REST API
```

## Лицензия

MIT
