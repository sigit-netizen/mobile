package com.gantenginapp.apps.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gantenginapp.apps.model.User
import com.gantenginapp.apps.pages.login.LoginScreen
import com.gantenginapp.apps.pages.register.RegisterScreen
import com.gantenginapp.apps.pages.home.HomeContent
import  com.gantenginapp.apps.pages.allPages.ProfileScreen
import  com.gantenginapp.apps.pages.allPages.BarberDetailScreen
import  com.gantenginapp.apps.pages.allPages.RegisterStoreScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    currentUser: User?,
    onUserLogin: (User) -> Unit,
) {
    var registeredUser by remember { mutableStateOf<User?>(null) }
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                registeredUser = registeredUser,
                onLoginSuccess = { username ->
                    onUserLogin(User(id = "01",username,password = "123",role = "user", noHp = "123", email = "email"),)
                    registeredUser = null
                    navController.navigate(Routes.HOME)
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Routes.REGISTER) }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = { user ->
                    registeredUser = user
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Routes.HOME) {
            HomeContent(
                username = currentUser?.username ?: "Guest",
                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                }
                ,
                onDetailClick = {
                    navController.navigate(Routes.DETAIL_STORE)
                }

            )
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                onBackClick = {navController.popBackStack()},
                onRegistStoreClick = {navController.navigate(Routes.REGIST_STORE)}
            )
        }

        composable(Routes.DETAIL_STORE) {
            BarberDetailScreen()
        }

        composable (Routes.REGIST_STORE){
            RegisterStoreScreen()
        }
    }
}
