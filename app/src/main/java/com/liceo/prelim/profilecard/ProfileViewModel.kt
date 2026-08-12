package com.liceo.prelim.profilecard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {
    // Private, editable state — only the ViewModel changes it
    private val _uiState = MutableStateFlow(ProfileUiState())
    // Public, read-only state — the screen observes this
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // --- Update each text field ---
    fun onNameChange(value: String) =
        _uiState.update { it.copy(name = value) }

    fun onEmailChange(value: String) =
        _uiState.update { it.copy(email = value) }

    fun onContactChange(value: String) =
        _uiState.update { it.copy(contactNumber = value) }

    fun onAddressChange(value: String) =
        _uiState.update { it.copy(address = value) }

    fun onUsernameChange(value: String) =
        _uiState.update { it.copy(username = value) }

    fun onNewSkillChange(value: String) =
        _uiState.update { it.copy(newSkill = value) }

    // --- Add / remove skills ---
    fun addSkill() {
        val skill = _uiState.value.newSkill.trim()
        if (skill.isEmpty()) return // ignore empty input
        
        // Bonus: Prevent adding duplicate skills
        if (_uiState.value.skills.contains(skill)) return

        _uiState.update { current ->
            current.copy(
                skills = current.skills + skill, // new list = old + one
                newSkill = "" // clear the input box
            )
        }
    }

    fun removeSkill(skill: String) {
        _uiState.update { current ->
            current.copy(skills = current.skills - skill)
        }
    }

    // --- Preview toggle ---
    fun showPreview() = _uiState.update { it.copy(isPreview = true) }
    fun backToEdit() = _uiState.update { it.copy(isPreview = false) }
    
    // Bonus: Clear all button
    fun clearAll() {
        _uiState.value = ProfileUiState()
    }
}
