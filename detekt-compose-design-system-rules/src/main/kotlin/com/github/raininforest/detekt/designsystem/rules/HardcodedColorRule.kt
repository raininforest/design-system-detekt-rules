package com.github.raininforest.detekt.designsystem.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtCallExpression

class HardcodedColorRule(config: Config = Config.empty) : Rule(config) {

    override val issue = Issue(
        id = "HardcodedColor",
        severity = Severity.Style,
        description = "Hardcoded color detected. Use design system color tokens instead.",
        debt = Debt.FIVE_MINS,
    )

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)
        // TODO: implement detection of Color(0xFF...) and Color.Red usages
    }
}
