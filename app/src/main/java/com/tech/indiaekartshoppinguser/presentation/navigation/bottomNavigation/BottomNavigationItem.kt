package com.tech.indiaekartshoppinguser.presentation.navigation.bottomNavigation

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavigationItem(
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector,
    val text: String
)