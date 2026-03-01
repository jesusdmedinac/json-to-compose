package com.jesusdmedinac.jsontocompose.renderer

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.jesusdmedinac.jsontocompose.ToCompose
import com.jesusdmedinac.jsontocompose.model.ComposeNode
import com.jesusdmedinac.jsontocompose.model.ComposeType
import com.jesusdmedinac.jsontocompose.model.NodeProperties
import com.jesusdmedinac.jsontocompose.modifier.from
import com.jesusdmedinac.jsontocompose.state.resolveStateHostValue

@Composable
fun ComposeNode.ToTextField() {
    val props = properties as? NodeProperties.TextFieldProps ?: return
    val modifier = (Modifier from composeModifier).testTag(type.name)

    val (value, valueStateHost) = resolveStateHostValue(
        stateHostName = props.valueStateHostName,
        inlineValue = props.value,
        defaultValue = "",
    )

    val (isError, _) = resolveStateHostValue(
        stateHostName = props.isErrorStateHostName,
        inlineValue = props.isError,
        defaultValue = false,
    )

    val kbType = when (props.keyboardType) {
        "Number", "NumberPassword" -> KeyboardType.Number
        "Email" -> KeyboardType.Email
        "Password" -> KeyboardType.Password
        "Phone" -> KeyboardType.Phone
        "Uri" -> KeyboardType.Uri
        else -> KeyboardType.Text
    }

    val visualTrans = when (props.visualTransformation) {
        "Password" -> PasswordVisualTransformation()
        else -> VisualTransformation.None
    }

    val onValueChange: (String) -> Unit = { newValue ->
        valueStateHost?.onStateChange(newValue)
    }

    val placeholder: @Composable (() -> Unit)? = props.placeholder?.let { placeholderNode ->
        { placeholderNode.ToCompose() }
    }
    val label: @Composable (() -> Unit)? = props.label?.let { labelNode ->
        { labelNode.ToCompose() }
    }
    val leadingIcon: @Composable (() -> Unit)? = props.leadingIcon?.let { iconNode ->
        { iconNode.ToCompose() }
    }
    val trailingIcon: @Composable (() -> Unit)? = props.trailingIcon?.let { iconNode ->
        { iconNode.ToCompose() }
    }
    val supportingText: @Composable (() -> Unit)? = props.supportingText?.let { textNode ->
        { textNode.ToCompose() }
    }
    val prefix: @Composable (() -> Unit)? = props.prefix?.let { prefixNode ->
        { prefixNode.ToCompose() }
    }
    val suffix: @Composable (() -> Unit)? = props.suffix?.let { suffixNode ->
        { suffixNode.ToCompose() }
    }

    if (type == ComposeType.OutlinedTextField) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            readOnly = props.readOnly ?: false,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            prefix = prefix,
            suffix = suffix,
            supportingText = supportingText,
            isError = isError,
            visualTransformation = visualTrans,
            keyboardOptions = KeyboardOptions(keyboardType = kbType),
            singleLine = props.singleLine ?: false,
            maxLines = props.maxLines ?: if (props.singleLine == true) 1 else Int.MAX_VALUE,
        )
    } else {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            readOnly = props.readOnly ?: false,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            prefix = prefix,
            suffix = suffix,
            supportingText = supportingText,
            isError = isError,
            visualTransformation = visualTrans,
            keyboardOptions = KeyboardOptions(keyboardType = kbType),
            singleLine = props.singleLine ?: false,
            maxLines = props.maxLines ?: if (props.singleLine == true) 1 else Int.MAX_VALUE,
        )
    }
}
