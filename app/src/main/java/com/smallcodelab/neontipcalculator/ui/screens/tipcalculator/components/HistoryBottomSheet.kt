package com.smallcodelab.neontipcalculator.ui.screens.tipcalculator.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallcodelab.neontipcalculator.R
import com.smallcodelab.neontipcalculator.data.currency.CurrencyOption
import com.smallcodelab.neontipcalculator.data.db.TipHistoryEntity
import com.smallcodelab.neontipcalculator.ui.theme.CyanBlue
import com.smallcodelab.neontipcalculator.ui.theme.DarkBg
import com.smallcodelab.neontipcalculator.ui.theme.MutedText
import com.smallcodelab.neontipcalculator.ui.theme.NeonGreen
import com.smallcodelab.neontipcalculator.ui.theme.PanelBg
import com.smallcodelab.neontipcalculator.ui.theme.WhiteText
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Modal bottom sheet displaying saved tip calculation history.
 *
 * @param historyList List of saved calculation items from Room.
 * @param onDismiss Callback to close bottom sheet.
 * @param onDeleteItem Callback to delete a specific history item.
 * @param onClearAll Callback to delete all history items.
 * @param modifier Composable modifier.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryBottomSheet(
    historyList: List<TipHistoryEntity>,
    onDismiss: () -> Unit,
    onDeleteItem: (TipHistoryEntity) -> Unit,
    onClearAll: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = DarkBg,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = NeonGreen,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = stringResource(id = R.string.history_title),
                    color = WhiteText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                )
                if (historyList.isNotEmpty()) {
                    TextButton(onClick = onClearAll) {
                        Text(
                            text = stringResource(id = R.string.btn_clear_all),
                            color = CyanBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (historyList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(id = R.string.history_empty),
                            color = WhiteText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(id = R.string.history_empty_subtitle),
                            color = MutedText,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.height(340.dp)
                ) {
                    items(items = historyList, key = { it.id }) { item ->
                        HistoryCardItem(
                            item = item,
                            onDelete = { onDeleteItem(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryCardItem(
    item: TipHistoryEntity,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currency = CurrencyOption.entries.find { it.code == item.currencyCode } ?: CurrencyOption.USD
    val formattedDate = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(item.timestamp))
    val deleteDescription = stringResource(id = R.string.cd_delete_history_item)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = PanelBg, shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${currency.symbol}${item.perPersonAmount} / person",
                color = NeonGreen,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Bill: ${currency.symbol}${item.billAmount} | Tip: ${item.tipPercentage}% | Split: ${item.peopleCount}",
                color = WhiteText,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = formattedDate,
                color = MutedText,
                fontSize = 11.sp
            )
        }
        IconButton(
            onClick = onDelete,
            modifier = Modifier.semantics { contentDescription = deleteDescription }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = deleteDescription,
                tint = MutedText
            )
        }
    }
}
