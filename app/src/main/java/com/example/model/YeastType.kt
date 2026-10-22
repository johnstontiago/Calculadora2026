package com.example.model

enum class YeastType(
  val id: String,
  val icon: String,
  val displayNameEs: String,
  val displayNamePt: String,
  val percentageText: String,
  val percentageFactor: Double
) {
  FRESCO(
    id = "fresco",
    icon = "🧊",
    displayNameEs = "Fresco",
    displayNamePt = "Fresco",
    percentageText = "0,20%",
    percentageFactor = 0.002
  ),
  SECO(
    id = "seco",
    icon = "🫙",
    displayNameEs = "Seco activo",
    displayNamePt = "Seco ativo",
    percentageText = "0,07%",
    percentageFactor = 0.00067
  )
}
