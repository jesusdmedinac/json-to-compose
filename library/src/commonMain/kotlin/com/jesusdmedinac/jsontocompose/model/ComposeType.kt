package com.jesusdmedinac.jsontocompose.model

enum class ComposeType {
    Column,
    Row,
    Box,
    Text,
    Button,
    Image,
    TextField,
    LazyColumn,
    LazyRow,
    Scaffold;

    fun isLayout(): Boolean = when (this) {
        Column, Row, Box -> true
        else -> false
    }

    fun hasChild(): Boolean = when (this) {
        Button -> true
        else -> false
    }
}
