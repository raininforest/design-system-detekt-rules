## ADDED Requirements

### Requirement: Обнаружение хардкода RoundedCornerShape с явным размером
Правило `HardcodedShape` SHALL репортить `CodeSmell` при вызове `RoundedCornerShape(...)`, `CutCornerShape(...)` или `CircleShape` с числовым dp-аргументом.

#### Scenario: RoundedCornerShape с числовым аргументом
- **WHEN** в коде встречается `RoundedCornerShape(8.dp)`
- **THEN** правило репортит одно нарушение

#### Scenario: CutCornerShape с числовым аргументом
- **WHEN** в коде встречается `CutCornerShape(4.dp)`
- **THEN** правило репортит одно нарушение

#### Scenario: RoundedCornerShape с процентным аргументом
- **WHEN** в коде встречается `RoundedCornerShape(50)`
- **THEN** правило репортит одно нарушение

### Requirement: Пропуск форм из разрешённого объекта токенов
Правило `HardcodedShape` SHALL NOT репортить нарушение, если выражение формы начинается с `allowedTokens`.

#### Scenario: Обращение к токену формы
- **WHEN** конфиг содержит `allowedTokens: "AppShapes"` и в коде встречается `AppShapes.medium`
- **THEN** правило не репортит нарушений

#### Scenario: Использование MaterialTheme.shapes (дефолт)
- **WHEN** конфиг не задан и в коде встречается `MaterialTheme.shapes.medium`
- **THEN** правило не репортит нарушений

### Requirement: Пропуск форм без явного аргумента
Правило `HardcodedShape` SHALL NOT репортить нарушение для `RoundedCornerShape` или `CutCornerShape`, вызванных без аргументов (вызов невалиден, ловится компилятором; правило не дублирует ошибку компилятора).

#### Scenario: Пустой вызов конструктора
- **WHEN** в коде встречается `RoundedCornerShape()` (без аргументов)
- **THEN** правило не репортит нарушений (ошибка компилятора, не наша)

### Requirement: Правило по умолчанию неактивно
Правило `HardcodedShape` SHALL иметь `active: false` по умолчанию.

#### Scenario: Правило не включено
- **WHEN** в `detekt.yml` нет явного включения `HardcodedShape`
- **THEN** правило не производит findings
