package kz.smartcarshare.app.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.data.RentalViewModel
import kz.smartcarshare.app.ui.screens.*
import kz.smartcarshare.app.ui.theme.*
import kz.smartcarshare.app.ui.viewmodel.RegisterViewModel

object Routes {
    const val REGISTER = "register"
    const val HOME = "home"
    const val DETAIL = "detail/{carId}"
    const val BOOKING = "booking"
    const val INSPECT_BEFORE = "inspect_before"
    const val ACTIVE = "active"
    const val INSPECT_AFTER = "inspect_after"
    const val CHAT = "chat"
    const val B2B = "b2b"
    const val PROFILE = "profile"
    const val TRIPS = "trips"
    const val FAVORITES = "favorites"

    fun detail(carId: Int) = "detail/$carId"
}

@Composable
fun SmartCarShareApp() {
    AppNav()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav() {
    val navController = rememberNavController()
    val vm: RentalViewModel = viewModel()
    val registerViewModel: RegisterViewModel = viewModel()

    val registerState by registerViewModel.uiState.collectAsState()
    val currentUser = registerState.currentUser

    val bottomBarRoutes = listOf(Routes.HOME, Routes.CHAT, Routes.ACTIVE, Routes.PROFILE, Routes.TRIPS)

    LaunchedEffect(registerState.lang) {
        vm.setLanguage(registerState.lang)
    }

    LaunchedEffect(vm.currentLang) {
        registerViewModel.setLanguage(vm.currentLang)
    }

    val snackbarHostState = remember { SnackbarHostState() }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val startDest = remember(currentUser, vm.rentalActive) {
        if (vm.rentalActive) Routes.ACTIVE
        else if (currentUser != null) Routes.HOME
        else Routes.REGISTER
    }

    Scaffold(
        containerColor = Navy800,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                FloatingGlassBottomBar(
                    currentRoute = currentRoute,
                    vm = vm,
                    navController = navController
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDest,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.REGISTER) {
                RegisterScreen(
                    onRegisterSuccess = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.REGISTER) { inclusive = true }
                        }
                    },
                    viewModel = registerViewModel
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    vm = vm,
                    currentUser = currentUser,
                    lang = vm.currentLang,
                    onCarClick = { carId ->
                        vm.selectCar(carId)
                        navController.navigate(Routes.detail(carId))
                    },
                    onB2BClick = { navController.navigate(Routes.B2B) }
                )
            }

            composable(
                route = Routes.DETAIL,
                arguments = listOf(navArgument("carId") { type = NavType.IntType })
            ) { backStack ->
                val carId = backStack.arguments?.getInt("carId") ?: return@composable
                LaunchedEffect(carId) {
                    vm.selectCar(carId)
                }
                CarDetailScreen(
                    vm = vm,
                    onBack = { navController.popBackStack() },
                    onRentClick = { navController.navigate(Routes.BOOKING) }
                )
            }

            composable(Routes.BOOKING) {
                BookingScreen(
                    vm = vm,
                    onBack = { navController.popBackStack() },
                    onConfirm = { navController.navigate(Routes.INSPECT_BEFORE) }
                )
            }

            composable(Routes.INSPECT_BEFORE) {
                InspectionBeforeScreen(
                    lang = vm.currentLang,
                    onBack = { navController.popBackStack() },
                    onStartRental = {
                        vm.startRental()
                        navController.navigate(Routes.ACTIVE) {
                            popUpTo(Routes.HOME) { inclusive = false }
                        }
                    }
                )
            }

            composable(Routes.ACTIVE) {
                ActiveRentalScreen(
                    vm = vm,
                    onFinishTrip = { navController.navigate(Routes.INSPECT_AFTER) }
                )
            }

            composable(Routes.INSPECT_AFTER) {
                InspectionAfterScreen(
                    vm = vm,
                    onDone = {
                        vm.finishRental()
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.CHAT) {
                ChatScreen(
                    cars = vm.cars,
                    currentUser = currentUser,
                    lang = vm.currentLang,
                    onCarChosen = { carId ->
                        vm.selectCar(carId)
                        navController.navigate(Routes.detail(carId))
                    }
                )
            }

            composable(Routes.B2B) {
                B2BScreen(
                    lang = vm.currentLang,
                    onBack = { navController.popBackStack() },
                    onPlanChosen = { navController.popBackStack() }
                )
            }

            composable(Routes.PROFILE) {
                ProfileScreen(
                    vm = vm,
                    currentUser = currentUser,
                    lang = vm.currentLang,
                    onChatClick = { navController.navigate(Routes.CHAT) },
                    onB2BClick = { navController.navigate(Routes.B2B) },
                    onMyTripsClick = { navController.navigate(Routes.TRIPS) },
                    onFavoritesClick = { navController.navigate(Routes.FAVORITES) },
                    onLogoutClick = {
                        registerViewModel.logout()
                        navController.navigate(Routes.REGISTER) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onRegisterClick = {
                        navController.navigate(Routes.REGISTER)
                    },
                    onSendPasswordReset = { email ->
                        registerViewModel.onEmailChange(email)
                        registerViewModel.sendPasswordReset()
                    },
                    onUpdateProfile = { fName, lName, phone, email, avatarUrl ->
                        registerViewModel.updateProfile(
                            firstName = fName,
                            lastName = lName,
                            phone = phone,
                            email = email,
                            avatarUrl = avatarUrl
                        )
                    }
                )
            }

            composable(Routes.TRIPS) {
                MyTripsScreen(
                    vm = vm,
                    currentUser = currentUser,
                    lang = vm.currentLang,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Routes.FAVORITES) {
                FavoritesScreen(
                    vm = vm,
                    currentUser = currentUser,
                    lang = vm.currentLang,
                    onBack = { navController.popBackStack() },
                    onCarClick = { carId ->
                        vm.selectCar(carId)
                        navController.navigate(Routes.detail(carId))
                    }
                )
            }
        }
    }
}

@Composable
fun FloatingGlassBottomBar(
    currentRoute: String?,
    vm: RentalViewModel,
    navController: NavHostController
) {
    val items = listOf(
        Triple(Routes.HOME, Icons.Filled.Home, "nav_home"),
        Triple(Routes.CHAT, Icons.Filled.Chat, "nav_chat"),
        Triple(if (vm.rentalActive) Routes.ACTIVE else Routes.TRIPS, Icons.Filled.DirectionsCar, "nav_trips"),
        Triple(Routes.PROFILE, Icons.Filled.Person, "nav_profile")
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(100.dp))
                .background(Color.White.copy(alpha = 0.40f))
                .border(1.2.dp, Color.White.copy(alpha = 0.85f), RoundedCornerShape(100.dp))
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                items.forEach { (route, icon, labelKey) ->
                    val isSelected = (currentRoute == route) ||
                            (route == Routes.TRIPS && currentRoute == Routes.ACTIVE)

                    val activeBg by animateColorAsState(
                        targetValue = if (isSelected) Amber else Color.Transparent,
                        animationSpec = tween(250),
                        label = "activeBg"
                    )

                    Column(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .clickable {
                                navController.navigate(route) { launchSingleTop = true }
                            }
                            .padding(horizontal = 12.dp, vertical = 2.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(width = 44.dp, height = 26.dp)
                                .clip(RoundedCornerShape(100.dp))
                                .background(activeBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else TextMid,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = Strings.get(vm.currentLang, labelKey),
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Amber else TextMid,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
