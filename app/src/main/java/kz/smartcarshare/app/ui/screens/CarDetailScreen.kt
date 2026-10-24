package kz.smartcarshare.app.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import kz.smartcarshare.app.data.CompanyData
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.data.RentalViewModel
import kz.smartcarshare.app.data.Tariff
import kz.smartcarshare.app.data.formatTenge
import kz.smartcarshare.app.ui.components.*
import kz.smartcarshare.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarDetailScreen(
    vm: RentalViewModel,
    onBack: () -> Unit,
    onRentClick: () -> Unit
) {
    val car = vm.selectedCar ?: return
    val lang = vm.currentLang
    var isBranchesSheetVisible by remember { mutableStateOf(false) }
    var isFullScreenImageOpen by remember { mutableStateOf(false) }

    val company = remember(car) {
        CompanyData.allCompanies.find { comp ->
            car.company.contains(comp.name, ignoreCase = true) ||
            comp.name.contains(car.company, ignoreCase = true) ||
            comp.fleetCarIds.contains(car.id)
        } ?: CompanyData.cashAutoRent
    }

    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar(title = car.name, onBack = onBack)

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            item {
                val carImages = remember(car) { car.allImages.ifEmpty { listOf("") } }
                val pagerState = rememberPagerState(pageCount = { carImages.size })
                val context = LocalContext.current

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 10f)
                        .clip(RoundedCornerShape(22.dp))
                        .background(Surface2)
                        .border(1.dp, BorderLight, RoundedCornerShape(22.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (carImages.isNotEmpty() && carImages.first().isNotBlank()) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { page ->
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(carImages[page])
                                    .crossfade(true)
                                    .setHeader("User-Agent", "Mozilla/5.0 (Linux; Android)")
                                    .build(),
                                contentDescription = "${car.name} photo ${page + 1}",
                                contentScale = ContentScale.Crop,
                                error = rememberVectorPainter(if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar),
                                placeholder = rememberVectorPainter(if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { isFullScreenImageOpen = true }
                            )
                        }

                        if (carImages.size > 1) {
                            Surface(
                                color = Surface1.copy(alpha = 0.75f),
                                shape = RoundedCornerShape(100.dp),
                                border = BorderStroke(1.dp, BorderColor),
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = "${pagerState.currentPage + 1}/${carImages.size}",
                                    color = TextHi,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .padding(bottom = 12.dp)
                                    .background(Surface1.copy(alpha = 0.65f), RoundedCornerShape(100.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                repeat(carImages.size) { iteration ->
                                    val color = if (pagerState.currentPage == iteration) Amber else TextMid.copy(alpha = 0.5f)
                                    Box(
                                        modifier = Modifier
                                            .size(if (pagerState.currentPage == iteration) 8.dp else 6.dp)
                                            .clip(CircleShape)
                                            .background(color)
                                    )
                                }
                            }
                        }
                    } else {
                        Icon(
                            imageVector = if (car.isLux) Icons.Filled.Star else Icons.Filled.DirectionsCar,
                            contentDescription = null,
                            tint = Ice,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = null, tint = Ice, modifier = Modifier.size(16.dp))
                    Text(car.location, style = MaterialTheme.typography.bodySmall, color = Ice)
                }
            }
            item {
                Row(
                    modifier = Modifier.padding(top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    car.tags.forEach { Chip(it, accent = true) }
                }
            }
            item {
                val context = LocalContext.current
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                        .background(Surface1, RoundedCornerShape(18.dp))
                        .border(1.dp, BorderColor, RoundedCornerShape(18.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (lang == Lang.KZ) "КӨЛІК ПАРКІ ЖӘНЕ БАЙЛАНЫС" else "АВТОПАРК И КОНТАКТЫ",
                        style = MaterialTheme.typography.bodySmall,
                        color = Amber,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = company.legalName.ifBlank { company.name },
                        style = MaterialTheme.typography.titleMedium,
                        color = TextHi,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(10.dp))

                    company.phones.forEach { phone ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phone.number.replace("[^0-9+]".toRegex(), "")}"))
                                    context.startActivity(intent)
                                }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.Phone, contentDescription = null, tint = Ice, modifier = Modifier.size(18.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = phone.number,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Ice,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = phone.label,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMid
                                )
                            }
                        }
                        Spacer(Modifier.height(4.dp))
                    }

                    Spacer(Modifier.height(8.dp))

                    if (company.websiteUrl.isNotBlank()) {
                        OutlinedButton(
                            onClick = {
                                val url = if (!company.websiteUrl.startsWith("http")) "https://${company.websiteUrl}" else company.websiteUrl
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Ice),
                            border = BorderStroke(1.dp, Ice)
                        ) {
                            Icon(Icons.Filled.Language, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = if (lang == Lang.KZ) "Компанияның ресми сайтына өту" else "Перейти на сайт компании",
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }

                    OutlinedButton(
                        onClick = { isBranchesSheetVisible = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Amber),
                        border = BorderStroke(1.dp, Amber)
                    ) {
                        Icon(Icons.Filled.Map, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (lang == Lang.KZ) "Барлық филиалдар (2GIS)" else "Все филиалы компании (2GIS)",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                        .background(Surface1, RoundedCornerShape(22.dp))
                        .border(1.dp, BorderColor, RoundedCornerShape(22.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (lang == Lang.KZ) "ТЕХНИКАЛЫҚ СИПАТТАМАСЫ" else "ТЕХНИЧЕСКИЕ ХАРАКТЕРИСТИКИ",
                        style = MaterialTheme.typography.bodySmall,
                        color = Amber,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SpecItem(Strings.get(lang, "seats"), car.displaySeats(lang), Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(36.dp)
                                .background(BorderColor)
                        )
                        SpecItem(
                            Strings.get(lang, "transmission"),
                            car.displayTransmission(lang),
                            Modifier
                                .weight(1f)
                                .padding(start = 14.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                            .height(1.dp)
                            .background(BorderColor)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SpecItem(Strings.get(lang, "fuel"), car.displayFuel(lang), Modifier.weight(1f))
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(36.dp)
                                .background(BorderColor)
                        )
                        SpecItem(
                            Strings.get(lang, "category"),
                            if (car.isLux) "Lux" else "Standard",
                            Modifier
                                .weight(1f)
                                .padding(start = 14.dp)
                        )
                    }
                }
            }
            item { SectionTitle(title = Strings.get(lang, "rental_format")) }
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TariffOption(
                        label = Strings.get(lang, "hourly"),
                        value = formatTenge(car.pricePerHour),
                        selected = vm.tariff == Tariff.HOUR,
                        modifier = Modifier.weight(1f)
                    ) { vm.changeTariff(Tariff.HOUR) }
                    TariffOption(
                        label = Strings.get(lang, "daily"),
                        value = car.pricePerDay?.let { formatTenge(it) } ?: "—",
                        selected = vm.tariff == Tariff.DAY,
                        enabled = car.pricePerDay != null,
                        modifier = Modifier.weight(1f)
                    ) { vm.changeTariff(Tariff.DAY) }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Navy800)
                .padding(20.dp)
        ) {
            val unit = if (vm.tariff == Tariff.HOUR) (if (lang == Lang.KZ) "/сағ" else "/час") else (if (lang == Lang.KZ) "/тәулік" else "/сутки")
            PrimaryButton(
                text = "${Strings.get(lang, "rent")} — ${formatTenge(vm.currentPrice())} $unit",
                onClick = onRentClick
            )
        }
    }

    if (isBranchesSheetVisible) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val context = LocalContext.current
        ModalBottomSheet(
            onDismissRequest = { isBranchesSheetVisible = false },
            sheetState = sheetState,
            containerColor = Surface1,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = if (lang == Lang.KZ) "«${company.name}» филиалдары" else "Филиалы компании «${company.name}»",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextHi,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (lang == Lang.KZ) "Картадан ашу үшін қажетті филиалды таңдаңыз:" else "Выберите нужный филиал для открытия на карте 2GIS:",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMid
                )

                company.locations.forEach { location ->
                    Surface(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(location.mapUrl.ifBlank { company.map2GisUrl }))
                            context.startActivity(intent)
                            isBranchesSheetVisible = false
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = Surface2,
                        border = BorderStroke(1.dp, BorderColor),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            IconBadge(
                                icon = Icons.Filled.LocationOn,
                                bg = Ice.copy(alpha = 0.18f),
                                tint = Ice,
                                sizeDp = 42
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = location.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextHi,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = location.address,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMid,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                            Surface(
                                color = Amber,
                                shape = RoundedCornerShape(100.dp)
                            ) {
                                Text(
                                    text = if (lang == Lang.KZ) "2GIS ашу" else "Открыть",
                                    color = AmberOnDark,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
            }
        }
    }

    if (isFullScreenImageOpen) {
        val carImages = remember(car) { car.allImages.ifEmpty { listOf("") } }
        val fullPagerState = rememberPagerState(initialPage = 0, pageCount = { carImages.size })
        val context = LocalContext.current

        Dialog(
            onDismissRequest = { isFullScreenImageOpen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.96f)),
                contentAlignment = Alignment.Center
            ) {
                if (carImages.isNotEmpty() && carImages.first().isNotBlank()) {
                    HorizontalPager(
                        state = fullPagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(carImages[page])
                                .crossfade(true)
                                .setHeader("User-Agent", "Mozilla/5.0 (Linux; Android)")
                                .build(),
                            contentDescription = "${car.name} full photo ${page + 1}",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 40.dp, horizontal = 12.dp)
                        )
                    }

                    if (carImages.size > 1) {
                        Surface(
                            color = Surface1.copy(alpha = 0.85f),
                            shape = RoundedCornerShape(100.dp),
                            border = BorderStroke(1.dp, BorderColor),
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 28.dp)
                        ) {
                            Text(
                                text = "${fullPagerState.currentPage + 1}/${carImages.size}",
                                color = TextHi,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                IconButton(
                    onClick = { isFullScreenImageOpen = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 40.dp, end = 20.dp)
                        .size(44.dp)
                        .background(Surface1.copy(alpha = 0.85f), CircleShape)
                        .border(1.dp, BorderColor, CircleShape)
                ) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Закрыть",
                        tint = TextHi,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SpecItem(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextLow)
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = TextHi,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

@Composable
private fun TariffOption(
    label: String,
    value: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(
                if (selected) Amber.copy(alpha = 0.18f) else Surface1,
                RoundedCornerShape(16.dp)
            )
            .border(1.5.dp, if (selected) Amber else BorderColor, RoundedCornerShape(16.dp))
            .clickable(enabled = enabled) { onClick() }
            .padding(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = if (selected) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (selected) Amber else TextLow,
                modifier = Modifier.size(16.dp)
            )
            Text(label, style = MaterialTheme.typography.bodySmall, color = if (selected) Amber else TextMid, fontWeight = FontWeight.Bold)
        }
        Text(
            value,
            style = MaterialTheme.typography.titleLarge,
            color = TextHi,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
