package kz.smartcarshare.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kz.smartcarshare.app.data.Car
import kz.smartcarshare.app.data.CompanyData
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.data.RentalViewModel
import kz.smartcarshare.app.data.SortOrder
import kz.smartcarshare.app.data.formatTenge
import kz.smartcarshare.app.data.model.LocalUser
import kz.smartcarshare.app.ui.components.*
import kz.smartcarshare.app.ui.theme.*

@Composable
fun PulsingOnlineDot(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Box(
        modifier = modifier.size(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(12.dp * scale)
                .clip(CircleShape)
                .background(Success.copy(alpha = alpha * 0.45f))
        )
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(Success)
        )
    }
}

@Composable
fun AnimatedDrivingTrack(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "drivingCar")
    val carOffsetFraction by infiniteTransition.animateFloat(
        initialValue = -0.1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(4500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "carOffset"
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(26.dp)
            .clip(RoundedCornerShape(100.dp))
            .background(Surface2)
            .border(1.dp, BorderColor, RoundedCornerShape(100.dp))
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        val totalWidth = this.maxWidth
        val currentPx = totalWidth * carOffsetFraction

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Ice.copy(alpha = 0.2f),
                            Amber.copy(alpha = 0.6f),
                            Ice.copy(alpha = 0.2f)
                        )
                    )
                )
        )

        Row(
            modifier = Modifier.offset(x = currentPx),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.DirectionsCar,
                contentDescription = null,
                tint = Amber,
                modifier = Modifier.size(18.dp)
            )
            Box(
                modifier = Modifier
                    .size(width = 10.dp, height = 2.dp)
                    .background(Ice, CircleShape)
            )
        }
    }
}

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
        if (lang == Lang.KZ) "Сәлем, ${currentUser.name}!" else "Привет, ${currentUser.name}!"
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
        // Hero Welcome Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Surface1,
                                Surface2,
                                Ice.copy(alpha = 0.08f)
                            )
                        ),
                        shape = RoundedCornerShape(26.dp)
                    )
                    .border(1.dp, BorderColor, RoundedCornerShape(26.dp))
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            PulsingOnlineDot()
                            Text(
                                text = "Smart CarShare · 24/7 Online",
                                style = MaterialTheme.typography.labelSmall,
                                color = Ice,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = greetingText,
                            style = MaterialTheme.typography.headlineSmall,
                            color = TextHi,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = Strings.get(lang, "home_greeting"),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMid,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Amber.copy(alpha = 0.12f))
                            .border(1.dp, Amber.copy(alpha = 0.35f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DirectionsCar,
                            contentDescription = null,
                            tint = Amber,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                AnimatedDrivingTrack()
            }
        }

        // Search Bar & Filter Button
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = vm.searchQuery,
                    onValueChange = { vm.searchQuery = it },
                    placeholder = { Text(if (lang == Lang.KZ) "Көлік немесе модель іздеу..." else "Поиск авто или модели...", color = TextMid) },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null, tint = Ice) },
                    colors = textFieldColors,
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                val hasActiveFilter = vm.sortOrder != SortOrder.NONE ||
                        (vm.selectedCompanyFilter != "Барлығы" && vm.selectedCompanyFilter != "Все") ||
                        vm.selectedSeatsFilter != "Барлығы" ||
                        vm.selectedBrand != "Барлығы" ||
                        vm.selectedBodyType != "Барлығы" ||
                        vm.selectedClass != "Барлығы"

                val filterBg by animateColorAsState(
                    targetValue = if (hasActiveFilter) Amber else Surface1,
                    animationSpec = tween(300),
                    label = "filterBg"
                )

                IconButton(
                    onClick = { vm.isFilterDialogVisible = true },
                    modifier = Modifier
                        .size(54.dp)
                        .background(filterBg, RoundedCornerShape(18.dp))
                        .border(1.dp, if (hasActiveFilter) Amber else BorderColor, RoundedCornerShape(18.dp))
                ) {
                    Icon(
                        imageVector = Icons.Filled.Tune,
                        contentDescription = "Фильтр",
                        tint = if (hasActiveFilter) Color.White else Ice
                    )
                }
            }
        }

        // Company Story Strip
        item {
            Column(modifier = Modifier.padding(bottom = 12.dp)) {
                Text(
                    text = if (lang == Lang.KZ) "ПРОКАТ КОМПАНИЯЛАРЫ" else "ПРОКАТНЫЕ КОМПАНИИ",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextLow,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val allCompNames = CompanyData.allCompanies.map { it.name }
                    items(allCompNames) { compName ->
                        val isSelected = (vm.selectedCompanyFilter == compName)

                        val chipBg by animateColorAsState(
                            targetValue = if (isSelected) Amber else Surface1,
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            label = "compChipBg"
                        )

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(chipBg)
                                .border(1.dp, if (isSelected) Amber else BorderColor, RoundedCornerShape(100.dp))
                                .clickable {
                                    vm.selectedCompanyFilter = if (isSelected) (if (lang == Lang.KZ) "Барлығы" else "Все") else compName
                                }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = compName,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextHi
                            )
                        }
                    }
                }
            }
        }

        // Categories LazyRow
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(vm.categories) { cat ->
                    val isSelected = vm.selectedCategory == cat
                    val catBg by animateColorAsState(
                        targetValue = if (isSelected) ElectricIndigo else Surface1,
                        animationSpec = spring(stiffness = Spring.StiffnessLow),
                        label = "catBg"
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100.dp))
                            .background(catBg)
                            .border(
                                1.dp,
                                if (isSelected) ElectricIndigo else BorderColor,
                                RoundedCornerShape(100.dp)
                            )
                            .clickable { vm.selectedCategory = cat }
                            .padding(horizontal = 16.dp, vertical = 9.dp)
                    ) {
                        Text(
                            text = cat,
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isSelected) Color.White else TextHi,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Section Title & Car Count
        item {
            SectionTitle(
                title = Strings.get(lang, "nearby_cars"),
                trailing = "${vm.filteredCars.size} ${Strings.get(lang, "available")}"
            )
        }

        // Cars List
        items(vm.filteredCars) { car ->
            CarRow(car = car, lang = lang, onClick = { onCarClick(car.id) })
        }

        // B2B Prompt
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .background(Surface1, RoundedCornerShape(20.dp))
                    .border(1.dp, BorderColor, RoundedCornerShape(20.dp))
                    .clickable { onB2BClick() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                IconBadge(Icons.Filled.Apartment, bg = Ice.copy(alpha = 0.12f), tint = Ice, sizeDp = 42)
                Column(modifier = Modifier.weight(1f)) {
                    Text(Strings.get(lang, "b2b_prompt"), style = MaterialTheme.typography.labelLarge, color = TextHi, fontWeight = FontWeight.Bold)
                    Text(Strings.get(lang, "b2b_sub"), style = MaterialTheme.typography.bodySmall, color = TextMid)
                }
                Surface(
                    color = Amber,
                    shape = RoundedCornerShape(100.dp)
                ) {
                    Text(
                        text = if (lang == Lang.KZ) "Өту" else "Перейти",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                    )
                }
            }
        }
    }

    // Filter Modal Sheet
    if (vm.isFilterDialogVisible) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { vm.isFilterDialogVisible = false },
            sheetState = sheetState,
            containerColor = Surface1,
            shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f)
                    .padding(horizontal = 22.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    if (lang == Lang.KZ) "Фильтр және сұрыптау" else "Фильтр и сортировка",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextHi,
                    fontWeight = FontWeight.Bold
                )

                Text(if (lang == Lang.KZ) "Компания бойынша:" else "По компании:", style = MaterialTheme.typography.labelLarge, color = Amber)
                ExpandableChipGrid(
                    items = vm.companyFilterOptions.filter { it != "Барлығы" && it != "Все" },
                    selectedItem = vm.selectedCompanyFilter,
                    lang = lang,
                    onSelect = {
                        vm.selectedCompanyFilter = if (vm.selectedCompanyFilter == it) (if (lang == Lang.KZ) "Барлығы" else "Все") else it
                    }
                )

                Text(if (lang == Lang.KZ) "Маркасы бойынша:" else "По марке:", style = MaterialTheme.typography.labelLarge, color = Amber)
                ExpandableChipGrid(
                    items = vm.brands.filter { it != "Барлығы" && it != "Все" },
                    selectedItem = vm.selectedBrand,
                    lang = lang,
                    onSelect = {
                        vm.selectedBrand = if (vm.selectedBrand == it) (if (lang == Lang.KZ) "Барлығы" else "Все") else it
                    }
                )

                Text(if (lang == Lang.KZ) "Шасси түрі бойынша:" else "По кузову:", style = MaterialTheme.typography.labelLarge, color = Amber)
                ExpandableChipGrid(
                    items = vm.bodyTypes.filter { it != "Барлығы" && it != "Все" },
                    selectedItem = vm.selectedBodyType,
                    lang = lang,
                    onSelect = {
                        vm.selectedBodyType = if (vm.selectedBodyType == it) (if (lang == Lang.KZ) "Барлығы" else "Все") else it
                    }
                )

                Text(if (lang == Lang.KZ) "Сұрыптау:" else "Сортировка по цене:", style = MaterialTheme.typography.labelLarge, color = Amber)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    val isAsc = vm.sortOrder == SortOrder.PRICE_ASC
                    val isDesc = vm.sortOrder == SortOrder.PRICE_DESC

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isAsc) Amber else Surface2)
                            .clickable { vm.sortOrder = if (isAsc) SortOrder.NONE else SortOrder.PRICE_ASC }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (lang == Lang.KZ) "Арзаннан" else "Сначала дешевле",
                            color = if (isAsc) Color.White else TextHi,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isDesc) Amber else Surface2)
                            .clickable { vm.sortOrder = if (isDesc) SortOrder.NONE else SortOrder.PRICE_DESC }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (lang == Lang.KZ) "Қымбаттан" else "Сначала дороже",
                            color = if (isDesc) Color.White else TextHi,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = { vm.resetFilters() },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f),
                        border = BorderStroke(1.dp, BorderColor)
                    ) {
                        Text(if (lang == Lang.KZ) "Тазалау" else "Сбросить", color = TextMid)
                    }

                    Button(
                        onClick = { vm.isFilterDialogVisible = false },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Amber)
                    ) {
                        Text(if (lang == Lang.KZ) "Қолдану" else "Применить", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun CarRow(car: Car, lang: Lang, onClick: () -> Unit) {
    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        color = Surface1,
        border = BorderStroke(1.dp, BorderColor),
        shadowElevation = 3.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 104.dp, height = 72.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Surface2)
                        .border(1.dp, BorderColor, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (car.displayImageUrl.isNotBlank()) {
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
                    } else {
                        IconBadge(
                            icon = if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar,
                            bg = Ice.copy(alpha = 0.12f),
                            tint = Ice,
                            sizeDp = 44
                        )
                    }

                    if (car.isLux) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(5.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(Brush.horizontalGradient(listOf(Amber, AmberGradientEnd)))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(10.dp)
                                )
                                Text(
                                    text = "LUX",
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = car.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = TextHi,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = car.location,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextLow,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Ice.copy(alpha = 0.12f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = car.displayTransmission(lang),
                                fontSize = 10.sp,
                                color = Ice,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ElectricIndigo.copy(alpha = 0.12f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = car.displayFuel(lang),
                                fontSize = 10.sp,
                                color = ElectricIndigo,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    IconButton(
                        onClick = { isFavorite = !isFavorite },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Избранное",
                            tint = if (isFavorite) ErrorColor else TextLow,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(Modifier.height(2.dp))

                    Text(
                        text = formatTenge(car.pricePerHour),
                        color = Ice,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = if (lang == Lang.KZ) "/ сағат" else "/ час",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMid
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    car.tags.forEach { tag ->
                        Chip(text = tag)
                    }
                }

                Spacer(Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(Brush.horizontalGradient(listOf(Amber, AmberGradientEnd)))
                        .clickable { onClick() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (lang == Lang.KZ) "Брондау" else "Забронировать",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
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
    val chunkSize = 3
    val rows = items.chunked(chunkSize)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEach { item ->
                    val isSelected = item == selectedItem
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(100.dp))
                            .background(if (isSelected) Amber else Surface2)
                            .border(1.dp, if (isSelected) Amber else BorderColor, RoundedCornerShape(100.dp))
                            .clickable { onSelect(item) }
                            .padding(vertical = 9.dp, horizontal = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item,
                            fontSize = 12.sp,
                            color = if (isSelected) Color.White else TextHi,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                if (rowItems.size < chunkSize) {
                    repeat(chunkSize - rowItems.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}
