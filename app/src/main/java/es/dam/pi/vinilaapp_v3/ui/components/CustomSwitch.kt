package es.dam.pi.vinilaapp_v3.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color(0xFFFFA726), // Naranja
            uncheckedThumbColor = Color(0xFF8B0000), // Rojo Oscuro
            checkedTrackColor = Color(0xFFFFA500), // Naranja más oscuro
            uncheckedTrackColor = Color(0xFFC70039) // Rojo
        ),
        modifier = modifier.size(36.dp)
    )
}
