package com.phatnhse.hnthreads.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.phatnhse.hnthreads.ui.screens.DashboardScreen
import com.phatnhse.hnthreads.ui.screens.StoryDetailScreen

@Composable
fun HNNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = HNRoute.Dashboard
    ) {
        composable<HNRoute.Dashboard> {
            DashboardScreen(
                onStoryClick = { storyId ->
                    navController.navigate(HNRoute.StoryDetail(storyId))
                }
            )
        }
        
        composable<HNRoute.StoryDetail> { backStackEntry ->
            val storyDetail = backStackEntry.toRoute<HNRoute.StoryDetail>()
            StoryDetailScreen(
                storyId = storyDetail.storyId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}