package com.example.pricelist.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pricelist.Product
import com.example.pricelist.ProductDao
import com.example.pricelist.ProductListVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val MainGreen = Color(76, 175, 80, 255)
val SecondaryRed = Color.Red.copy(0.70f)

@Composable
fun ShoppingListScr(dao: ProductDao, prodVM: ProductListVM = viewModel()) {
    val prods by prodVM.productList
    val scope = rememberCoroutineScope()
    var pName by remember { mutableStateOf("") }
    LaunchedEffect(Unit) { prodVM.copyFrom(dao.getProducts()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 12.dp, horizontal = 32.dp)
    ) {
        DefTextFld(
            value = pName,
            onVal = { pName = it },
            placeholder = {
                Text(
                    "Название товара",
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.alpha(0.3f)
                )
            }
        )
        Spacer(Modifier.height(12.dp))
        DefButton(
            act = {
                if (pName.isNotBlank()) {
                    pName = pName.trim()
                    prodVM.add(Product(name = pName))
                    scope.launch { dao.insert(prods.last()) }
                    pName = ""
                }
            },
            txt = "Добавить"
        )
        Spacer(Modifier.height(6.dp))
        DefButton(
            act = {
                if (prods.size > 1)
                    prodVM.sort()
            },
            txt = "Сортировать"
        )
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(prods) { prod ->
                ProductCard(
                    prod = prod,
                    scope = scope,
                    dao = dao,
                    prodVM = prodVM
                )
            }
        }
    }
}

@Composable
fun ProductCard(prod: Product, scope: CoroutineScope, dao: ProductDao, prodVM: ProductListVM) {
    var onEdit by remember { mutableStateOf(false) }
    var nProdName by remember { mutableStateOf(prod.name) }

    Card(
        colors = CardDefaults.elevatedCardColors(containerColor = MainGreen),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 12.dp)
        ) {
            if (onEdit) {
                CardTxtFld(
                    value = nProdName,
                    onVal = { nProdName = it }
                )
            } else {
                Text(
                    text = prod.name,
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    softWrap = true
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(
                onClick = {
                    onEdit = !onEdit
                    if (!onEdit)
                        prodVM.replace(prod, Product(name = nProdName), scope, dao)
                }
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Редактировать",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(
                onClick = {
                    prodVM.remove(prod)
                    scope.launch { dao.delete(prod) }
                }
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Удалить",
                    tint = SecondaryRed,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun DefButton(act: () -> Unit = {}, txt: String) {
    Button(
        modifier = Modifier.fillMaxWidth().padding(),
        onClick = act,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        border = BorderStroke(1.75.dp, MainGreen)
    ) {
        Text(txt, fontSize = 20.sp, color = Color.Black)
    }
}

@Composable
fun DefTextFld(
    value: String,
    onVal: (String) -> Unit = {},
    placeholder: @Composable (() -> Unit)? = null,
    colors: TextFieldColors? = null
) {
    if (colors == null) {
        TextField(
            textStyle = TextStyle.Default.copy(fontSize = 18.sp),
            value = value,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Black,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = MainGreen,
                focusedContainerColor = Color.Transparent,
                cursorColor = SecondaryRed
            ),
            placeholder = placeholder,
            onValueChange = onVal,
            modifier = Modifier.fillMaxWidth()
        )
    }
    else {
        TextField(
            textStyle = TextStyle.Default.copy(fontSize = 18.sp),
            value = value,
            colors = colors,
            placeholder = placeholder,
            onValueChange = onVal,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CardTxtFld(value: String, onVal: (String) -> Unit = {}) {
    DefTextFld(
        value = value,
        onVal = onVal,
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = SecondaryRed,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.White,
            focusedContainerColor = Color.Transparent,
            cursorColor = SecondaryRed,
            unfocusedTextColor = Color.White,
            focusedTextColor = Color.White
        )
    )
}