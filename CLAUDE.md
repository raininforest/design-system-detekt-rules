# ds-detekt-rules

Статический анализатор Kotlin/Compose кода на базе Detekt. Ищет хардкод вместо токенов design system и предлагает замену.

## Что проверяется

| Проверка | Хардкод | Должно быть |
|---|---|---|
| Цвета | `Color(0xFF...)`, `Color.Red` | `MaterialTheme.colorScheme.*` или кастомные токены |
| Размеры | `16.dp`, `24.sp` | токены `Spacing.*` или `Typography.*` |
| Шрифты | прямое использование `FontFamily` | токены типографики |
| Радиус | `RoundedCornerShape(8.dp)` | `Shapes.medium` и т.п. |

## Технология

Правила пишутся на базе **Detekt** с использованием **PSI (Program Structure Interface)** — того же API, на котором строятся стандартные Android Lint checks. Каждое правило — наследник `Rule` из detekt-api, анализирует AST Kotlin-кода.

## Структура модулей

- `app/` — тестовое Android-приложение на Compose (minSdk 24, compileSdk 36), используется как живой стенд для проверки правил

## Команды

```bash
./gradlew test           # юнит-тесты правил
./gradlew detekt         # прогнать detekt по проекту
./gradlew assembleDebug  # собрать тестовое приложение
```

## Package

`com.github.raininforest.ds.rules`
