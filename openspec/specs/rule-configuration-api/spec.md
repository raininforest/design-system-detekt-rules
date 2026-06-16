# Spec: rule-configuration-api

## Purpose

Описывает общий API конфигурации правил через `detekt.yml`, включая механизм `allowedTokens`, `allowedValues` и переопределение severity.

## Requirements

### Requirement: Конфигурация allowedTokens через detekt.yml
Каждое правило SHALL читать строковый конфиг-ключ `allowedTokens` из своего sub-config через стандартный Detekt `ConfigAware.valueOrDefault`. Значение SHALL использоваться как prefix для проверки `startsWith`.

#### Scenario: allowedTokens задан в detekt.yml
- **WHEN** detekt.yml содержит блок `ComposeDesignSystem > HardcodedColor > allowedTokens: "AppColors"`
- **THEN** правило `HardcodedColor` использует `"AppColors"` как разрешённый префикс и не репортит `AppColors.*` выражения

#### Scenario: allowedTokens не задан — используется дефолт
- **WHEN** в detekt.yml нет `allowedTokens` для правила
- **THEN** правило использует встроенный дефолт (например, `"MaterialTheme.colorScheme"` для `HardcodedColor`)

### Requirement: Конфигурация allowedValues для HardcodedTypography
Правило `HardcodedTypography` SHALL читать список целых чисел `allowedValues` из своего sub-config. Значения из списка SHALL считаться допустимыми "магическими" числами и не SHALL вызывать нарушение.

#### Scenario: allowedValues задан — разрешённые значения пропускаются
- **WHEN** detekt.yml содержит `allowedValues: [0, 4, 8]` и в коде встречается `lineHeight = 0.sp`
- **THEN** правило не репортит нарушений для значения `0`

#### Scenario: allowedValues задан — запрещённые значения репортятся
- **WHEN** detekt.yml содержит `allowedValues: [0, 4, 8]` и в коде встречается `lineHeight = 16.sp`
- **THEN** правило репортит одно нарушение

#### Scenario: allowedValues не задан — все магические числа запрещены
- **WHEN** `allowedValues` не задан в конфиге
- **THEN** дефолт — пустой список, любое числовое `.sp` значение репортится как нарушение

### Requirement: Переопределение severity через стандартный Detekt-механизм
Правила SHALL поддерживать переопределение severity через стандартный Detekt конфиг-ключ `severity` в блоке правила. Документация SHALL описывать этот механизм с примером.

#### Scenario: severity задан в detekt.yml
- **WHEN** detekt.yml содержит `ComposeDesignSystem > HardcodedColor > severity: warning`
- **THEN** Detekt применяет severity `warning` к findings этого правила (стандартное поведение Detekt)

### Requirement: Пример конфигурации в проектном detekt.yml
Проектный `detekt.yml` SHALL содержать закомментированный пример конфигурации для всех трёх правил, демонстрирующий все доступные ключи.

#### Scenario: Наличие примера в detekt.yml
- **WHEN** разработчик открывает `detekt.yml` проекта
- **THEN** он видит секцию `ComposeDesignSystem` с примерами для `HardcodedColor`, `HardcodedShape`, `HardcodedTypography`
