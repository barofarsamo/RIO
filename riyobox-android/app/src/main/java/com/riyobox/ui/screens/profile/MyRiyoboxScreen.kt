@file:OptIn(ExperimentalMaterial3Api::class)
package com.riyobox.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riyobox.ui.viewmodel.ProfileViewModel
import com.riyobox.ui.viewmodel.User
import com.riyobox.ui.viewmodel.UserStats

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRiyoboxScreen(
    onLogout: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToWatchHistory: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val user by viewModel.user.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val stats by viewModel.stats.collectAsState()
    
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }
    
    val quickActions = listOf(
        MenuItemData(
            icon = Icons.Default.Favorite,
            title = "My Favorites",
            subtitle = "12 movies",
            onClick = onNavigateToFavorites
        ),
        MenuItemData(
            icon = Icons.Default.History,
            title = "Watch History",
            subtitle = "Last watched movies",
            onClick = onNavigateToWatchHistory
        ),
        MenuItemData(
            icon = Icons.Default.Notifications,
            title = "Notifications",
            subtitle = "Manage alerts",
            onClick = {}
        )
    )

    val settingsItems = listOf(
        MenuItemData(
            icon = Icons.Default.Person,
            title = "Account Settings",
            subtitle = "Update profile and password",
            onClick = {}
        ),
        MenuItemData(
            icon = Icons.Default.Settings,
            title = "App Settings",
            subtitle = "Preferences and appearance",
            onClick = onNavigateToSettings
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Riyobox", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        if (isLoading && user == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp)
            ) {
                // Profile Card
                item {
                    ProfileCard(
                        user = user,
                        onEditProfile = { /* TODO: Navigate to edit profile */ }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                // Stats Card
                item {
                    StatsCard(stats = stats)
                    Spacer(modifier = Modifier.height(24.dp))
                }
                
                // Quick Actions
                item {
                    Text(
                        text = "Quick Actions",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                items(quickActions) { action ->
                    MenuItem(
                        icon = action.icon,
                        title = action.title,
                        subtitle = action.subtitle,
                        onClick = action.onClick
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                // Settings Section
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                items(settingsItems) { item ->
                    MenuItem(
                        icon = item.icon,
                        title = item.title,
                        subtitle = item.subtitle,
                        onClick = item.onClick
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                // Logout Button
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { showLogoutDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Logout")
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
        
        // Logout Dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Logout") },
                text = { Text("Are you sure you want to logout?") },
                confirmButton = {
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            onLogout()
                        }
                    ) {
                        Text("Logout")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showLogoutDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileCard(
    user: User?,
    onEditProfile: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user?.name?.firstOrNull()?.toString() ?: "U",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // User Info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = user?.name ?: "User",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = user?.email ?: "user@example.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Subscription Plan
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = user?.subscriptionPlan?.uppercase() ?: "FREE",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
                
                // Edit Button
                IconButton(onClick = onEditProfile) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                }
            }
        }
    }
}

@Composable
fun StatsCard(stats: UserStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Your Stats",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(
                    value = "${stats.watchTime}",
                    label = "Hours Watched",
                    icon = Icons.Default.History
                )
                
                StatItem(
                    value = "${stats.favoriteCount}",
                    label = "Favorites",
                    icon = Icons.Default.Favorite
                )
                
                StatItem(
                    value = "${stats.downloadCount}",
                    label = "Downloads",
                    icon = Icons.Default.Star
                )
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String, icon: ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(56.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun MenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
                
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

// Menu Items
data class MenuItemData(
    val icon: ImageVector,
    val title: String,
    val subtitle: String? = null,
    val onClick: () -> Unit
)
