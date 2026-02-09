package com.example.qm_app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amap.api.services.core.PoiItemV2
import com.example.qm_app.common.QmIcons
import com.example.qm_app.remember.rememberPopBackStackListener
import com.example.qm_app.router.Route
import com.example.qm_app.router.Router
import com.example.qm_app.ui.theme.gray

@Composable
fun FormItemLocation(
    label: String,
    value: String,
    bordered: Boolean = true,
    onChange: (String) -> Unit,
) {
    rememberPopBackStackListener<PoiItemV2>(source = Route.MapLocationScreen.route) { data ->
        val address =
            """${data.provinceName}${data.cityName}${data.adName}${data.snippet}"""

        onChange(address)
    }

    FormItemInput(
        label = label,
        value = value,
        allowClear = false,
        bordered = bordered,
        keyboardType = KeyboardType.Text,
        onChange = { input -> onChange(input) },
        suffix = {
            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clickable(
                        indication = null,
                        interactionSource = null,
                        onClick = {
                            Router.navigate(Route.MapLocationScreen.route)
                        },
                    )
            ) {
                QmIcon(icon = QmIcons.Stroke.Location, tint = gray, size = 22.dp)
                Text(
                    text = "定位",
                    fontSize = 11.sp,
                    lineHeight = 11.sp,
                    modifier = Modifier.padding(top = 0.dp),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    )
}