@file:OptIn(ExperimentalMaterial3Api::class)

package ru.urfu.droidpractice1.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ru.urfu.droidpractice1.R
import ru.urfu.droidpractice1.ui.theme.DroidPractice1Theme

const val ARTICLE1_IMAGE_URL =
    "https://journal.litres.ru/wp-content/uploads/2021/02/382270040cd7.png"

private sealed interface ArticleBlock {
    data object Rubric : ArticleBlock
    data object Title : ArticleBlock
    data object Lead : ArticleBlock
    data object Image : ArticleBlock
    data object Section : ArticleBlock
    data class Body(val textRes: Int) : ArticleBlock
    data object Quote : ArticleBlock
}

private val rammsteinArticle: List<ArticleBlock> = listOf(
    ArticleBlock.Rubric,
    ArticleBlock.Title,
    ArticleBlock.Lead,
    ArticleBlock.Image,
    ArticleBlock.Section,
    ArticleBlock.Body(R.string.article1_body_1),
    ArticleBlock.Body(R.string.article1_body_2),
    ArticleBlock.Body(R.string.article1_body_3),
    ArticleBlock.Quote,
)

@Composable
fun MainActivityScreen(
    isSecondArticleRead: Boolean = false,
    initialLikes: Int = 0,
    initialDislikes: Int = 0,
    onShare: (String) -> Unit = {},
    onOpenSecond: (likes: Int, dislikes: Int) -> Unit = { _, _ -> },
) {
    var likes by rememberSaveable { mutableIntStateOf(initialLikes) }
    var dislikes by rememberSaveable { mutableIntStateOf(initialDislikes) }

    DroidPractice1Theme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.article_title)) },
                    actions = {
                        IconButton(onClick = { onShare(buildShareText()) }) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = stringResource(R.string.share_article),
                            )
                        }
                    },
                )
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                items(rammsteinArticle, key = { it::class.simpleName.orEmpty() + it.hashCode() }) { block ->
                    when (block) {
                        ArticleBlock.Rubric -> RubricText(stringResource(R.string.article1_rubric))
                        ArticleBlock.Title -> TitleText(stringResource(R.string.article1_title))
                        ArticleBlock.Lead -> LeadText(stringResource(R.string.article1_lead))
                        ArticleBlock.Image -> ArticleImage(
                            url = ARTICLE1_IMAGE_URL,
                            descRes = R.string.article1_image_desc,
                        )
                        ArticleBlock.Section -> SectionText(stringResource(R.string.article1_section))
                        is ArticleBlock.Body -> BodyText(stringResource(block.textRes))
                        ArticleBlock.Quote -> QuoteBlock(
                            quoteRes = R.string.article1_quote,
                            translationRes = R.string.article1_quote_translation,
                            authorRes = R.string.article1_quote_author,
                        )
                    }
                }

                item {
                    SecondArticleStatus(isSecondArticleRead)
                }
                item {
                    ArticleActions(
                        likes = likes,
                        dislikes = dislikes,
                        onLike = { likes++ },
                        onDislike = { dislikes++ },
                        onOpenSecond = { onOpenSecond(likes, dislikes) },
                    )
                }
            }
        }
    }
}


@Composable
private fun RubricText(text: String) = Text(
    text = text,
    style = MaterialTheme.typography.labelSmall,
    letterSpacing = 2.sp,
    color = MaterialTheme.colorScheme.primary,
    fontWeight = FontWeight.SemiBold,
)

@Composable
private fun TitleText(text: String) = Text(
    text = text,
    style = MaterialTheme.typography.displaySmall,
    fontWeight = FontWeight.Black,
    color = MaterialTheme.colorScheme.onSurface,
)

@Composable
private fun LeadText(text: String) = Text(
    text = text,
    style = MaterialTheme.typography.titleMedium,
    fontStyle = FontStyle.Italic,
    color = MaterialTheme.colorScheme.onSurfaceVariant,
)

@Composable
private fun SectionText(text: String) = Text(
    text = text,
    style = MaterialTheme.typography.titleSmall,
    fontWeight = FontWeight.Bold,
    letterSpacing = 1.sp,
    color = MaterialTheme.colorScheme.onSurface,
)

@Composable
private fun BodyText(text: String) = Text(
    text = text,
    style = MaterialTheme.typography.bodyLarge,
    color = MaterialTheme.colorScheme.onSurface,
    lineHeight = 26.sp,
)

@Composable
private fun ArticleImage(url: String, descRes: Int) = AsyncImage(
    model = url,
    contentDescription = stringResource(descRes),
    modifier = Modifier
        .fillMaxWidth()
        .height(220.dp)
        .clip(RoundedCornerShape(12.dp)),
    contentScale = ContentScale.Crop,
)

@Composable
private fun QuoteBlock(quoteRes: Int, translationRes: Int, authorRes: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(96.dp)
                .background(MaterialTheme.colorScheme.primary),
        )
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = stringResource(quoteRes),
                style = MaterialTheme.typography.titleMedium,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = stringResource(translationRes),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(authorRes),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun SecondArticleStatus(isRead: Boolean) = Text(
    text = stringResource(
        if (isRead) R.string.second_article_read else R.string.second_article_not_read,
    ),
    style = MaterialTheme.typography.bodyMedium,
    color = if (isRead) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onSurfaceVariant,
)

@Composable
private fun ArticleActions(
    likes: Int,
    dislikes: Int,
    onLike: () -> Unit,
    onDislike: () -> Unit,
    onOpenSecond: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            CounterButton(Icons.Default.ThumbUp, "Лайк", likes, onLike)
            CounterButton(Icons.Default.ThumbDown, "Дизлайк", dislikes, onDislike)
        }
        Button(onClick = onOpenSecond) {
            Text(stringResource(R.string.go_to_second_article))
        }
    }
}

@Composable
private fun CounterButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDesc: String,
    value: Int,
    onClick: () -> Unit,
) = OutlinedButton(onClick = onClick) {
    Icon(icon, contentDescription = contentDesc, modifier = Modifier.size(18.dp))
    Spacer(Modifier.width(6.dp))
    Text("$value", style = MaterialTheme.typography.bodyLarge)
}


private fun buildShareText(): String = buildString {
    append("Rammstein\n\n")
    append("Rammstein — немецкая индастриал-метал группа, основанная в 1994 году в Берлине. ")
    append("Тиль Линдеманн, пиротехника и жёсткие риффы — их визитная карточка.")
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainActivityScreen()
}