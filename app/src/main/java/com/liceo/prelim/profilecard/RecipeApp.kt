package com.liceo.prelim.profilecard

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun RecipeApp() {
    val navController = rememberNavController()
    // ONE ViewModel created here and shared by BOTH screens,
    // so edits on Screen 2 are visible on Screen 1.
    val viewModel: DishViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "dish_list" // Screen 1 is the start destination
    ) {
        // ----- SCREEN 1 -----
        composable(route = "dish_list") {
            DishListScreen(
                viewModel = viewModel,
                onDishClick = { dishId ->
                    // TODO 6 (15 pts): navigate to the detail screen for this dish.
                    // Hint: the route below expects the id at the end, like "dish_detail/7".
                    // Kotlin string templates: "dish_detail/$dishId"
                    navController.navigate("dish_detail/$dishId")
                }
            )
        }

        // ----- SCREEN 2 ----- (GIVEN, do not change)
        composable(
            route = "dish_detail/{dishId}",
            arguments = listOf(navArgument("dishId") { type = NavType.IntType })
        ) { backStackEntry ->
            val dishId = backStackEntry.arguments?.getInt("dishId") ?: 0
            DishDetailScreen(
                dishId = dishId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
