package com.pawlowski.temperaturemanager.ui.screens.bottomSheets.share

import com.pawlowski.temperaturemanager.domain.models.Member

data class ShareBottomSheetState(
    val searchText: String,
    val contentState: ContentState,
) {
    sealed interface ContentState {
        object Loading : ContentState

        object Error : ContentState

        data class MembersList(val membersState: List<AddMemberViewState>) : ContentState {
            sealed interface AddMemberViewState {
                val email: String

                data class AlreadyAdded(val member: Member) : AddMemberViewState {
                    override val email: String
                        get() = member.email
                }

                data class NotAdded(override val email: String) : AddMemberViewState
            }
        }
    }
}

sealed interface ShareBottomSheetEvent {
    data class SearchTextChange(val newText: String) : ShareBottomSheetEvent

    object InviteClick : ShareBottomSheetEvent

    data class DeleteClick(val userId: Long) : ShareBottomSheetEvent

    object RetryClick : ShareBottomSheetEvent
}
