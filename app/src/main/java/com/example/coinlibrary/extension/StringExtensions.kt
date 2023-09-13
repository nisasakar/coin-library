package com.example.coinlibrary.extension

import org.jsoup.Jsoup

fun clearHtmlTagsWithJSoup(input: String): String {
    return Jsoup.parse(input).text()
}