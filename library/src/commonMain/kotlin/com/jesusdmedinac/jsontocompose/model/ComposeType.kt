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
    Scaffold,
    Card,
    Dialog,
    Custom;

    fun isLayout(): Boolean = when (this) {
        Column, Row, Box -> true
        else -> false
    }

    fun hasChild(): Boolean = when (this) {
        Button, Card -> true
        else -> false
    }
}
