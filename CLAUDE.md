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

```
ds-detekt-rules/
├── detekt-compose-design-system-rules/   # Kotlin JVM-библиотека с правилами detekt
│   └── src/main/kotlin/com/github/raininforest/detekt/designsystem/
│       ├── rules/                        # по одному файлу на правило
│       ├── config/DesignSystemConfig.kt  # конфиг токенов (читается из detekt.yml)
│       └── ComposeDesignSystemRuleSetProvider.kt  # SPI точка входа
└── app/                                  # Android Compose-приложение (живой стенд)
```

Регистрация плагина через SPI:
`src/main/resources/META-INF/services/io.gitlab.arturbosch.detekt.api.RuleSetProvider`

## Команды

```bash
./gradlew :detekt-compose-design-system-rules:test  # юнит-тесты правил
./gradlew detekt                                     # прогнать detekt по проекту
./gradlew :app:assembleDebug                         # собрать тестовое приложение
```

## Package

- Правила: `com.github.raininforest.detekt.designsystem`
- Тестовое приложение: `com.github.raininforest.ds.rules`
