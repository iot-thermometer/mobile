package com.pawlowski.temperaturemanager.ui.screens.bottomSheets.share

import androidx.lifecycle.viewModelScope
import com.pawlowski.temperaturemanager.BaseMviViewModel
import com.pawlowski.temperaturemanager.domain.Resource
import com.pawlowski.temperaturemanager.domain.RetrySharedFlow
import com.pawlowski.temperaturemanager.domain.models.Member
import com.pawlowski.temperaturemanager.domain.resourceFlowWithRetrying
import com.pawlowski.temperaturemanager.domain.useCase.devices.DeviceSelectionUseCase
import com.pawlowski.temperaturemanager.domain.useCase.members.AddDeviceMemberUseCase
import com.pawlowski.temperaturemanager.domain.useCase.members.DeleteDeviceMemberUseCase
import com.pawlowski.temperaturemanager.domain.useCase.members.GetDeviceMembersUseCase
import com.pawlowski.temperaturemanager.ui.screens.bottomSheets.share.ShareBottomSheetState.ContentState
import com.pawlowski.temperaturemanager.ui.screens.bottomSheets.share.ShareBottomSheetState.ContentState.MembersList.AddMemberViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ShareBottomSheetViewModel
    @Inject
    constructor(
        deviceSelectionUseCase: DeviceSelectionUseCase,
        private val getDeviceMembersUseCase: GetDeviceMembersUseCase,
        private val addDeviceMemberUseCase: AddDeviceMemberUseCase,
        private val deleteDeviceMemberUseCase: DeleteDeviceMemberUseCase,
    ) :
    BaseMviViewModel<ShareBottomSheetState, ShareBottomSheetEvent, Nothing>(
            initialState =
                ShareBottomSheetState(
                    searchText = "",
                    contentState = ContentState.Loading,
                ),
        ) {
        private val deviceId = deviceSelectionUseCase.getSelectedDeviceId()!!

        private val currentMembers = MutableStateFlow<List<Member>?>(null)

        private val retrySharedFlow = RetrySharedFlow()

        override fun initialised() {
            viewModelScope.launch {
                resourceFlowWithRetrying(retrySharedFlow = retrySharedFlow) {
                    fetchMembers()
                }.collect {
                    when (it) {
                        is Resource.Success -> {
                            updateState {
                                copy(contentState = ContentState.MembersList(membersState = emptyList()))
                            }
                        }

                        is Resource.Error -> {
                            updateState {
                                copy(contentState = ContentState.Error)
                            }
                        }

                        is Resource.Loading -> {
                            updateState {
                                copy(contentState = ContentState.Loading)
                            }
                        }
                    }
                }
            }

            combine(
                searchTextFlow(),
                currentMembers,
                isLoadingOrErrorFlow(),
            ) { searchText, currentMembers, isLoadingOrError ->
                if (currentMembers == null || isLoadingOrError) {
                    null
                } else {
                    searchText to currentMembers
                }
            }.filterNotNull()
                .map { (searchText, currentMembers) ->
                    val matchingMembers =
                        currentMembers.filter {
                            searchText.isEmpty() || it.email.contains(searchText)
                        }.map {
                            AddMemberViewState.AlreadyAdded(
                                member = it,
                            )
                        }
                    val memberToAdd =
                        if (searchText.isNotBlank() && !currentMembers.any { it.email == searchText }) {
                            AddMemberViewState.NotAdded(
                                email = searchText,
                            )
                        } else {
                            null
                        }

                    (memberToAdd?.let { listOf(it) } ?: emptyList()) + matchingMembers
                }.onEach { newMembers ->
                    updateState {
                        copy(contentState = ContentState.MembersList(membersState = newMembers))
                    }
                }.launchIn(viewModelScope)
        }

        private suspend fun fetchMembers() {
            getDeviceMembersUseCase(deviceId).also {
                currentMembers.value = it
            }
        }

        private fun searchTextFlow(): Flow<String> =
            stateFlow.map {
                it.searchText.trim()
            }.distinctUntilChanged()

        private fun isLoadingOrErrorFlow(): Flow<Boolean> =
            stateFlow
                .map { it.contentState is ContentState.Loading || it.contentState is ContentState.Error }
                .distinctUntilChanged()

        override fun onNewEvent(event: ShareBottomSheetEvent) {
            when (event) {
                is ShareBottomSheetEvent.SearchTextChange -> {
                    if (event.newText.length < 100) {
                        updateState {
                            copy(searchText = event.newText.lowercase())
                        }
                    }
                }

                is ShareBottomSheetEvent.InviteClick -> {
                    if (actualState.contentState is ContentState.MembersList) {
                        val email = actualState.searchText
                        updateState {
                            copy(contentState = ContentState.Loading)
                        }
                        viewModelScope.launch {
                            resourceFlowWithRetrying(retrySharedFlow = retrySharedFlow) {
                                addDeviceMemberUseCase(
                                    deviceId = deviceId,
                                    email = email,
                                )
                                fetchMembers()
                            }.collect {
                                when (it) {
                                    is Resource.Success -> {
                                        updateState {
                                            copy(
                                                searchText = "",
                                                contentState =
                                                    ContentState.MembersList(
                                                        membersState = emptyList(),
                                                    ),
                                            )
                                        }
                                    }

                                    is Resource.Error -> {
                                        updateState {
                                            copy(
                                                contentState = ContentState.Error,
                                            )
                                        }
                                    }

                                    is Resource.Loading -> {
                                        updateState {
                                            copy(contentState = ContentState.Loading)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                is ShareBottomSheetEvent.DeleteClick -> {
                    if (actualState.contentState is ContentState.MembersList) {
                        updateState {
                            copy(contentState = ContentState.Loading)
                        }
                        viewModelScope.launch {
                            resourceFlowWithRetrying(retrySharedFlow = retrySharedFlow) {
                                deleteDeviceMemberUseCase(
                                    deviceId = deviceId,
                                    userId = event.userId,
                                )
                                fetchMembers()
                            }.collect {
                                when (it) {
                                    is Resource.Success -> {
                                        updateState {
                                            copy(
                                                contentState =
                                                    ContentState.MembersList(
                                                        membersState = emptyList(),
                                                    ),
                                            )
                                        }
                                    }

                                    is Resource.Error ->
                                        updateState {
                                            copy(
                                                contentState = ContentState.Error,
                                            )
                                        }

                                    Resource.Loading -> {
                                        updateState {
                                            copy(contentState = ContentState.Loading)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                ShareBottomSheetEvent.RetryClick -> {
                    retrySharedFlow.sendRetryEvent()
                }
            }
        }
    }
