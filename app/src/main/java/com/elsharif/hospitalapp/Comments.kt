package com.elsharif.hospitalapp/*
* @Composable
fun CheckListCard(checkList: CheckList) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Checklist: ${checkList.checkList}", fontWeight = FontWeight.Bold)

            IconButton(onClick = { isExpanded = !isExpanded }) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isExpanded) {
            checkList.items.forEach { checkListItem ->
               // CheckListItemCard(checkListItem,checkList.checkList)

                var selectedOptions by remember { mutableStateOf(List(checkListItem.subItems.size) { -1 }) }


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = "Title: ${checkListItem.title}", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))

                    checkListItem.subItems.forEachIndexed { index, subItem ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)

                        ) {
                            Text(text = "${index + 1}: $subItem", maxLines = 2)
                        }
                        // Radio buttons for choices
                        Row(
                            verticalAlignment = Alignment.CenterVertically

                        ) {
                            RadioButton(
                                selected = selectedOptions[index] == 0,
                                onClick = { selectedOptions = selectedOptions.toMutableList().apply { set(index, 0) } },
                                colors = RadioButtonDefaults.colors(MaterialTheme.colorScheme.primary)
                            )
                            Text(text = "0", modifier = Modifier.padding(end = 4.dp))
                            RadioButton(
                                selected = selectedOptions[index] == 1,
                                onClick = { selectedOptions = selectedOptions.toMutableList().apply { set(index, 1) }

                                },
                                colors = RadioButtonDefaults.colors(MaterialTheme.colorScheme.primary)
                            )
                            Text(text = "1", modifier = Modifier.padding(end = 4.dp))
                            RadioButton(
                                selected = selectedOptions[index] == 2,
                                onClick = { selectedOptions = selectedOptions.toMutableList().apply { set(index, 2) } },
                                colors = RadioButtonDefaults.colors(MaterialTheme.colorScheme.primary)
                            )
                            Text(text = "2", modifier = Modifier.padding(end = 4.dp))
                        }



                    }
                }

                // Calculate the sum of selected options
                val sum = selectedOptions.filter { it != -1 }.sum()
                Text(text = "Total Sum: $sum", fontWeight = FontWeight.Bold)
            }
        }
    }
}
@Composable
fun CheckListItemCard(checkListItem: CheckListItem,checkList: String) {
    var selectedOptions by remember { mutableStateOf(List(checkListItem.subItems.size) { -1 }) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(text = "Title: ${checkListItem.title}", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))

        checkListItem.subItems.forEachIndexed { index, subItem ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)

            ) {
                Text(text = "${index + 1}: $subItem", maxLines = 2)
            }
            // Radio buttons for choices
            Row(
                verticalAlignment = Alignment.CenterVertically

            ) {
                RadioButton(
                    selected = selectedOptions[index] == 0,
                    onClick = { selectedOptions = selectedOptions.toMutableList().apply { set(index, 0) } },
                    colors = RadioButtonDefaults.colors(MaterialTheme.colorScheme.primary)
                )
                Text(text = "0", modifier = Modifier.padding(end = 4.dp))
                RadioButton(
                    selected = selectedOptions[index] == 1,
                    onClick = { selectedOptions = selectedOptions.toMutableList().apply { set(index, 1) }

                              },
                    colors = RadioButtonDefaults.colors(MaterialTheme.colorScheme.primary)
                )
                Text(text = "1", modifier = Modifier.padding(end = 4.dp))
                RadioButton(
                    selected = selectedOptions[index] == 2,
                    onClick = { selectedOptions = selectedOptions.toMutableList().apply { set(index, 2) } },
                    colors = RadioButtonDefaults.colors(MaterialTheme.colorScheme.primary)
                )
                Text(text = "2", modifier = Modifier.padding(end = 4.dp))
            }



        }
    }

    // Calculate the sum of selected options
    val sum = selectedOptions.filter { it != -1 }.sum()
    Text(text = "Total Sum: $sum", fontWeight = FontWeight.Bold)
}



* */