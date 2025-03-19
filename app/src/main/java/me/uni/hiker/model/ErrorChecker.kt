package me.uni.hiker.model

import android.content.Context
import androidx.compose.runtime.snapshots.SnapshotStateMap
import me.uni.hiker.R
import java.util.regex.Pattern

class ErrorChecker(
    private val context: Context,
) {
    val errors = SnapshotStateMap<String, String>()

    fun clear() {
        if (errors.isNotEmpty()) {
            errors.clear()
        }
    }

    fun lengthConstraintsMatch(value: String, errorKey: String, min: Int? = null, max: Int? = null): Boolean {
        if (min != null && value.length < min) {
            errors[errorKey] = context.getString(R.string.field_is_too_short).replace("{:VALUE}", min.toString())

            return false
        } else if (max != null && value.length > max) {
            errors[errorKey] = context.getString(R.string.field_is_too_long).replace("{:VALUE}", min.toString())

            return false
        }

        return true
    }

    fun isFieldBlank(value: String, errorKey: String): Boolean {
        if (value.isBlank()) {
            errors[errorKey] = context.getString(R.string.field_is_blank)

            return true
        }

        return false
    }

    fun isValidEmail(value: String, errorKey: String): Boolean {
        if (!emailPattern.matcher(value).matches()) {
            errors[errorKey] = context.getString(R.string.field_must_be_valid_email)

            return false
        }

        return true
    }

    companion object {
        private val emailPattern = Pattern.compile("^([A-Za-z0-9_+-]+\\.?)*[A-Za-z0-9_+-]@([A-Za-z0-9][A-Za-z0-9-]*\\.)+[A-Za-z]{2,}$", Pattern.CASE_INSENSITIVE)
    }
}