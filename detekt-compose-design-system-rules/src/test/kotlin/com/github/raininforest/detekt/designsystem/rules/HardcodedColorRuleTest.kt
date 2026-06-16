package com.github.raininforest.detekt.designsystem.rules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.TestConfig
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
    fun `reports hardcoded RGBA float color`() {
        val code = """
            import androidx.compose.ui.graphics.Color
            val color = Color(1f, 0f, 0f, 1f)
        """.trimIndent()

        val findings = rule.lint(code)
        assertEquals(1, findings.size)
    }

    @Test
    fun `reports Color dot Red`() {
        val code = """
            import androidx.compose.ui.graphics.Color
            val color = Color.Red
        """.trimIndent()

        val findings = rule.lint(code)
        assertEquals(1, findings.size)
    }

    @Test
    fun `reports Color dot Transparent`() {
        val code = """
            import androidx.compose.ui.graphics.Color
            val color = Color.Transparent
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

    @Test
    fun `does not report custom token when allowedTokens is set`() {
        val customRule = HardcodedColorRule(TestConfig("allowedTokens" to "AppColors"))
        val code = """
            val color = AppColors.primary
        """.trimIndent()

        val findings = customRule.lint(code)
        assertEquals(0, findings.size)
    }

    @Test
    fun `still reports hardcoded color when custom allowedTokens is set`() {
        val customRule = HardcodedColorRule(TestConfig("allowedTokens" to "AppColors"))
        val code = """
            import androidx.compose.ui.graphics.Color
            val color = Color(0xFF123456)
        """.trimIndent()

        val findings = customRule.lint(code)
        assertEquals(1, findings.size)
    }
}
