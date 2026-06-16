# Spec: hardcoded-typography-rule

## Purpose

Правило Detekt `HardcodedTypography` обнаруживает прямое использование хардкодированных типографических значений в Compose-коде вместо токенов design system.

## Requirements

### Requirement: Обнаружение прямого использования FontFamily
Правило `HardcodedTypography` SHALL репортить `CodeSmell` при обращении к членам `FontFamily.*` (кроме `allowedTokens`).

#### Scenario: Использование FontFamily.SansSerif
- **WHEN** в коде встречается `FontFamily.SansSerif`
- **THEN** правило репортит одно нарушение

#### Scenario: Использование FontFamily.Default
- **WHEN** в коде встречается `FontFamily.Default`
- **THEN** правило репортит одно нарушение

### Requirement: Обнаружение хардкода размера шрифта через .sp
Правило `HardcodedTypography` SHALL репортить `CodeSmell` при использовании числового литерала с суффиксом `.sp`, если значение не входит в список `allowedValues`.

#### Scenario: Хардкод 16.sp, не входящий в allowedValues
- **WHEN** конфиг содержит `allowedValues: [0]` и в коде встречается `fontSize = 16.sp`
- **THEN** правило репортит одно нарушение

#### Scenario: Значение 0.sp входит в allowedValues
- **WHEN** конфиг содержит `allowedValues: [0]` и в коде встречается `fontSize = 0.sp`
- **THEN** правило не репортит нарушений

#### Scenario: allowedValues не задан — все .sp значения запрещены
- **WHEN** `allowedValues` не задан в конфиге и в коде встречается `fontSize = 14.sp`
- **THEN** правило репортит одно нарушение

### Requirement: Обнаружение хардкода TextStyle с типографическими аргументами
Правило `HardcodedTypography` SHALL репортить `CodeSmell` при вызове конструктора `TextStyle(...)`, содержащего аргументы `fontSize`, `fontFamily` или `lineHeight`.

#### Scenario: TextStyle с fontSize
- **WHEN** в коде встречается `TextStyle(fontSize = 14.sp)`
- **THEN** правило репортит одно нарушение

#### Scenario: TextStyle с fontFamily
- **WHEN** в коде встречается `TextStyle(fontFamily = FontFamily.SansSerif)`
- **THEN** правило репортит одно нарушение

### Requirement: Пропуск токенов типографики из allowedTokens
Правило `HardcodedTypography` SHALL NOT репортить нарушение, если выражение начинается с `allowedTokens`.

#### Scenario: Использование токена типографики
- **WHEN** конфиг содержит `allowedTokens: "AppTypography"` и в коде встречается `AppTypography.body1`
- **THEN** правило не репортит нарушений

#### Scenario: Использование MaterialTheme.typography (дефолт)
- **WHEN** конфиг не задан и в коде встречается `MaterialTheme.typography.bodyMedium`
- **THEN** правило не репортит нарушений

### Requirement: Правило по умолчанию неактивно
Правило `HardcodedTypography` SHALL иметь `active: false` по умолчанию.

#### Scenario: Правило не включено
- **WHEN** в `detekt.yml` нет явного включения `HardcodedTypography`
- **THEN** правило не производит findings
