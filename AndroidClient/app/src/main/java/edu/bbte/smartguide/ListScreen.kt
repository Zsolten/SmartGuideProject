package edu.bbte.smartguide

import androidx.compose.animation.AnimatedVisibilityScope
//import androidx.compose.animation.ExperimentalSharedTransitionApi
//import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ListScreen(
    onItemClick: (Int, String) -> Unit
) {
    var drawable = listOf(R.drawable.image1, R.drawable.image2)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        itemsIndexed(drawable) { index, resId ->
            var text = "Item$index"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(resId, text) }
            ) {
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = null,
                    modifier = Modifier
                        .aspectRatio(16/9f)
                        .weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = text,
                    modifier = Modifier.weight(1f)
                )
            }
            }
    }
}