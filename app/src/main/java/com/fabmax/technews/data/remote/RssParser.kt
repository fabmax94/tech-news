package com.fabmax.technews.data.remote

import android.util.Log
import com.fabmax.technews.data.local.entity.ArticleEntity
import com.fabmax.technews.data.local.entity.SourceEntity
import org.jsoup.Jsoup
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RssParser @Inject constructor() {

    private val dateFormats = listOf(
        "EEE, dd MMM yyyy HH:mm:ss z",
        "EEE, dd MMM yyyy HH:mm:ss Z",
        "yyyy-MM-dd'T'HH:mm:ssZ",
        "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd'T'HH:mm:ssXXX",
        "dd MMM yyyy HH:mm:ss z"
    )

    fun parseRss(xmlContent: String, source: SourceEntity): List<ArticleEntity> {
        return try {
            if (xmlContent.trimStart().startsWith("<feed") || xmlContent.contains("<feed ")) {
                parseAtom(xmlContent, source)
            } else {
                parseRss20(xmlContent, source)
            }
        } catch (e: Exception) {
            Log.e("RssParser", "Error parsing feed from ${source.name}: ${e.message}")
            emptyList()
        }
    }

    private fun parseRss20(xmlContent: String, source: SourceEntity): List<ArticleEntity> {
        val articles = mutableListOf<ArticleEntity>()
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val parser = factory.newPullParser()
        parser.setInput(StringReader(xmlContent))

        var eventType = parser.eventType
        var inItem = false
        var title = ""
        var link = ""
        var description = ""
        var pubDate = ""
        var author = ""
        var content = ""
        var imageUrl: String? = null
        var guid = ""

        while (eventType != XmlPullParser.END_DOCUMENT) {
            val tagName = parser.name?.lowercase() ?: ""

            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when {
                        tagName == "item" -> {
                            inItem = true
                            title = ""; link = ""; description = ""; pubDate = ""
                            author = ""; content = ""; imageUrl = null; guid = ""
                        }
                        inItem && tagName == "title" -> title = parser.nextText()
                        inItem && tagName == "link" -> {
                            val href = parser.getAttributeValue(null, "href")
                            link = if (href != null) href else {
                                try { parser.nextText() } catch (e: Exception) { "" }
                            }
                        }
                        inItem && (tagName == "description" || tagName == "summary") ->
                            description = parser.nextText()
                        inItem && (tagName == "pubdate" || tagName == "published" || tagName == "updated" || tagName == "dc:date") ->
                            pubDate = try { parser.nextText() } catch (e: Exception) { "" }
                        inItem && (tagName == "author" || tagName == "dc:creator" || tagName == "creator") ->
                            author = try { parser.nextText() } catch (e: Exception) { "" }
                        inItem && (tagName == "content:encoded" || tagName == "encoded" || tagName == "content") ->
                            content = try { parser.nextText() } catch (e: Exception) { "" }
                        inItem && tagName == "guid" ->
                            guid = try { parser.nextText() } catch (e: Exception) { "" }
                        inItem && tagName == "enclosure" -> {
                            val type = parser.getAttributeValue(null, "type") ?: ""
                            if (type.startsWith("image/") && imageUrl == null) {
                                imageUrl = parser.getAttributeValue(null, "url")
                            }
                        }
                        inItem && (tagName == "media:thumbnail" || tagName == "media:content") -> {
                            if (imageUrl == null) {
                                imageUrl = parser.getAttributeValue(null, "url")
                            }
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (tagName == "item" && inItem) {
                        inItem = false
                        if (title.isNotBlank() && link.isNotBlank()) {
                            val id = if (guid.isNotBlank()) "${source.id}_$guid"
                                     else "${source.id}_${UUID.nameUUIDFromBytes((title + link).toByteArray())}"
                            val resolvedImage = imageUrl ?: extractImageFromHtml(description.ifBlank { content })
                            val cleanDescription = cleanHtml(description.ifBlank { content })
                            articles.add(
                                ArticleEntity(
                                    id = id,
                                    title = cleanHtml(title),
                                    description = cleanDescription,
                                    content = content.ifBlank { description },
                                    link = link.trim(),
                                    imageUrl = resolvedImage,
                                    author = author.trim().takeIf { it.isNotBlank() },
                                    publishedAt = parseDate(pubDate),
                                    sourceId = source.id,
                                    sourceName = source.name,
                                    sourceLogoUrl = source.logoUrl,
                                    category = source.category
                                )
                            )
                        }
                    }
                }
            }
            eventType = parser.next()
        }
        return articles
    }

    private fun parseAtom(xmlContent: String, source: SourceEntity): List<ArticleEntity> {
        val articles = mutableListOf<ArticleEntity>()
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true
        val parser = factory.newPullParser()
        parser.setInput(StringReader(xmlContent))

        var eventType = parser.eventType
        var inEntry = false
        var title = ""
        var link = ""
        var summary = ""
        var published = ""
        var authorName = ""
        var content = ""
        var id = ""
        var imageUrl: String? = null

        while (eventType != XmlPullParser.END_DOCUMENT) {
            val tagName = parser.name?.lowercase() ?: ""

            when (eventType) {
                XmlPullParser.START_TAG -> {
                    when {
                        tagName == "entry" -> {
                            inEntry = true
                            title = ""; link = ""; summary = ""; published = ""
                            authorName = ""; content = ""; id = ""; imageUrl = null
                        }
                        inEntry && tagName == "title" -> title = try { parser.nextText() } catch (e: Exception) { "" }
                        inEntry && tagName == "link" -> {
                            val rel = parser.getAttributeValue(null, "rel")
                            val href = parser.getAttributeValue(null, "href") ?: ""
                            if (rel == null || rel == "alternate") link = href
                        }
                        inEntry && tagName == "summary" -> summary = try { parser.nextText() } catch (e: Exception) { "" }
                        inEntry && tagName == "content" -> content = try { parser.nextText() } catch (e: Exception) { "" }
                        inEntry && (tagName == "published" || tagName == "updated") && published.isBlank() ->
                            published = try { parser.nextText() } catch (e: Exception) { "" }
                        inEntry && tagName == "name" && inEntry -> {
                            if (authorName.isBlank()) authorName = try { parser.nextText() } catch (e: Exception) { "" }
                        }
                        inEntry && tagName == "id" -> id = try { parser.nextText() } catch (e: Exception) { "" }
                        inEntry && (tagName == "media:thumbnail" || tagName == "media:content") -> {
                            if (imageUrl == null) {
                                imageUrl = parser.getAttributeValue(null, "url")
                            }
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    if (tagName == "entry" && inEntry) {
                        inEntry = false
                        if (title.isNotBlank() && link.isNotBlank()) {
                            val articleId = if (id.isNotBlank()) "${source.id}_$id"
                                           else "${source.id}_${UUID.nameUUIDFromBytes((title + link).toByteArray())}"
                            val bodyHtml = content.ifBlank { summary }
                            val resolvedImage = imageUrl ?: extractImageFromHtml(bodyHtml)
                            val cleanDescription = cleanHtml(summary.ifBlank { content })
                            articles.add(
                                ArticleEntity(
                                    id = articleId,
                                    title = cleanHtml(title),
                                    description = cleanDescription,
                                    content = bodyHtml,
                                    link = link.trim(),
                                    imageUrl = resolvedImage,
                                    author = authorName.trim().takeIf { it.isNotBlank() },
                                    publishedAt = parseDate(published),
                                    sourceId = source.id,
                                    sourceName = source.name,
                                    sourceLogoUrl = source.logoUrl,
                                    category = source.category
                                )
                            )
                        }
                    }
                }
            }
            eventType = parser.next()
        }
        return articles
    }

    private fun cleanHtml(html: String): String {
        return try {
            Jsoup.parse(html).text().trim()
        } catch (e: Exception) {
            html.replace(Regex("<[^>]*>"), "").trim()
        }
    }

    private fun extractImageFromHtml(html: String): String? {
        return try {
            val doc = Jsoup.parse(html)
            doc.select("img[src]").firstOrNull()?.attr("src")
        } catch (e: Exception) {
            null
        }
    }

    private fun parseDate(dateStr: String): Long {
        if (dateStr.isBlank()) return System.currentTimeMillis()
        for (format in dateFormats) {
            try {
                return SimpleDateFormat(format, Locale.ENGLISH).parse(dateStr)?.time
                    ?: System.currentTimeMillis()
            } catch (e: ParseException) {
                // try next format
            }
        }
        return System.currentTimeMillis()
    }
}
