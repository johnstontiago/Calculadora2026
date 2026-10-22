package com.example.model

enum class FermentationTechnique(
  val id: String,
  val icon: String,
  val displayNameEs: String,
  val displayNamePt: String,
  val descEs: String,
  val descPt: String
) {
  DIRETA(
    id = "direta",
    icon = "🌾",
    displayNameEs = "Directa",
    displayNamePt = "Direta",
    descEs = "Método clásico AVPN. Todos los ingredientes juntos.",
    descPt = "Método clássico AVPN. Todos ingredientes juntos."
  ),
  BIGA(
    id = "biga",
    icon = "🧪",
    displayNameEs = "Biga",
    displayNamePt = "Biga",
    descEs = "Pre-fermento sólido. Más sabor y alveolado.",
    descPt = "Pré-fermento sólido. Mais sabor e alvéolos."
  ),
  POOLISH(
    id = "poolish",
    icon = "💧",
    displayNameEs = "Poolish",
    displayNamePt = "Poolish",
    descEs = "Pre-fermento líquido. Masa extensible y aromática.",
    descPt = "Pré-fermento líquido. Massa extensível e aromática."
  ),
  AUTOLISE(
    id = "autolise",
    icon = "⏳",
    displayNameEs = "Autólisis",
    displayNamePt = "Autólise",
    descEs = "Reposo pre-amasado. Gluten más desarrollado.",
    descPt = "Repouso pré-amassamento. Glúten mais desenvolvido."
  )
}
