package com.mwende.onlinestore.ui.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel : ViewModel() {

    // State for email, password, and confirm password
    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()

    // Error states
    private val _emailError = MutableStateFlow<String?>(null)
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError = _passwordError.asStateFlow()

    private val _confirmPasswordError = MutableStateFlow<String?>(null)
    val confirmPasswordError = _confirmPasswordError.asStateFlow()

    // Functions to update text fields
    fun onEmailChange(newEmail: String) {
        _email.value = newEmail
        _emailError.value = null
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
        _passwordError.value = null
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        _confirmPassword.value = newConfirmPassword
        _confirmPasswordError.value = null
    }

    // Validation functions
    private fun validateEmail(): Boolean {
        val emailPattern = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        return if (_email.value.isEmpty()) {
            _emailError.value = "Email cannot be empty"
            false
        } else if (!emailPattern.matches(_email.value)) {
            _emailError.value = "Invalid email"
            false
        } else {
            _emailError.value = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        return if (_password.value.length < 6) {
            _passwordError.value = "Password must be at least 6 characters"
            false
        } else {
            _passwordError.value = null
            true
        }
    }

    private fun validateConfirmPassword(): Boolean {
        return if (_confirmPassword.value != _password.value) {
            _confirmPasswordError.value = "Passwords do not match"
            false
        } else {
            _confirmPasswordError.value = null
            true
        }
    }

    fun validateLoginForm(): Boolean {
        val emailValid = validateEmail()
        val passwordValid = validatePassword()
        return emailValid && passwordValid
    }

    fun validateSignupForm(): Boolean {
        val emailValid = validateEmail()
        val passwordValid = validatePassword()
        val confirmValid = validateConfirmPassword()
        return emailValid && passwordValid && confirmValid
    }

    fun clearFields() {
        _email.value = ""
        _password.value = ""
        _confirmPassword.value = ""
        _emailError.value = null
        _passwordError.value = null
        _confirmPasswordError.value = null
    }
}

