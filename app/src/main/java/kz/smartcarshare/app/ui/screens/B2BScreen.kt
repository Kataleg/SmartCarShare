package kz.smartcarshare.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.smartcarshare.app.data.Lang
import kz.smartcarshare.app.ui.components.*
import kz.smartcarshare.app.ui.theme.*

private data class Plan(
    val name: String,
    val price: String,
    val features: List<String>,
    val recommended: Boolean = false
)

private fun getPlans(lang: Lang) = if (lang == Lang.KZ) {
    listOf(
        Plan("Старт", "280 000 ₸ / айына", listOf("3 қызметкерге дейін", "Айына 40 сағат жалдау")),
        Plan(
            "Бизнес", "690 000 ₸ / айына",
            listOf("10 қызметкерге дейін", "Шектеусіз сағаттық жалдау", "Бірыңғай бухгалтерлік есеп"),
            recommended = true
        ),
        Plan("Корпоративтік", "жеке есеп", listOf("10+ қызметкер, жеке флот"))
    )
} else {
    listOf(
        Plan("Старт", "280 000 ₸ / месяц", listOf("До 3 сотрудников", "40 часов аренды в месяц")),
        Plan(
            "Бизнес", "690 000 ₸ / месяц",
            listOf("До 10 сотрудников", "Безлимитная почасовая аренда", "Единый бухгалтерский учет"),
            recommended = true
        ),
        Plan("Корпоративный", "индивидуальный расчет", listOf("10+ сотрудников, свой автопарк"))
    )
}

@Composable
fun B2BScreen(
    lang: Lang = Lang.KZ,
    onBack: () -> Unit,
    onPlanChosen: (String) -> Unit
) {
    val plans = getPlans(lang)

    Column(modifier = Modifier.fillMaxSize()) {
        BackTopBar(
            title = if (lang == Lang.KZ) "Компанияларға арналған" else "Для бизнеса и компаний",
            onBack = onBack
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp)
        ) {
            item {
                Text(
                    text = if (lang == Lang.KZ)
                        "Қызметкерлеріңіз үшін айлық абонемент рәсімдеңіз — бекітілген саны бойынша көлікті шектеусіз пайдалану."
                    else
                        "Оформите месячный абонемент для сотрудников — безлимитное использование автомобилей для вашего бизнеса.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMid,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            items(plans) { plan ->
                PlanCard(plan, lang) { onPlanChosen(plan.name) }
            }
        }
    }
}

@Composable
private fun PlanCard(plan: Plan, lang: Lang, onChoose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .background(
                if (plan.recommended) Amber.copy(alpha = 0.10f) else Surface1,
                RoundedCornerShape(18.dp)
            )
            .border(1.dp, if (plan.recommended) Amber else BorderColor, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        if (plan.recommended) {
            Box(
                modifier = Modifier
                    .background(Amber, RoundedCornerShape(100.dp))
                    .padding(horizontal = 9.dp, vertical = 3.dp)
            ) {
                Text(
                    if (lang == Lang.KZ) "Ұсынылады" else "Рекомендуется",
                    style = MaterialTheme.typography.bodySmall,
                    color = AmberOnDark
                )
            }
        }
        Text(
            plan.name,
            style = MaterialTheme.typography.titleMedium,
            color = TextHi,
            modifier = Modifier.padding(top = 6.dp)
        )
        Text(
            plan.price,
            style = MaterialTheme.typography.headlineSmall,
            color = Ice,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        plan.features.forEach { feat ->
            Row(
                modifier = Modifier.padding(top = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Success, modifier = Modifier.size(18.dp))
                Text(feat, style = MaterialTheme.typography.bodyMedium, color = TextMid)
            }
        }
        PrimaryButton(
            text = if (plan.name == "Корпоративтік" || plan.name == "Корпоративный") {
                if (lang == Lang.KZ) "Байланысу" else "Связаться"
            } else {
                if (lang == Lang.KZ) "Тарифті таңдау" else "Выбрать тариф"
            },
            modifier = Modifier.padding(top = 12.dp),
            onClick = onChoose
        )
    }
}