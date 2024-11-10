package com.project.lolchampions



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TeamTitleBox(teamName: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
            .padding(8.dp)
    ) {
        Text(
            text = teamName,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

@Composable
fun OptionsButtonsRow(areButtonsVisible: Boolean, onToggleVisibility: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (areButtonsVisible) "Ocultar opções" else "Mostrar opções",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 8.dp)
        )
        IconButton(onClick = onToggleVisibility) {
            Icon(
                imageVector = if (areButtonsVisible) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = "Mostrar opções"
            )
        }
    }
}

@Composable
fun TeamActionButtons(
    onReload: () -> Unit,
    onShare: () -> Unit,
    onSave: () -> Unit
) {
    Button(onClick = onReload, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF)), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = "Recarregar")
    }
    Button(onClick = onShare, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF)), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = "Compartilhar")
    }
    Button(onClick = onSave, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E90FF)), modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = "Salvar")
    }
}
