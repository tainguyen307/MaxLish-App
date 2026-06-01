package com.example.maxlish.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.maxlish.data.repository.FirebaseAuthRepository
import com.example.maxlish.data.repository.FirebaseLearningRepository
import com.example.maxlish.data.repository.FirebaseProgressRepository
import com.example.maxlish.data.repository.FirebaseVocabularyRepository
import com.example.maxlish.ui.screen.learning.LearningRoute
import com.example.maxlish.ui.screen.learning.LearningViewModel
import com.example.maxlish.ui.screen.home.HomeRoute
import com.example.maxlish.ui.screen.login.LoginScreen
import com.example.maxlish.ui.screen.profile.ProfileRoute
import com.example.maxlish.ui.screen.progress.ProgressScreen
import com.example.maxlish.ui.screen.progress.ProgressViewModel
import com.example.maxlish.ui.screen.register.RegisterScreen
import com.example.maxlish.ui.screen.vocabulary.set.create.VocabularySetCreateRoute
import com.example.maxlish.ui.screen.vocabulary.set.create.VocabularySetCreateViewModel
import com.example.maxlish.ui.screen.vocabulary.set.detail.VocabularySetDetailRoute
import com.example.maxlish.ui.screen.vocabulary.set.detail.VocabularySetDetailViewModel
import com.example.maxlish.ui.screen.vocabulary.set.list.VocabularySetListRoute
import com.example.maxlish.ui.screen.vocabulary.word.create.VocabularyWordCreateRoute
import com.example.maxlish.ui.screen.vocabulary.word.create.VocabularyWordCreateViewModel
import com.example.maxlish.ui.screen.vocabulary.word.detail.VocabularyWordDetailRoute
import com.example.maxlish.ui.screen.vocabulary.word.detail.VocabularyWordDetailViewModel
import com.example.maxlish.ui.screen.vocabulary.word.list.VocabularyWordListRoute
import com.example.maxlish.ui.screen.vocabulary.word.list.VocabularyWordListViewModel
import com.google.firebase.firestore.FirebaseFirestore

object AppDestinations {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val PROFILE = "profile"
    const val HOME = "home"
    const val PROGRESS = "progress"
    const val VOCABULARY = "vocabulary"

    const val VOCABULARY_CREATE = "vocabulary_create"

    const val VOCABULARY_DETAIL = "vocabulary_set_detail/{setId}"
    const val VOCABULARY_EDIT = "vocabulary_set_edit/{setId}"
    const val VOCABULARY_WORD_LIST = "vocabulary_word_list/{setId}"
    const val VOCABULARY_WORD_CREATE = "vocabulary_word_create/{setId}?wordId={wordId}"
    const val VOCABULARY_WORD_DETAIL = "vocabulary_word_detail/{setId}/{wordId}"

    fun vocabularyWordList(setId: String) =
        "vocabulary_word_list/$setId"
    fun vocabularyWordCreate(
        setId: String,
        wordId: String? = null
    ): String {

        return if (wordId == null) {
            "vocabulary_word_create/$setId"
        } else {
            "vocabulary_word_create/$setId?wordId=$wordId"
        }
    }
    fun vocabularyWordDetail(
        setId: String,
        wordId: String
    ): String =
        "vocabulary_word_detail/$setId/$wordId"
    fun vocabularyDetail(setId: String): String {
        return "vocabulary_set_detail/$setId"
    }
    fun vocabularyEdit(
        setId: String
    ) = "vocabulary_set_edit/$setId"

    const val LEARN = "learn/{setId}?mode={mode}"
    fun learn(setId: String, mode: String = "all") =
        "learn/$setId?mode=$mode"
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {

    val firestore = FirebaseFirestore.getInstance()

    val authRepository = FirebaseAuthRepository()

    val vocabularyRepository =
        FirebaseVocabularyRepository(firestore)

    val startDestination =
        if (authRepository.getCurrentUser() != null) {
            AppDestinations.HOME
        } else {
            AppDestinations.LOGIN
        }

    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute =
        backStackEntry?.destination?.route

    val showBottomBar =
        currentRoute == AppDestinations.HOME ||
                currentRoute == AppDestinations.PROFILE ||
                currentRoute == AppDestinations.PROGRESS ||
                currentRoute == AppDestinations.VOCABULARY

    Scaffold(
        bottomBar = {

            if (showBottomBar) {

                NavigationBar {

                    NavigationBarItem(
                        selected = currentRoute == AppDestinations.HOME,
                        onClick = {
                            navController.navigate(
                                AppDestinations.HOME
                            ) {
                                launchSingleTop = true

                                popUpTo(AppDestinations.HOME) {
                                    inclusive = false
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text("Home")
                        }
                    )

                    NavigationBarItem(
                        selected = currentRoute == AppDestinations.VOCABULARY,
                        onClick = {
                            navController.navigate(
                                AppDestinations.VOCABULARY
                            ) {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Book,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text("Vocabulary")
                        }
                    )

                    NavigationBarItem(
                        selected = currentRoute == AppDestinations.PROGRESS,
                        onClick = {
                            navController.navigate(
                                AppDestinations.PROGRESS
                            ) {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text("Progress")
                        }
                    )

                    NavigationBarItem(
                        selected = currentRoute == AppDestinations.PROFILE,
                        onClick = {
                            navController.navigate(
                                AppDestinations.PROFILE
                            ) {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text("Profile")
                        }
                    )
                }
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues)
        ) {

            composable(AppDestinations.LOGIN) {

                LoginScreen(
                    onLoginSuccess = {

                        navController.navigate(
                            AppDestinations.HOME
                        ) {
                            popUpTo(AppDestinations.LOGIN) {
                                inclusive = true
                            }
                        }
                    },

                    onNavigateToRegister = {
                        navController.navigate(
                            AppDestinations.REGISTER
                        )
                    }
                )
            }

            composable(AppDestinations.REGISTER) {

                RegisterScreen(
                    onRegisterSuccess = {

                        navController.navigate(
                            AppDestinations.PROFILE
                        ) {
                            popUpTo(AppDestinations.REGISTER) {
                                inclusive = true
                            }
                        }
                    },

                    onNavigateToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(AppDestinations.PROFILE) {

                ProfileRoute(
                    onSaveSuccess = {

                        navController.navigate(
                            AppDestinations.HOME
                        ) {
                            popUpTo(AppDestinations.LOGIN) {
                                inclusive = true
                            }
                        }
                    },

                    onLogout = {

                        navController.navigate(
                            AppDestinations.LOGIN
                        ) {
                            popUpTo(0) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(AppDestinations.HOME) {

                HomeRoute(
                    onNavigateToProfile = {
                        navController.navigate(
                            AppDestinations.PROFILE
                        )
                    },

                    onNavigateToProgress = {
                        navController.navigate(
                            AppDestinations.PROGRESS
                        )
                    },

                    onNavigateToVocabularySetDetail = { setId ->
                        navController.navigate(AppDestinations.vocabularyDetail(setId))
                    },

                    onNavigateToLearn = { setId, mode ->
                        navController.navigate(AppDestinations.learn(setId, mode))
                    }
                )
            }

            composable(AppDestinations.PROGRESS) {

                val progressRepository =
                    FirebaseProgressRepository(firestore)

                val viewModel: ProgressViewModel =
                    viewModel(
                        factory = object : ViewModelProvider.Factory {

                            override fun <T : ViewModel> create(
                                modelClass: Class<T>
                            ): T {

                                @Suppress("UNCHECKED_CAST")

                                return ProgressViewModel(
                                    authRepository,
                                    progressRepository
                                ) as T
                            }
                        }
                    )

                ProgressScreen(
                    viewModel = viewModel
                )
            }

            composable(AppDestinations.VOCABULARY) {

                VocabularySetListRoute(

                    onNavigateToDetail = { setId ->

                        navController.navigate(
                            AppDestinations.vocabularyDetail(setId)
                        )
                    },

                    onNavigateToCreate = {

                        navController.navigate(
                            AppDestinations.VOCABULARY_CREATE
                        )
                    },
                    onNavigateToEdit = { setId ->
                        navController.navigate(
                            AppDestinations.vocabularyEdit(setId)
                        )
                    }
                )
            }

            composable(
                AppDestinations.VOCABULARY_CREATE
            ) {

                val currentUser =
                    authRepository.getCurrentUser()

                val viewModel:
                        VocabularySetCreateViewModel =
                    viewModel(
                        factory = object : ViewModelProvider.Factory {

                            override fun <T : ViewModel> create(
                                modelClass: Class<T>
                            ): T {

                                @Suppress("UNCHECKED_CAST")

                                return VocabularySetCreateViewModel(
                                    repository = vocabularyRepository,
                                    ownerId = currentUser?.uid ?: ""
                                ) as T
                            }
                        }
                    )

                VocabularySetCreateRoute(
                    viewModel = viewModel,
                    onSuccess = {
                        navController.popBackStack()
                        navController.navigate(AppDestinations.VOCABULARY) {
                            popUpTo(AppDestinations.VOCABULARY) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(
                route = AppDestinations.VOCABULARY_EDIT
            ) { backStackEntry ->

                val setId =
                    backStackEntry.arguments
                        ?.getString("setId")
                        ?: ""

                val currentUser =
                    authRepository.getCurrentUser()

                val viewModel: VocabularySetCreateViewModel =
                    viewModel(
                        factory = object : ViewModelProvider.Factory {

                            override fun <T : ViewModel> create(
                                modelClass: Class<T>
                            ): T {

                                @Suppress("UNCHECKED_CAST")

                                return VocabularySetCreateViewModel(
                                    repository = vocabularyRepository,
                                    ownerId = currentUser?.uid ?: ""
                                ) as T
                            }
                        }
                    )

                VocabularySetCreateRoute(
                    setId = setId,
                    viewModel = viewModel,
                    onSuccess = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                AppDestinations.VOCABULARY_DETAIL
            ) { backStackEntry ->

                val setId =
                    backStackEntry.arguments?.getString("setId")

                if (setId.isNullOrBlank()) {
                    navController.popBackStack()
                    return@composable
                }

                val viewModel: VocabularySetDetailViewModel =
                    viewModel(
                        factory = object : ViewModelProvider.Factory {

                            override fun <T : ViewModel> create(modelClass: Class<T>): T {

                                @Suppress("UNCHECKED_CAST")
                                return VocabularySetDetailViewModel(
                                    repository = vocabularyRepository
                                ) as T
                            }
                        }
                    )

                VocabularySetDetailRoute(
                    setId = setId,
                    viewModel = viewModel,
                    onNavigateToWordList = { id ->

                        navController.navigate(
                            AppDestinations.vocabularyWordList(id)
                        )
                    },

                    onNavigateToAddWord = { id ->

                        navController.navigate(
                            AppDestinations.vocabularyWordCreate(id)
                        ) {
                            launchSingleTop = true
                        }
                    },

                    onNavigateToLearn = { id ->
                        navController.navigate(AppDestinations.learn(id, "all"))
                    },

                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = AppDestinations.VOCABULARY_WORD_LIST
            ) { backStackEntry ->

                val setId =
                    backStackEntry.arguments?.getString("setId") ?: ""

                val viewModel: VocabularyWordListViewModel =
                    viewModel(
                        factory = object : ViewModelProvider.Factory {

                            override fun <T : ViewModel> create(modelClass: Class<T>): T {

                                return VocabularyWordListViewModel(
                                    repository = vocabularyRepository,
                                    setId = setId
                                ) as T
                            }
                        }
                    )

                VocabularyWordListRoute(

                    viewModel = viewModel,

                    onNavigateToDetail = { wordId ->

                        navController.navigate(
                            AppDestinations.vocabularyWordDetail(setId, wordId)
                        )
                    },

                    onNavigateToCreate = {

                        navController.navigate(
                            AppDestinations.vocabularyWordCreate(setId)
                        )
                    }
                )
            }

            composable(
                route = AppDestinations.VOCABULARY_WORD_CREATE,
                arguments = listOf(
                    navArgument("setId") { defaultValue = "" },
                    navArgument("wordId") {
                        defaultValue = null
                        nullable = true
                    }
                )

            ) { backStackEntry ->

                val setId =
                    backStackEntry.arguments?.getString("setId") ?: ""

                val wordId =
                    backStackEntry.arguments?.getString("wordId")

                val viewModel: VocabularyWordCreateViewModel =
                    viewModel(
                        factory = object : ViewModelProvider.Factory {

                            override fun <T : ViewModel> create(modelClass: Class<T>): T {

                                return VocabularyWordCreateViewModel(
                                    repository = vocabularyRepository,
                                    setId = setId,
                                    wordId = wordId
                                ) as T
                            }
                        }
                    )

                VocabularyWordCreateRoute(

                    setId = setId,
                    wordId = wordId,

                    viewModel = viewModel,

                    onSuccess = {
                        navController.navigate(
                            AppDestinations.vocabularyWordList(setId)
                        ) {
                            popUpTo(AppDestinations.VOCABULARY_WORD_LIST) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    },

                    onBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = AppDestinations.VOCABULARY_WORD_DETAIL
            ) { backStackEntry ->

                val setId =
                    backStackEntry.arguments?.getString("setId") ?: ""

                val wordId =
                    backStackEntry.arguments?.getString("wordId") ?: ""

                val viewModel: VocabularyWordDetailViewModel =
                    viewModel(
                        factory = object : ViewModelProvider.Factory {

                            override fun <T : ViewModel> create(modelClass: Class<T>): T {

                                return VocabularyWordDetailViewModel(
                                    repository = vocabularyRepository,
                                    setId = setId,
                                    wordId = wordId
                                ) as T
                            }
                        }
                    )

                VocabularyWordDetailRoute(

                    setId = setId,
                    wordId = wordId,

                    viewModel = viewModel,

                    onBack = {
                        navController.popBackStack()
                    },

                    onNavigateToEdit = { sId, wId ->

                        navController.navigate(
                            AppDestinations.vocabularyWordCreate(sId, wId)
                        )
                    }
                )
            }

            composable(
                route = AppDestinations.LEARN,
                arguments = listOf(
                    navArgument("setId") { defaultValue = "" },
                    navArgument("mode") { defaultValue = "all" }
                )
            ) { backStackEntry ->
                val setId = backStackEntry.arguments?.getString("setId") ?: ""
                val mode = backStackEntry.arguments?.getString("mode") ?: "all"

                val learningRepository = FirebaseLearningRepository(firestore)
                val progressRepository = FirebaseProgressRepository(firestore)

                val viewModel: LearningViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            @Suppress("UNCHECKED_CAST")
                            return LearningViewModel(
                                setId = setId,
                                mode = mode,
                                authRepository = authRepository,
                                learningRepository = learningRepository,
                                progressRepository = progressRepository,
                                vocabularyRepository = vocabularyRepository
                            ) as T
                        }
                    }
                )

                LearningRoute(
                    viewModel = viewModel,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}