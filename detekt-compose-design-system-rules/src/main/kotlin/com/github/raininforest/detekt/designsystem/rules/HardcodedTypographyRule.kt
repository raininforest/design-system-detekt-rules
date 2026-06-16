package com.github.raininforest.detekt.designsystem.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtConstantExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression

class HardcodedTypographyRule(config: Config = Config.empty) : Rule(config) {

    override val issue = Issue(
        id = "HardcodedTypography",
        severity = Severity.Style,
        description = "Hardcoded typography value detected. Use design system typography tokens instead.",
        debt = Debt.FIVE_MINS,
    )

    private val allowedTokens: String = valueOrDefault("allowedTokens", "MaterialTheme.typography")
    private val allowedValues: List<Int> = valueOrDefault("allowedValues", emptyList())

    override fun visitDotQualifiedExpression(expression: KtDotQualifiedExpression) {
        super.visitDotQualifiedExpression(expression)
        val receiver = expression.receiverExpression
        val selectorText = expression.selectorExpression?.text ?: return

        if (receiver.text == "FontFamily" && !expression.text.startsWith(allowedTokens)) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(expression),
                    "Hardcoded font family: '${expression.text}'. Use $allowedTokens tokens instead.",
                )
            )
            return
        }

        if (selectorText in setOf("sp", "em") && receiver is KtConstantExpression) {
            val numericValue = receiver.text.trimEnd('f', 'F', 'L', 'l').toDoubleOrNull()?.toInt()
            if (numericValue == null || numericValue !in allowedValues) {
                report(
                    CodeSmell(
                        issue,
                        Entity.from(expression),
                        "Hardcoded text unit: '${expression.text}'. Use $allowedTokens tokens instead.",
                    )
                )
            }
        }
    }
}
