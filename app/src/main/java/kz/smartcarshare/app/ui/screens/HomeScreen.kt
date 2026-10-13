package kz.smartcarshare.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kz.smartcarshare.app.data.Car
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.data.RentalViewModel
import kz.smartcarshare.app.data.SortOrder
import kz.smartcarshare.app.data.formatTenge
import kz.smartcarshare.app.data.model.LocalUser
import kz.smartcarshare.app.ui.components.*
import kz.smartcarshare.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: RentalViewModel,
    currentUser: LocalUser? = null,
    lang: Lang = Lang.KZ,
    onCarClick: (Int) -> Unit,
    onB2BClick: () -> Unit
) {
    val greetingText = if (!currentUser?.name.isNullOrBlank()) {
        if (lang == Lang.KZ) "Сәлем, ${currentUser.name}" else "Привет, ${currentUser.name}"
    } else {
        if (lang == Lang.KZ) "Сәлем!" else "Привет!"
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Surface1,
        unfocusedContainerColor = Surface1,
        focusedBorderColor = Ice,
        unfocusedBorderColor = BorderColor,
        focusedLabelColor = Ice,
        unfocusedLabelColor = TextMid,
        focusedTextColor = TextHi,
        unfocusedTextColor = TextHi
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(listOf(Surface1, Amber.copy(alpha = 0.12f))),
                        shape = RoundedCornerShape(22.dp)
                    )
                    .border(1.dp, BorderColor, RoundedCornerShape(22.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Text(greetingText, style = MaterialTheme.typography.bodySmall, color = TextMid)
                    Text(
                        Strings.get(lang, "home_greeting"),
                        style = MaterialTheme.typography.titleLarge,
                        color = TextHi,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = vm.searchQuery,
                    onValueChange = { vm.searchQuery = it },
                    placeholder = { Text(if (lang == Lang.KZ) "Көлік немесе модель іздеу..." else "Поиск авто или модели...", color = TextMid) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Ice) },
                    colors = textFieldColors,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                IconButton(
                    onClick = { vm.isFilterDialogVisible = true },
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            if (vm.sortOrder != SortOrder.NONE || vm.selectedSeatsFilter != "Барлығы" || vm.selectedBrand != "Барлығы" || vm.selectedBodyType != "Барлығы" || vm.selectedClass != "Барлығы") Amber else Surface1,
                            RoundedCornerShape(16.dp)
                        )
                        .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Tune,
                        contentDescription = "Фильтр",
                        tint = if (vm.sortOrder != SortOrder.NONE || vm.selectedSeatsFilter != "Барлығы" || vm.selectedBrand != "Барлығы" || vm.selectedBodyType != "Барлығы" || vm.selectedClass != "Барлығы") AmberOnDark else Ice
                    )
                }
            }
        }

        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(vm.categories) { cat ->
                    val isSelected = vm.selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .background(
                                if (isSelected) Amber else Surface1,
                                RoundedCornerShape(100.dp)
                            )
                            .border(
                                1.dp,
                                if (isSelected) Amber else BorderColor,
                                RoundedCornerShape(100.dp)
                            )
                            .clickable { vm.selectedCategory = cat }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isSelected) AmberOnDark else TextHi,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        item {
            SectionTitle(title = Strings.get(lang, "nearby_cars"), trailing = "${vm.filteredCars.size} ${Strings.get(lang, "available")}")
        }
        items(vm.filteredCars) { car ->
            CarRow(car = car, lang = lang, onClick = { onCarClick(car.id) })
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(Surface1, RoundedCornerShape(16.dp))
                    .border(1.dp, BorderColor, RoundedCornerShape(16.dp))
                    .clickable { onB2BClick() }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconBadge(Icons.Filled.Apartment, bg = Ice.copy(alpha = 0.16f), tint = Ice, sizeDp = 38)
                Column(modifier = Modifier.weight(1f)) {
                    Text(Strings.get(lang, "b2b_prompt"), style = MaterialTheme.typography.labelLarge, color = TextHi)
                    Text(Strings.get(lang, "b2b_sub"), style = MaterialTheme.typography.bodySmall, color = TextMid)
                }
                Surface(
                    color = Amber,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (lang == Lang.KZ) "Өту" else "Перейти",
                        color = AmberOnDark,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }

    if (vm.isFilterDialogVisible) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { vm.isFilterDialogVisible = false },
            sheetState = sheetState,
            containerColor = Surface1,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.72f)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    if (lang == Lang.KZ) "Фильтр және сұрыптау" else "Фильтр и сортировка",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextHi,
                    fontWeight = FontWeight.Bold
                )

                Text(if (lang == Lang.KZ) "Маркасы бойынша:" else "По марке:", style = MaterialTheme.typography.labelLarge, color = Amber)
                ExpandableChipGrid(
                    items = vm.brands,
                    selectedItem = vm.selectedBrand,
                    lang = lang,
                    onSelect = { vm.selectedBrand = it }
                )

                Text(if (lang == Lang.KZ) "Шасси / Кузов түрі:" else "Тип кузова / транспорта:", style = MaterialTheme.typography.labelLarge, color = Amber)
                ExpandableChipGrid(
                    items = vm.bodyTypes,
                    selectedItem = vm.selectedBodyType,
                    lang = lang,
                    onSelect = { vm.selectedBodyType = it }
                )

                Text(if (lang == Lang.KZ) "Көлік класы:" else "Класс авто/мото:", style = MaterialTheme.typography.labelLarge, color = Amber)
                ExpandableChipGrid(
                    items = vm.vehicleClasses,
                    selectedItem = vm.selectedClass,
                    lang = lang,
                    onSelect = { vm.selectedClass = it }
                )

                Text(if (lang == Lang.KZ) "Орын саны бойынша:" else "По количеству мест:", style = MaterialTheme.typography.labelLarge, color = Amber)
                val seatOptions = listOf("Барлығы", "2 орын", "4–5 орын", "7+ орын")
                ExpandableChipGrid(
                    items = seatOptions,
                    selectedItem = vm.selectedSeatsFilter,
                    lang = lang,
                    onSelect = { vm.selectedSeatsFilter = it }
                )

                Text(if (lang == Lang.KZ) "Бағасы бойынша сұрыптау:" else "Сортировка по цене:", style = MaterialTheme.typography.labelLarge, color = Amber)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = vm.sortOrder == SortOrder.PRICE_ASC,
                        onClick = { vm.sortOrder = if (vm.sortOrder == SortOrder.PRICE_ASC) SortOrder.NONE else SortOrder.PRICE_ASC },
                        label = { Text(if (lang == Lang.KZ) "Арзанынан" else "Сначала дешевле") }
                    )
                    FilterChip(
                        selected = vm.sortOrder == SortOrder.PRICE_DESC,
                        onClick = { vm.sortOrder = if (vm.sortOrder == SortOrder.PRICE_DESC) SortOrder.NONE else SortOrder.PRICE_DESC },
                        label = { Text(if (lang == Lang.KZ) "Қымбатынан" else "Сначала дороже") }
                    )
                }

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { vm.resetFilters() },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (lang == Lang.KZ) "Тазалау" else "Сбросить", color = TextMid)
                    }
                    Button(
                        onClick = { vm.isFilterDialogVisible = false },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Amber, contentColor = AmberOnDark)
                    ) {
                        Text(if (lang == Lang.KZ) "Қолдану" else "Применить", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun ExpandableChipGrid(
    items: List<String>,
    selectedItem: String,
    lang: Lang,
    onSelect: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val initialLimit = 6
    val initialItems = remember(items) { items.take(initialLimit) }
    val remainingItems = remember(items) { if (items.size > initialLimit) items.drop(initialLimit) else emptyList() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val initialRows = initialItems.chunked(3)
        initialRows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        FilterChip(
                            selected = selectedItem == item,
                            onClick = { onSelect(item) },
                            label = {
                                Text(
                                    text = item,
                                    maxLines = 1,
                                    fontSize = 11.sp
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        if (remainingItems.isNotEmpty()) {
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(animationSpec = tween(300)) + fadeIn(animationSpec = tween(300)),
                exit = shrinkVertically(animationSpec = tween(300)) + fadeOut(animationSpec = tween(300))
            ) {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val remainingRows = remainingItems.chunked(3)
                    remainingRows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { item ->
                                Box(modifier = Modifier.weight(1f)) {
                                    FilterChip(
                                        selected = selectedItem == item,
                                        onClick = { onSelect(item) },
                                        label = {
                                            Text(
                                                text = item,
                                                maxLines = 1,
                                                fontSize = 11.sp
                                            )
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                            repeat(3 - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            Surface(
                onClick = { isExpanded = !isExpanded },
                color = Surface1,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, BorderColor),
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = if (isExpanded) {
                        if (lang == Lang.KZ) "Жасыру ▲" else "Свернуть ▲"
                    } else {
                        val remaining = remainingItems.size
                        if (lang == Lang.KZ) "Көбірек көрсету (+$remaining) ▼" else "Показать больше (+$remaining) ▼"
                    },
                    color = Amber,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun CarRow(car: Car, lang: Lang, onClick: () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .background(Surface1, RoundedCornerShape(18.dp))
            .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (car.displayImageUrl.isNotBlank()) {
            Box(
                modifier = Modifier
                    .size(width = 80.dp, height = 58.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Surface2, RoundedCornerShape(12.dp))
                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(car.displayImageUrl)
                        .crossfade(true)
                        .setHeader("User-Agent", "Mozilla/5.0 (Linux; Android)")
                        .build(),
                    contentDescription = car.name,
                    contentScale = ContentScale.Crop,
                    error = rememberVectorPainter(if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar),
                    placeholder = rememberVectorPainter(if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar),
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            IconBadge(
                icon = if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar,
                bg = Ice,
                tint = AmberOnDark
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(car.name, style = MaterialTheme.typography.titleMedium, color = TextHi)
            Text(car.location, style = MaterialTheme.typography.bodySmall, color = TextLow)
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(top = 5.dp)
            ) {
                car.tags.forEach { Chip(it) }
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(formatTenge(car.pricePerHour), color = Ice, style = MaterialTheme.typography.titleMedium)
            Text(if (lang == Lang.KZ) "/ сағат" else "/ час", style = MaterialTheme.typography.bodySmall, color = TextMid)
            Spacer(Modifier.height(4.dp))
            Surface(
                color = Ice.copy(alpha = 0.15f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Ice.copy(alpha = 0.4f))
            ) {
                Text(
                    text = if (lang == Lang.KZ) "Брондау" else "Забронировать",
                    color = Ice,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}
