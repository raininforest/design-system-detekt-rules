package com.github.raininforest.detekt.designsystem.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtConstantExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtExpression

class HardcodedShapeRule(config: Config = Config.empty) : Rule(config) {

    override val issue = Issue(
        id = "HardcodedShape",
        severity = Severity.Style,
        description = "Hardcoded shape detected. Use design system shape tokens instead.",
        debt = Debt.FIVE_MINS,
    )

    private val allowedTokens: String = valueOrDefault("allowedTokens", "MaterialTheme.shapes")

    private val shapeConstructors = setOf("RoundedCornerShape", "CutCornerShape", "CircleShape")

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)
        val calleeName = expression.calleeExpression?.text ?: return
        if (calleeName in shapeConstructors && expression.valueArguments.isNotEmpty()) {
            val hasLiteralArg = expression.valueArguments.any { arg ->
                arg.getArgumentExpression().containsLiteralDimension()
            }
            if (hasLiteralArg && !expression.text.startsWith(allowedTokens)) {
                report(
                    CodeSmell(
                        issue,
                        Entity.from(expression),
                        "Hardcoded shape: '${expression.text}'. Use $allowedTokens tokens instead.",
                    )
                )
            }
        }
    }

    private fun KtExpression?.containsLiteralDimension(): Boolean = when (this) {
        is KtConstantExpression -> true
        is KtDotQualifiedExpression ->
            receiverExpression is KtConstantExpression &&
                selectorExpression?.text in setOf("dp", "sp", "em")
        else -> false
    }
}
