## 1. HardcodedColor — реализация правила

- [x] 1.1 Реализовать `visitCallExpression` в `HardcodedColorRule`: детектировать `Color(0xFF...)` и `Color(r,g,b,a)` через проверку имени callee и наличия числовых аргументов
- [x] 1.2 Реализовать детектирование `Color.Red` / `Color.Blue` / `Color.*` через `visitDotQualifiedExpression` с проверкой receiver `Color`
- [x] 1.3 Добавить чтение `allowedTokens` из config через `valueOrDefault("allowedTokens", "MaterialTheme.colorScheme")` и пропускать выражения с подходящим префиксом
- [x] 1.4 Добавить `active: false` в `Issue` дефолт и задокументировать в `detekt.yml`

## 2. HardcodedShape — новое правило

- [x] 2.1 Создать `HardcodedShapeRule.kt` в `rules/` как наследник `Rule(config)` с `Issue` id `HardcodedShape`, `Severity.Style`
- [x] 2.2 Реализовать `visitCallExpression`: детектировать `RoundedCornerShape(...)` и `CutCornerShape(...)` с числовыми dp-аргументами
- [x] 2.3 Добавить чтение `allowedTokens` из config (`valueOrDefault("allowedTokens", "MaterialTheme.shapes")`) и пропускать родительские выражения с подходящим префиксом
- [x] 2.4 Зарегистрировать `HardcodedShapeRule` в `ComposeDesignSystemRuleSetProvider`

## 3. HardcodedTypography — новое правило

- [x] 3.1 Создать `HardcodedTypographyRule.kt` в `rules/` как наследник `Rule(config)` с `Issue` id `HardcodedTypography`, `Severity.Style`
- [x] 3.2 Реализовать детектирование `FontFamily.*` через `visitDotQualifiedExpression` с проверкой receiver `FontFamily`
- [x] 3.3 Реализовать детектирование числовых `.sp` литералов через `visitPostfixExpression` или `visitCallExpression` (расширение Int/Float); сравнивать с `allowedValues`
- [x] 3.4 Реализовать детектирование `TextStyle(fontSize=..., fontFamily=..., lineHeight=...)` через `visitCallExpression` с именем `TextStyle` и наличием типографических named-аргументов
- [x] 3.5 Добавить чтение `allowedTokens` (`valueOrDefault("allowedTokens", "MaterialTheme.typography")`) и `allowedValues` (`valueOrDefault("allowedValues", emptyList<Int>())`)
- [x] 3.6 Зарегистрировать `HardcodedTypographyRule` в `ComposeDesignSystemRuleSetProvider`

## 4. Юнит-тесты

- [x] 4.1 Расширить `HardcodedColorRuleTest`: добавить тест на `Color.Red`, тест с кастомным `allowedTokens`, тест на отсутствие false-positive для `AppColors.primary`
- [x] 4.2 Создать `HardcodedShapeRuleTest`: positive для `RoundedCornerShape(8.dp)` и `CutCornerShape(4.dp)`; negative для `MaterialTheme.shapes.medium` и кастомного `allowedTokens`
- [x] 4.3 Создать `HardcodedTypographyRuleTest`: positive для `FontFamily.SansSerif`, `16.sp`, `TextStyle(fontSize=14.sp)`; negative для `MaterialTheme.typography.bodyMedium`, `allowedValues=[0]` + `0.sp`
- [x] 4.4 Убедиться, что все тесты проходят: `./gradlew :detekt-compose-design-system-rules:test`

## 5. Конфигурация и документация

- [x] 5.1 Добавить в проектный `detekt.yml` секцию `ComposeDesignSystem` с примером конфигурации всех трёх правил (закомментированный рабочий пример)
- [x] 5.2 Обновить README модуля `detekt-compose-design-system-rules`: описать три правила, все конфиг-ключи (`allowedTokens`, `allowedValues`, `severity`) и полный пример `detekt.yml`
