## Why

Существующие Compose Lint / compose-rules проверяют хардкод относительно фиксированных Compose best practices. Командам, использующим собственные design system токены (`AppColors`, `AppSpacing`, etc.), нет возможности задать свои имена объектов — инструмент не применим без форка. Набор правил ds-detekt-rules должен стать универсальным: любая команда подключает плагин и через `detekt.yml` указывает свои токены — без изменения кода.

## What Changes

- Добавлены три новых Detekt-правила в модуле `detekt-compose-design-system-rules`:
  - `HardcodedColor` — обнаруживает хардкод цветов (`Color(0xFF...)`, `Color.Red`, etc.)
  - `HardcodedShape` — обнаруживает хардкод форм (`RoundedCornerShape(8.dp)`, `CircleShape` с явным радиусом, etc.)
  - `HardcodedTypography` — обнаруживает хардкод типографики (`FontFamily.*`, прямые `TextStyle(fontSize = 16.sp)`, etc.)
- Каждое правило читает конфигурацию из `detekt.yml` через `ConfigAware` API:
  - `allowedTokens` — имя объекта/класса с разрешёнными токенами (например, `"AppColors"`)
  - `allowedValues` — список допустимых «магических» значений (например, `[0, 4, 8]` для отступов)
  - `severity` — уровень нарушения на уровне правила
- `DesignSystemConfig` расширен для чтения новых конфиг-ключей
- `ComposeDesignSystemRuleSetProvider` регистрирует три новых правила
- Юнит-тесты для каждого правила с покрытием positive/negative кейсов и кастомной конфигурации
- Документация: README с примером `detekt.yml` и описанием каждого правила

## Capabilities

### New Capabilities

- `hardcoded-color-rule`: Правило обнаружения хардкода цветов с конфигурируемым именем объекта-токена и severity
- `hardcoded-shape-rule`: Правило обнаружения хардкода форм/радиусов с конфигурируемым именем объекта-токена
- `hardcoded-typography-rule`: Правило обнаружения хардкода типографики (шрифты, размеры) с конфигурируемым именем объекта-токена и допустимыми значениями
- `rule-configuration-api`: Единый механизм чтения конфигурации из detekt.yml, переиспользуемый всеми правилами

### Modified Capabilities

<!-- Нет существующих specs, модификации требований не применимы -->

## Impact

- **Модуль**: `detekt-compose-design-system-rules/src/main/kotlin/…/rules/` — 3 новых файла правил
- **Конфиг**: `DesignSystemConfig.kt` расширяется для поддержки новых ключей; `detekt.yml` в проекте дополняется примером конфигурации
- **SPI**: `ComposeDesignSystemRuleSetProvider.kt` регистрирует новые правила
- **Тесты**: `detekt-compose-design-system-rules/src/test/` — минимум 3 новых тест-класса
- **Зависимости**: нет новых внешних зависимостей (используется уже подключённый `detekt-api`)
- **Обратная совместимость**: новые правила по умолчанию `active: false` — существующие проекты не затронуты
