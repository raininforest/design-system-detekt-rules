## Context

Модуль `detekt-compose-design-system-rules` уже содержит `HardcodedColorRule` (stub с TODO), `DesignSystemConfig` (читает общий конфиг) и `ComposeDesignSystemRuleSetProvider`. Правила реализованы через Detekt `Rule`, который наследует `ConfigAware` — каждый экземпляр правила автоматически получает свой sub-config из блока `ComposeDesignSystem > <RuleName>` в `detekt.yml`. Это стандартный механизм Detekt, не требующий кастомного парсинга YAML.

## Goals / Non-Goals

**Goals:**
- Реализовать три правила: `HardcodedColor`, `HardcodedShape`, `HardcodedTypography`
- Каждое правило читает `allowedTokens` (строка) из своего sub-config через `valueOrDefault`
- `HardcodedTypography` дополнительно читает `allowedValues: List<Int>` для допустимых магических чисел
- Правила по умолчанию `active: false` — нулевое влияние на существующие проекты
- Полное покрытие юнит-тестами, включая кастомную конфигурацию

**Non-Goals:**
- Автофикс (quick fix / autocorrect) — только репорт нарушений
- Поддержка мульти-токенных объектов в одной строке конфига (один `allowedTokens` на правило)
- Интеграция с Android Lint (только Detekt)
- Анализ runtime-значений (только статический AST)

## Decisions

### 1. Per-rule config через ConfigAware, а не единый DesignSystemConfig

**Решение**: каждое правило читает `config.valueOrDefault(...)` напрямую из своего `config`-параметра. Существующий `DesignSystemConfig` сохраняется для обратной совместимости но не используется новыми правилами.

**Альтернатива**: расширить `DesignSystemConfig` с новыми полями и передавать его в каждое правило.

**Почему отклонено**: при передаче shared-конфига теряется стандартный Detekt-способ переопределения правил (блок `HardcodedColor: allowedTokens: ...` в yml). Пользователи привыкли к этому паттерну, ломать его нет смысла.

### 2. PSI-узлы для каждого правила

**HardcodedColor** — посещает `KtCallExpression` (ловит `Color(0xFF123456)`, `Color(red=1f, ...)`) и `KtDotQualifiedExpression` + `KtReferenceExpression` (ловит `Color.Red`, `Color.Blue`, etc.). Фильтрует узлы, где receiver или callee — `Color`, а выражение не начинается с `allowedTokens`.

**HardcodedShape** — посещает `KtCallExpression` с именами `RoundedCornerShape`, `CutCornerShape`, `CircleShape` (когда содержит явный аргумент-размер). Пропускает, если родительское выражение — `allowedTokens.*`.

**HardcodedTypography** — посещает:
- `KtCallExpression` с именем `TextStyle` с аргументами `fontSize`, `fontFamily`, `lineHeight`
- `KtDotQualifiedExpression` начинающееся с `FontFamily.` (кроме `allowedTokens`)
- Числовые литералы `.sp` / `.em` — репортятся, если значение не в `allowedValues`

### 3. allowedTokens как строка-префикс (не regex)

**Решение**: `allowedTokens` — простая строка, проверка через `startsWith(allowedTokens)`. Пример: `allowedTokens: "AppColors"` пропускает `AppColors.primary`, `AppColors.background`.

**Альтернатива**: regex или список строк.

**Почему отклонено**: для 95% кейсов достаточно prefix-проверки, regex усложняет конфиг для пользователя. Список строк можно добавить позже как `allowedTokensList`.

### 4. Severity задаётся через стандартный Detekt-механизм

Detekt позволяет переопределить severity на уровне правила в yml (`severity: warning`). Правила объявляют дефолтный `Severity.Style` в `Issue`. Документируем стандартный механизм переопределения — не изобретаем кастомный.

## Risks / Trade-offs

- **PSI-совместимость между версиями Kotlin** → Mitigation: тесты гоняются на CI с фиксированной версией Kotlin Compiler Embeddable, указанной в `build.gradle`.
- **allowedTokens не покрывает nested-выражения** (например, `val c = someFunction(AppColors.primary)` — Color внутри вызова) → Mitigation: scope правил — прямые присваивания и аргументы Compose-функций; сложные вложения — известное ограничение, документируется.
- **HardcodedColorRule уже существует как stub** → без breaking change: добавляем реализацию в тот же класс, тесты дополняются, а не переписываются.

## Migration Plan

1. Реализовать три правила и тесты (всё в `detekt-compose-design-system-rules`)
2. Зарегистрировать в `ComposeDesignSystemRuleSetProvider`
3. Добавить пример конфигурации в `detekt.yml` проекта (все правила `active: false` по умолчанию)
4. Обновить README с описанием конфиг-ключей и примером

Откат: удалить регистрацию правил в провайдере — достаточно, правила не влияют на существующий код при `active: false`.
