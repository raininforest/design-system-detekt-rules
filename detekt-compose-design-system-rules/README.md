# detekt-compose-design-system-rules

Configurable Detekt rules for detecting hardcoded design values in Jetpack Compose code.

Unlike compose-rules (which are hardcoded to Compose best practices), this plugin lets any team configure their own design system token names via `detekt.yml` — making it work with `AppColors`, `AppShapes`, `AppTypography`, or any other naming convention.

## Rules

### HardcodedColor

Reports usages of `Color(...)` constructors with literal arguments and `Color.*` named constants.

| Detected | Not reported (token usage) |
|---|---|
| `Color(0xFF123456)` | `MaterialTheme.colorScheme.primary` |
| `Color(1f, 0f, 0f)` | `AppColors.primary` (with `allowedTokens: "AppColors"`) |
| `Color.Red` | |
| `Color.Transparent` | |

**Config keys:**
- `allowedTokens` (String, default: `"MaterialTheme.colorScheme"`) — expressions starting with this prefix are not reported.

### HardcodedShape

Reports usages of `RoundedCornerShape(...)` and `CutCornerShape(...)` with numeric literal arguments.

| Detected | Not reported |
|---|---|
| `RoundedCornerShape(8.dp)` | `MaterialTheme.shapes.medium` |
| `CutCornerShape(4.dp)` | `AppShapes.card` (with `allowedTokens: "AppShapes"`) |
| `RoundedCornerShape(50)` | |

**Config keys:**
- `allowedTokens` (String, default: `"MaterialTheme.shapes"`)

### HardcodedTypography

Reports usages of `FontFamily.*` constants and numeric `.sp` / `.em` literals.

| Detected | Not reported |
|---|---|
| `FontFamily.SansSerif` | `MaterialTheme.typography.bodyMedium` |
| `FontFamily.Default` | `AppTypography.body1` (with `allowedTokens: "AppTypography"`) |
| `16.sp` | `0.sp` (with `allowedValues: [0]`) |
| `TextStyle(fontSize = 14.sp)` | |

**Config keys:**
- `allowedTokens` (String, default: `"MaterialTheme.typography"`)
- `allowedValues` (List\<Int\>, default: `[]`) — `.sp`/`.em` values in this list are not reported. Useful for known sentinel values like `0`.

## Configuration

Add to `detekt.yml`:

```yaml
ComposeDesignSystem:
  HardcodedColor:
    active: true
    allowedTokens: "AppColors"
    severity: warning

  HardcodedShape:
    active: true
    allowedTokens: "AppShapes"

  HardcodedTypography:
    active: true
    allowedTokens: "AppTypography"
    allowedValues: [0, 4, 8]
```

All rules are **inactive by default** (`active: false`). Enable only the rules relevant to your project.

### Overriding severity

Use Detekt's standard `severity` key per rule:

```yaml
HardcodedColor:
  active: true
  severity: warning   # error | warning | info | style (default)
```

### Excluding token definition files

If your design system tokens are defined using the same hardcoded values (e.g. `val primary = Color(0xFF1234AB)` in a tokens file), exclude that file in your detekt config:

```yaml
detekt:
  excludes:
    - "**/designsystem/tokens/**"
```

## Setup

Add the plugin JAR to your Detekt classpath in `build.gradle.kts`:

```kotlin
detektPlugins(project(":detekt-compose-design-system-rules"))
```
