package kz.smartcarshare.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
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

    fun detail(carId: Int) = "detail/$carId"
}

private val bottomBarRoutes = setOf(Routes.HOME, Routes.CHAT, Routes.PROFILE)

@Composable
fun SmartCarShareApp() {
    val navController = rememberNavController()
    val vm: RentalViewModel = viewModel()
    val registerViewModel: RegisterViewModel = viewModel()
    val registerState by registerViewModel.uiState.collectAsState()
    val currentUser = registerState.currentUser

    LaunchedEffect(currentUser) {
        if (!currentUser?.email.isNullOrBlank()) {
            vm.loadUserData(currentUser.email)
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val navColors = NavigationBarItemDefaults.colors(
        selectedIconColor = AmberOnDark,
        selectedTextColor = Amber,
        indicatorColor = Amber,
        unselectedIconColor = TextLow,
        unselectedTextColor = TextLow
    )

    Scaffold(
        containerColor = Navy800,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                NavigationBar(containerColor = Surface1) {
                    NavigationBarItem(
                        selected = currentRoute == Routes.HOME,
                        onClick = { navController.navigate(Routes.HOME) { launchSingleTop = true } },
                        icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                        label = { Text(Strings.get(vm.currentLang, "nav_home")) },
                        colors = navColors
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.CHAT,
                        onClick = { navController.navigate(Routes.CHAT) { launchSingleTop = true } },
                        icon = { Icon(Icons.Filled.Chat, contentDescription = null) },
                        label = { Text(Strings.get(vm.currentLang, "nav_chat")) },
                        colors = navColors
                    )
                    NavigationBarItem(
                        selected = false,
                        onClick = {
                            if (vm.rentalActive) {
                                navController.navigate(Routes.ACTIVE) { launchSingleTop = true }
                            } else {
                                navController.navigate(Routes.TRIPS) { launchSingleTop = true }
                            }
                        },
                        icon = { Icon(Icons.Filled.DirectionsCar, contentDescription = null) },
                        label = { Text(Strings.get(vm.currentLang, "nav_trips")) },
                        colors = navColors
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.PROFILE,
                        onClick = { navController.navigate(Routes.PROFILE) { launchSingleTop = true } },
                        icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        label = { Text(Strings.get(vm.currentLang, "nav_profile")) },
                        colors = navColors
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = if (currentUser != null) Routes.HOME else Routes.REGISTER,
            modifier = Modifier.padding(innerPadding)
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
                    onCarClick = { id ->
                        vm.selectCar(id)
                        navController.navigate(Routes.detail(id))
                    },
                    onB2BClick = { navController.navigate(Routes.B2B) }
                )
            }
            composable(
                route = Routes.DETAIL,
                arguments = listOf(navArgument("carId") { type = NavType.IntType })
            ) {
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
                    onBack = { navController.popBackStack() },
                    onStartRental = {
                        vm.startRental(currentUser?.email ?: "guest_user")
                        navController.navigate(Routes.ACTIVE) {
                            popUpTo(Routes.HOME)
                        }
                    }
                )
            }
            composable(Routes.ACTIVE) {
                ActiveRentalScreen(
                    vm = vm,
                    onFinishTrip = {
                        vm.stopTripAccrual()
                        navController.navigate(Routes.INSPECT_AFTER)
                    }
                )
            }
            composable(Routes.INSPECT_AFTER) {
                InspectionAfterScreen(
                    vm = vm,
                    onDone = {
                        val paid = vm.liveAccruedPrice()
                        vm.finishRental()
                        scope.launch {
                            snackbarHostState.showSnackbar("Автоплатеж арқылы $paid ₸ есептен шығарылды")
                        }
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
                    onCarChosen = { id ->
                        vm.selectCar(id)
                        navController.navigate(Routes.detail(id))
                    }
                )
            }
            composable(Routes.B2B) {
                B2BScreen(
                    onBack = { navController.popBackStack() },
                    onPlanChosen = { plan ->
                        scope.launch { snackbarHostState.showSnackbar("Өтінім қабылданды: «$plan» тарифі") }
                    }
                )
            }
            composable(Routes.TRIPS) {
                MyTripsScreen(
                    vm = vm,
                    lang = vm.currentLang,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Routes.PROFILE) {
                ProfileScreen(
                    vm = vm,
                    currentUser = currentUser,
                    lang = vm.currentLang,
                    onToggleLang = { vm.toggleLanguage() },
                    onChatClick = { navController.navigate(Routes.CHAT) { launchSingleTop = true } },
                    onB2BClick = { navController.navigate(Routes.B2B) },
                    onMyTripsClick = { navController.navigate(Routes.TRIPS) },
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
                    onUpdateProfile = { firstName, lastName, phone, email ->
                        registerViewModel.updateProfile(firstName, lastName, phone, email)
                    }
                )
            }
        }
    }
}
