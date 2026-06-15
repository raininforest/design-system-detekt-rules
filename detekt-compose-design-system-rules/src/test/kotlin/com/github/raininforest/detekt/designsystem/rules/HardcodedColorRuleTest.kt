package com.github.raininforest.detekt.designsystem.rules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.lint
import org.junit.Assert.assertEquals
import org.junit.Test

class HardcodedColorRuleTest {

    private val rule = HardcodedColorRule(Config.empty)

    @Test
    fun `reports hardcoded hex color`() {
        val code = """
            import androidx.compose.ui.graphics.Color
            val color = Color(0xFF123456)
        """.trimIndent()

        val findings = rule.lint(code)
        assertEquals(1, findings.size)
    }

    @Test
    fun `does not report design system color token`() {
        val code = """
            val color = MaterialTheme.colorScheme.primary
        """.trimIndent()

        val findings = rule.lint(code)
        assertEquals(0, findings.size)
    }
}
