# Spec: hardcoded-color-rule

## Purpose

Правило Detekt `HardcodedColor` обнаруживает прямое использование хардкодированных цветов в Compose-коде вместо токенов design system.

## Requirements

### Requirement: Обнаружение хардкода цветов через конструктор Color
Правило `HardcodedColor` SHALL репортить `CodeSmell` при обнаружении вызова конструктора `Color(...)` с числовым или float-аргументом (например, `Color(0xFF123456)`, `Color(1f, 0f, 0f)`).

#### Scenario: Хардкод hex-цвета
- **WHEN** в коде встречается выражение `Color(0xFF123456)`
- **THEN** правило репортит одно нарушение с сообщением о хардкоде цвета

#### Scenario: Хардкод RGBA float-цвета
- **WHEN** в коде встречается выражение `Color(red = 1f, green = 0f, blue = 0f, alpha = 1f)`
- **THEN** правило репортит одно нарушение

### Requirement: Обнаружение хардкода именованных цветов Color.*
Правило `HardcodedColor` SHALL репортить `CodeSmell` при использовании предопределённых констант `Color.Red`, `Color.Blue`, `Color.Black` и других членов объекта `Color`.

#### Scenario: Хардкод Color.Red
- **WHEN** в коде встречается выражение `Color.Red`
- **THEN** правило репортит одно нарушение

#### Scenario: Хардкод Color.Transparent в аргументе функции
- **WHEN** Compose-функция вызывается с аргументом `color = Color.Transparent`
- **THEN** правило репортит одно нарушение

### Requirement: Пропуск разрешённых токенов
Правило `HardcodedColor` SHALL NOT репортить нарушение, если цветовое выражение начинается с значения конфиг-ключа `allowedTokens`.

#### Scenario: Использование токена из allowedTokens
- **WHEN** конфиг содержит `allowedTokens: "AppColors"` и в коде встречается `AppColors.primary`
- **THEN** правило не репортит нарушений

#### Scenario: Использование MaterialTheme.colorScheme (дефолт)
- **WHEN** конфиг не задан (используются дефолты) и в коде встречается `MaterialTheme.colorScheme.primary`
- **THEN** правило не репортит нарушений

### Requirement: Правило по умолчанию неактивно
Правило `HardcodedColor` SHALL иметь `active: false` по умолчанию и SHALL NOT влиять на проекты, не включившие его явно.

#### Scenario: Правило не включено в detekt.yml
- **WHEN** в `detekt.yml` нет блока `ComposeDesignSystem.HardcodedColor` или `active: false`
- **THEN** правило не производит findings при запуске detekt
