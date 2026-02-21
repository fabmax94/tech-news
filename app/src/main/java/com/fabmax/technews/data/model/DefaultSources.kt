package com.fabmax.technews.data.model

import com.fabmax.technews.data.local.entity.SourceEntity
import com.fabmax.technews.domain.model.ArticleCategory

object DefaultSources {

    val all = listOf(
        SourceEntity(
            id = "hacker_news",
            name = "Hacker News",
            feedUrl = "https://news.ycombinator.com/rss",
            siteUrl = "https://news.ycombinator.com",
            logoUrl = "https://news.ycombinator.com/favicon.ico",
            category = ArticleCategory.GENERAL.name
        ),
        SourceEntity(
            id = "techcrunch",
            name = "TechCrunch",
            feedUrl = "https://techcrunch.com/feed/",
            siteUrl = "https://techcrunch.com",
            logoUrl = "https://techcrunch.com/wp-content/uploads/2015/02/cropped-cropped-favicon-gradient.png",
            category = ArticleCategory.STARTUPS.name
        ),
        SourceEntity(
            id = "the_verge",
            name = "The Verge",
            feedUrl = "https://www.theverge.com/rss/index.xml",
            siteUrl = "https://www.theverge.com",
            logoUrl = "https://cdn.vox-cdn.com/uploads/chorus_asset/file/7395367/favicon-64x64.0.png",
            category = ArticleCategory.GENERAL.name
        ),
        SourceEntity(
            id = "ars_technica",
            name = "Ars Technica",
            feedUrl = "https://feeds.arstechnica.com/arstechnica/index",
            siteUrl = "https://arstechnica.com",
            logoUrl = "https://cdn.arstechnica.net/wp-content/uploads/2016/10/cropped-ars-logo-512_480-32x32.png",
            category = ArticleCategory.GENERAL.name
        ),
        SourceEntity(
            id = "devto",
            name = "Dev.to",
            feedUrl = "https://dev.to/feed",
            siteUrl = "https://dev.to",
            logoUrl = "https://dev.to/favicon.ico",
            category = ArticleCategory.DEV.name
        ),
        SourceEntity(
            id = "infoq",
            name = "InfoQ",
            feedUrl = "https://feed.infoq.com/",
            siteUrl = "https://www.infoq.com",
            logoUrl = "https://www.infoq.com/favicon.ico",
            category = ArticleCategory.DEV.name
        ),
        SourceEntity(
            id = "mit_tech_review",
            name = "MIT Technology Review",
            feedUrl = "https://www.technologyreview.com/feed/",
            siteUrl = "https://www.technologyreview.com",
            logoUrl = "https://www.technologyreview.com/favicon.ico",
            category = ArticleCategory.AI.name
        ),
        SourceEntity(
            id = "android_developers",
            name = "Android Developers Blog",
            feedUrl = "https://android-developers.googleblog.com/feeds/posts/default?alt=rss",
            siteUrl = "https://android-developers.googleblog.com",
            logoUrl = "https://www.gstatic.com/devrel-devsite/prod/v2210deb8920cd4a55bd580441aa58e7b7cd14c6b49f51e4e97e9e5e0fd2d854b/android/images/favicon.png",
            category = ArticleCategory.MOBILE.name
        ),
        SourceEntity(
            id = "security_week",
            name = "SecurityWeek",
            feedUrl = "https://feeds.feedburner.com/Securityweek",
            siteUrl = "https://www.securityweek.com",
            logoUrl = "https://www.securityweek.com/favicon.ico",
            category = ArticleCategory.SECURITY.name
        ),
        SourceEntity(
            id = "krebs_on_security",
            name = "Krebs on Security",
            feedUrl = "https://krebsonsecurity.com/feed/",
            siteUrl = "https://krebsonsecurity.com",
            logoUrl = "https://krebsonsecurity.com/favicon.ico",
            category = ArticleCategory.SECURITY.name
        ),
        SourceEntity(
            id = "aws_blog",
            name = "AWS News Blog",
            feedUrl = "https://aws.amazon.com/blogs/aws/feed/",
            siteUrl = "https://aws.amazon.com/blogs/aws",
            logoUrl = "https://aws.amazon.com/favicon.ico",
            category = ArticleCategory.CLOUD.name
        ),
        SourceEntity(
            id = "github_blog",
            name = "GitHub Blog",
            feedUrl = "https://github.blog/feed/",
            siteUrl = "https://github.blog",
            logoUrl = "https://github.com/favicon.ico",
            category = ArticleCategory.OPEN_SOURCE.name
        ),
        SourceEntity(
            id = "wired_tech",
            name = "Wired",
            feedUrl = "https://www.wired.com/feed/rss",
            siteUrl = "https://www.wired.com",
            logoUrl = "https://www.wired.com/favicon.ico",
            category = ArticleCategory.GENERAL.name
        ),
        SourceEntity(
            id = "css_tricks",
            name = "CSS-Tricks",
            feedUrl = "https://css-tricks.com/feed/",
            siteUrl = "https://css-tricks.com",
            logoUrl = "https://css-tricks.com/favicon.ico",
            category = ArticleCategory.DEV.name
        ),
        SourceEntity(
            id = "kotlin_blog",
            name = "Kotlin Blog",
            feedUrl = "https://blog.jetbrains.com/kotlin/feed/",
            siteUrl = "https://blog.jetbrains.com/kotlin",
            logoUrl = "https://kotlinlang.org/favicon.ico",
            category = ArticleCategory.DEV.name
        )
    )
}
