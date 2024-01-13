package com.pawlowski.temperaturemanager.ui.screens.bottomSheets.share

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pawlowski.temperaturemanager.domain.models.OwnershipType
import com.pawlowski.temperaturemanager.ui.screens.bottomSheets.BaseBottomSheet
import com.pawlowski.temperaturemanager.ui.screens.bottomSheets.share.ShareBottomSheetState.ContentState
import com.pawlowski.temperaturemanager.ui.screens.bottomSheets.share.ShareBottomSheetState.ContentState.MembersList.AddMemberViewState
import com.pawlowski.temperaturemanager.ui.screens.readings.HorizontalDivider

@Composable
fun ShareBottomSheet(
    show: Boolean,
    onDismiss: () -> Unit,
) {
    BaseBottomSheet(show = show, onDismiss = onDismiss) {
        val viewModel = hiltViewModel<ShareBottomSheetViewModel>()
        ShareBottomSheetContent(
            state = viewModel.stateFlow.collectAsState().value,
            onEvent = viewModel::onNewEvent,
        )
    }
}

@Composable
private fun ShareBottomSheetContent(
    state: ShareBottomSheetState,
    onEvent: (ShareBottomSheetEvent) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 16.dp),
        modifier =
            Modifier
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
    ) {
        Text(
            text = "Udostępnij urządzenie",
            style =
                TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF000000),
                ),
            textAlign = TextAlign.Center,
        )

        Text(
            text = "Gdy urządzenie jest udostępnione, inni dodani użytkownicy mogą przeglądać pomiary",
            style =
                TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500),
                    color = Color(0xFF505050),
                    textAlign = TextAlign.Center,
                ),
        )

        OutlinedTextField(
            value = state.searchText,
            onValueChange = { onEvent(ShareBottomSheetEvent.SearchTextChange(it)) },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = null)
            },
            trailingIcon = {
                if (state.searchText.isNotEmpty()) {
                    IconButton(onClick = { onEvent(ShareBottomSheetEvent.SearchTextChange("")) }) {
                        Icon(imageVector = Icons.Filled.Clear, contentDescription = null)
                    }
                }
            },
            label = {
                Text("Wpisz e-mail")
            },
        )

        val amIOwner =
            remember(state.contentState) {
                if (state.contentState is ContentState.MembersList) {
                    state.contentState.membersState.filterIsInstance<AddMemberViewState.AlreadyAdded>()
                        .firstOrNull { it.member.isMe }?.member?.ownership == OwnershipType.OWNER
                } else {
                    false
                }
            }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        ) {
            when (state.contentState) {
                is ContentState.MembersList -> {
                    items(state.contentState.membersState) { memberState ->
                        Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
                            MemberItem(
                                memberState = memberState,
                                amIOwner = amIOwner,
                                onInviteClick = {
                                    onEvent(ShareBottomSheetEvent.InviteClick)
                                },
                                onDeleteClick = {
                                    onEvent(ShareBottomSheetEvent.DeleteClick(userId = it))
                                },
                            )
                            if (memberState is AddMemberViewState.NotAdded) {
                                HorizontalDivider()
                            }
                        }
                    }
                }

                ContentState.Error -> {}
                ContentState.Loading -> {
                    item {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun MemberItem(
    memberState: AddMemberViewState,
    amIOwner: Boolean,
    onInviteClick: () -> Unit,
    onDeleteClick: (Long) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(all = 10.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .size(36.dp)
                    .background(
                        color = Color(0xFF7992FC),
                        shape = CircleShape,
                    ),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Filled.Mail,
                contentDescription = null,
                tint = Color.White,
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(space = 4.dp),
            modifier = Modifier.weight(weight = 1f, fill = true),
        ) {
            Text(
                text = memberState.email,
                color = Color(0xFF2C2C2C),
                fontSize = 13.sp,
                fontWeight = FontWeight(500),
                textAlign = TextAlign.Center,
            )
            Text(
                text =
                    when (memberState) {
                        is AddMemberViewState.NotAdded -> "Wyślij zaproszenie"
                        is AddMemberViewState.AlreadyAdded -> "Już udostępnione"
                    },
                color =
                    when (memberState) {
                        is AddMemberViewState.NotAdded -> Color(0xFF68C06C)
                        is AddMemberViewState.AlreadyAdded -> Color(0xFF7992FC)
                    },
                fontSize = 10.sp,
                fontWeight = FontWeight(600),
                textAlign = TextAlign.Center,
            )
        }

        when (memberState) {
            is AddMemberViewState.NotAdded -> {
                Button(onClick = onInviteClick) {
                    Text(text = "Zaproś", fontSize = 13.sp)
                }
            }

            is AddMemberViewState.AlreadyAdded -> {
                if (amIOwner && !memberState.member.isMe) {
                    Button(
                        onClick = { onDeleteClick(memberState.member.userId) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    ) {
                        Text(text = "Usuń", fontSize = 13.sp)
                    }
                } else if (memberState.member.isMe) {
                    Text(text = "Me (${memberState.member.ownership.toOwnershipString()})")
                } else {
                    Text(text = memberState.member.ownership.toOwnershipString())
                }
            }
        }
    }
}

private fun OwnershipType.toOwnershipString(): String =
    when (this) {
        OwnershipType.OWNER -> "Owner"
        OwnershipType.VIEWER -> "Viewer"
    }
