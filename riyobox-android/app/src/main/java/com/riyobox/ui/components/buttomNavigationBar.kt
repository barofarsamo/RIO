package com.riyobox.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.riyobox.R

@Composable
fun BottomNavigationBar(
    currentDestination: String?,
    onItemClick: (String) -> Unit
) {
    val items = listOf(
        BottomNavItem(
            route = "home",
            label = "Home",
            icon = R.drawable.ic_home,
            selectedIcon = R.drawable.ic_home_filled
        ),
        BottomNavItem(
            route = "category",
            label = "Category",
            icon = R.drawable.ic_category,
            selectedIcon = R.drawable.ic_category_filled
        ),
        BottomNavItem(
            route = "search",
            label = "Search",
            icon = R.drawable.ic_search,
            selectedIcon = R.drawable.ic_search_filled
        ),
        BottomNavItem(
            route = "downloads",
            label = "Downloads",
            icon = R.drawable.ic_download,
            selectedIcon = R.drawable.ic_download_filled
        ),
        BottomNavItem(
            route = "profile",
            label = "Profile",
            icon = R.drawable.ic_profile,
            selectedIcon = R.drawable.ic_profile_filled
        )
    )
    
    NavigationBar(
        modifier = Modifier
            .height(70.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val selected = currentDestination == item.route
            
            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item.route) },
                icon = {
                    Icon(
                        painter = painterResource(
                            id = if (selected) item.selectedIcon else item.icon
                        ),
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                    unselectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                    unselectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = androidx.compose.material3.MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: Int,
    val selectedIcon: Int
)
