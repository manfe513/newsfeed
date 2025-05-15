package com.vasilev.news.parser;

import com.vasilev.news.model.db.FeedEntity;
import com.vasilev.news.model.db.SourceRulesEntity;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedParser {

    @SneakyThrows
    public List<FeedEntity> parse(
            String sourceName,
            String rawContent,
            SourceRulesEntity rules
    ) {

        val factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);

        DocumentBuilder db = factory.newDocumentBuilder();
        Document document = db.parse(
                new ByteArrayInputStream(rawContent.getBytes())
        );

        document.getDocumentElement().normalize();

        List<FeedEntity> feedEntities = new ArrayList<>();

        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();


        NodeList items = (NodeList) xpath.evaluate(rules.getItemPath(), document, XPathConstants.NODESET);

        for (int i = 0; i < items.getLength(); i++) {
            Node itemNode = items.item(i);

            Node titleNode = (Node) xpath.evaluate(rules.getTitlePath(), itemNode, XPathConstants.NODE);
            Node descNode = (Node) xpath.evaluate(rules.getDescriptionPath(), itemNode, XPathConstants.NODE);
            Node dateNode = (Node) xpath.evaluate(rules.getDatePath(), itemNode, XPathConstants.NODE);

            FeedEntity feedEntity = FeedEntity.builder()
                    .date(
                            OffsetDateTime.from(
                                    DateTimeFormatter.ofPattern(rules.getDateFormat())
                                            .parse(dateNode.getTextContent())
                            ).toInstant()
                    )
                    .source(sourceName)
                    .title(titleNode.getTextContent())
                    .description(descNode.getTextContent())
                    .build();

            log.info("parsed entity: {}", feedEntity);

            feedEntities.add(feedEntity);
        }

        return feedEntities;
    }
}
