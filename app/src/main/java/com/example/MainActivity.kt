package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme(darkTheme = true) {
                val currentScreen by viewModel.currentScreen.collectAsState()
                val user by viewModel.currentUser.collectAsState()

                val isShowingWhatsappAlert by viewModel.isShowingWhatsappNotification.collectAsState()
                val lastWhatsappMsg by viewModel.lastSimulatedWhatsappMessage.collectAsState()

                val isShowingCcpWebflow by viewModel.isShowingCcpWebflow.collectAsState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentScreen != "auth") {
                            NavigationBar(
                                containerColor = RShopTheme.Surface,
                                contentColor = RShopTheme.TextSecondary,
                                modifier = Modifier.navigationBarsPadding()
                            ) {
                                NavigationBarItem(
                                    selected = currentScreen == "dashboard" || currentScreen == "admin",
                                    onClick = {
                                        viewModel.currentScreen.value = if (user?.role == "admin") "admin" else "dashboard"
                                    },
                                    icon = {
                                        Icon(
                                            imageVector = if (user?.role == "admin") Icons.Filled.Settings else Icons.Filled.Home,
                                            contentDescription = "لوحة التحكم"
                                        )
                                    },
                                    label = { Text("الرئيسية", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = RShopTheme.Primary,
                                        selectedTextColor = RShopTheme.Primary,
                                        indicatorColor = RShopTheme.CardBg,
                                        unselectedIconColor = RShopTheme.TextSecondary,
                                        unselectedTextColor = RShopTheme.TextSecondary
                                    )
                                )

                                if (user?.role != "admin") {
                                    NavigationBarItem(
                                        selected = currentScreen == "products",
                                        onClick = { viewModel.currentScreen.value = "products" },
                                        icon = { Icon(imageVector = Icons.Filled.ShoppingCart, contentDescription = "المنتجات") },
                                        label = { Text("المنتجات", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = RShopTheme.Primary,
                                            selectedTextColor = RShopTheme.Primary,
                                            indicatorColor = RShopTheme.CardBg,
                                            unselectedIconColor = RShopTheme.TextSecondary,
                                            unselectedTextColor = RShopTheme.TextSecondary
                                        )
                                    )

                                    NavigationBarItem(
                                        selected = currentScreen == "orders",
                                        onClick = { viewModel.currentScreen.value = "orders" },
                                        icon = { Icon(imageVector = Icons.Filled.List, contentDescription = "الطلبات") },
                                        label = { Text("الطلبات", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = RShopTheme.Primary,
                                            selectedTextColor = RShopTheme.Primary,
                                            indicatorColor = RShopTheme.CardBg,
                                            unselectedIconColor = RShopTheme.TextSecondary,
                                            unselectedTextColor = RShopTheme.TextSecondary
                                        )
                                    )

                                    NavigationBarItem(
                                        selected = currentScreen == "designer",
                                        onClick = { viewModel.currentScreen.value = "designer" },
                                        icon = { Icon(imageVector = Icons.Filled.Build, contentDescription = "المصمم") },
                                        label = { Text("المصمم", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                                        colors = NavigationBarItemDefaults.colors(
                                            selectedIconColor = RShopTheme.Primary,
                                            selectedTextColor = RShopTheme.Primary,
                                            indicatorColor = RShopTheme.CardBg,
                                            unselectedIconColor = RShopTheme.TextSecondary,
                                            unselectedTextColor = RShopTheme.TextSecondary
                                        )
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(RShopTheme.Background)
                            .padding(innerPadding)
                    ) {
                        AnimatedContent(
                            targetState = currentScreen,
                            transitionSpec = {
                                fadeIn() togetherWith fadeOut()
                            },
                            label = "ScreenTransition"
                        ) { screen ->
                            when (screen) {
                                "auth" -> AuthScreen(viewModel = viewModel)
                                "dashboard" -> DashboardScreen(viewModel = viewModel)
                                "products" -> ProductsScreen(viewModel = viewModel)
                                "orders" -> OrdersScreen(viewModel = viewModel)
                                "designer" -> DesignerScreen(viewModel = viewModel)
                                "admin" -> AdminScreen(viewModel = viewModel)
                            }
                        }

                        // WhatsApp Simulated overlay alert
                        if (isShowingWhatsappAlert && lastWhatsappMsg != null) {
                            WhatsappNotificationDialog(
                                message = lastWhatsappMsg!!,
                                onDismiss = { viewModel.isShowingWhatsappNotification.value = false }
                            )
                        }

                        // CCP Receipt simulator payment overlay
                        if (isShowingCcpWebflow) {
                            val userPlan = user?.plan ?: "basic"
                            val price = if (userPlan == "pro") 3000.0 else 1000.0
                            CcpPaymentDialog(
                                planName = if (userPlan == "pro") "المتقدمة (Pro)" else "الأساسية",
                                price = price,
                                onConfirm = { viewModel.renewSubscriptionViaCcp(userPlan) },
                                onDismiss = { viewModel.isShowingCcpWebflow.value = false }
                            )
                        }
                    }
                }
            }
        }
    }
}
