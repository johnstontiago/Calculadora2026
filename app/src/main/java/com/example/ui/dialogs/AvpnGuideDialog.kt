package com.example.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ImpastoNavy

@Composable
fun AvpnGuideDialog(
  isSpanish: Boolean,
  onDismiss: () -> Unit
) {
  val scrollState = rememberScrollState()

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = if (isSpanish) "Normas AVPN Disciplinare" else "Disciplinare AVPN",
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        color = ImpastoNavy
      )
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(scrollState)
      ) {
        GuideSection(
          title = if (isSpanish) "🌾 Harina Tipo 00" else "🌾 Farinha Tipo 00",
          content = if (isSpanish) {
            "Fuerza W 280–330 con relación P/L entre 0.50 y 0.60. Proteínas entre 11.5% y 13.5%. Permite maduraciones medias y largas sin colapso del gluten."
          } else {
            "Força W 280–330 com relação P/L entre 0.50 e 0.60. Permite maturações médias e longas sem perda de estrutura."
          }
        )

        Spacer(modifier = Modifier.height(10.dp))

        GuideSection(
          title = if (isSpanish) "💧 Hidratación y Agua" else "💧 Hidratação e Água",
          content = if (isSpanish) {
            "La base clásica AVPN es del 58% al 62%. El agua debe ser potable, no clorada, y a temperatura controlada (Fórmula: 60 - T.harina - T.ambiente)."
          } else {
            "Base clássica AVPN de 58% a 62%. A água deve ser controlada para que a massa não passe de 23°C no amassamento."
          }
        )

        Spacer(modifier = Modifier.height(10.dp))

        GuideSection(
          title = if (isSpanish) "🧂 Sal y Levadura" else "🧂 Sal e Fermento",
          content = if (isSpanish) {
            "Sal marina fina al 2.5% respecto a la harina (o 40–55g por litro de agua). Levadura fresca de cerveza al 0.1%–0.3% según la temperatura ambiente y tiempo de fermentación."
          } else {
            "Sal marinho fino a 2.5% em relação à farinha. Fermento biológico fresco em torno de 0.2%."
          }
        )

        Spacer(modifier = Modifier.height(10.dp))

        GuideSection(
          title = if (isSpanish) "🔥 Horneado Tradicional" else "🔥 Cozedura Tradicional",
          content = if (isSpanish) {
            "Horno de leña o napolitano con suelo refractario (biscotto di Sorrento) a 430–480°C durante 60 a 90 segundos. Diámetro máximo 35 cm y borde elevado (cornicione) de 1 a 2 cm."
          } else {
            "Forno a 430–480°C durante 60 a 90 segundos. Borda de 1 a 2 cm e diâmetro máximo de 35 cm."
          }
        )
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("Entendido", fontWeight = FontWeight.Bold)
      }
    }
  )
}

@Composable
private fun GuideSection(title: String, content: String) {
  Column {
    Text(
      text = title,
      fontWeight = FontWeight.Bold,
      fontSize = 13.sp,
      color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(modifier = Modifier.height(2.dp))
    Text(
      text = content,
      fontSize = 12.sp,
      lineHeight = 16.sp,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}
