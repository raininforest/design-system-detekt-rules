package com.github.raininforest.detekt.designsystem.rules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.TestConfig
import io.gitlab.arturbosch.detekt.test.lint
import org.junit.Assert.assertEquals
import org.junit.Test

class HardcodedTypographyRuleTest {

    private val rule = HardcodedTypographyRule(Config.empty)

    @Test
    fun `reports FontFamily dot SansSerif`() {
        val code = """
            import androidx.compose.ui.text.font.FontFamily
            val font = FontFamily.SansSerif
        """.trimIndent()

        val findings = rule.lint(code)
        assertEquals(1, findings.size)
    }

    @Test
    fun `reports FontFamily dot Default`() {
        val code = """
            import androidx.compose.ui.text.font.FontFamily
            val font = FontFamily.Default
        """.trimIndent()

        val findings = rule.lint(code)
        assertEquals(1, findings.size)
    }

    @Test
    fun `reports hardcoded sp value`() {
        val code = """
            val size = 16.sp
        """.trimIndent()

        val findings = rule.lint(code)
        assertEquals(1, findings.size)
    }

    @Test
    fun `reports TextStyle with fontSize argument`() {
        val code = """
            import androidx.compose.ui.text.TextStyle
            val style = TextStyle(fontSize = 14.sp)
        """.trimIndent()

        val findings = rule.lint(code)
        assertEquals(1, findings.size)
    }

    @Test
    fun `reports TextStyle with fontFamily argument`() {
        val code = """
            import androidx.compose.ui.text.TextStyle
            import androidx.compose.ui.text.font.FontFamily
            val style = TextStyle(fontFamily = FontFamily.SansSerif)
        """.trimIndent()

        val findings = rule.lint(code)
        assertEquals(1, findings.size)
    }

    @Test
    fun `does not report MaterialTheme typography token`() {
        val code = """
            val style = MaterialTheme.typography.bodyMedium
        """.trimIndent()

        val findings = rule.lint(code)
        assertEquals(0, findings.size)
    }

    @Test
    fun `does not report custom typography token when allowedTokens is set`() {
        val customRule = HardcodedTypographyRule(TestConfig("allowedTokens" to "AppTypography"))
        val code = """
            val style = AppTypography.body1
        """.trimIndent()

        val findings = customRule.lint(code)
        assertEquals(0, findings.size)
    }

    @Test
    fun `does not report sp value in allowedValues`() {
        val customRule = HardcodedTypographyRule(TestConfig("allowedValues" to listOf(0, 4, 8)))
        val code = """
            val size = 0.sp
        """.trimIndent()

        val findings = customRule.lint(code)
        assertEquals(0, findings.size)
    }

    @Test
    fun `reports sp value not in allowedValues`() {
        val customRule = HardcodedTypographyRule(TestConfig("allowedValues" to listOf(0, 4, 8)))
        val code = """
            val size = 16.sp
        """.trimIndent()

        val findings = customRule.lint(code)
        assertEquals(1, findings.size)
    }
}
