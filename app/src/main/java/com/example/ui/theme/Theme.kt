package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
  primary = ImpastoNavy,
  onPrimary = Color.White,
  primaryContainer = WarmSurfaceContainer,
  onPrimaryContainer = ImpastoNavyDark,
  secondary = ImpastoGoldDark,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFF7F1E5),
  onSecondaryContainer = ImpastoGoldDark,
  tertiary = ImpastoBlueLight,
  onTertiary = Color.White,
  background = WarmBgLight,
  onBackground = TextPrimaryLight,
  surface = WarmSurfaceLight,
  onSurface = TextPrimaryLight,
  surfaceVariant = WarmSurfaceContainer,
  onSurfaceVariant = TextMutedLight,
  outline = BorderLight,
  outlineVariant = Color(0xFFE2E8F0)
)

private val DarkColorScheme = darkColorScheme(
  primary = ImpastoBlueLight,
  onPrimary = Color.White,
  primaryContainer = ImpastoSurfaceContainerDark,
  onPrimaryContainer = Color(0xFFD6E4FF),
  secondary = ImpastoGold,
  onSecondary = Color(0xFF261A00),
  secondaryContainer = Color(0xFF3F3012),
  onSecondaryContainer = ImpastoGoldLight,
  tertiary = ImpastoGold,
  onTertiary = Color.Black,
  background = ImpastoNavyDeep,
  onBackground = TextPrimaryDark,
  surface = ImpastoSurfaceDark,
  onSurface = TextPrimaryDark,
  surfaceVariant = ImpastoSurfaceContainerDark,
  onSurfaceVariant = TextMutedDark,
  outline = BorderDark,
  outlineVariant = Color(0xFF23354E)
)

@Composable
fun ImpastoTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
