package com.liceo.prelim.profilecard

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val contactNumber: String = "",
    val address: String = "",
    val username: String = "",
    val skills: List<String> = emptyList(),
    val newSkill: String = "", // text typed in the "add skill" box
    val isPreview: Boolean = false // false = editing, true = preview
)
