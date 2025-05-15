package com.vasilev.news.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

/**
 * <pre>
 * Правила для поиска тегов в структуре XML
 * Формат правил XPath:
 * - <a href="https://docs.oracle.com/javase/8/docs/api/javax/xml/xpath/package-summary.html">XPath</a>
 *
 * Например, для Xml структуры вида:
 * <?xml version="1.0" encoding="UTF-8"?>
 * <rss xmlns:rbc_news="https://www.rbc.ru" version="2.0">
 *     <channel>
 *         ..
 *         <item>
 *             <title>Это заголовок новости"</title>
 *             ..
 *         </item>
 *         ..
 *     </channel>
 * </rss>
 *
 * В данном примере правила поиска будут такими:
 * - Поиск всех элементов <item> будет производиться через: item_path="//item"
 * - Заголовок внутри новости: title="title"
 * </pre>
 */
@Builder
public record AddSourceRequestRules(

        @JsonProperty("item_path") String itemPath,

        @JsonProperty("title_path") String titlePath,

        @JsonProperty("description_path") String descriptionPath,

        @JsonProperty("date_path") String datePath,

        // Формат даты,
        // например: "EEE, dd MMM yyyy HH:mm:ss Z"
        @JsonProperty("date_format") String dateFormat
) {
}
