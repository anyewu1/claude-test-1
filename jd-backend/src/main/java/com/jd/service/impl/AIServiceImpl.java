package com.jd.service.impl;

import com.jd.dto.AIRecommendRequest;
import com.jd.dto.ChatRequest;
import com.jd.entity.Product;
import com.jd.service.AIService;
import com.jd.service.EmbeddingService;
import com.jd.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AIServiceImpl implements AIService {

    private static final String SYSTEM_PROMPT = """
            你是 JD 电商平台的智能客服助手，名叫"京小智"。
            你的职责：
            1. 解答商品咨询、推荐合适的商品
            2. 解答购物、支付、配送、退换货等问题

            重要规则（必须严格遵守）：
            1. 你只服务于本平台。绝对不要建议用户去京东官网（jd.com）、淘宝、天猫、拼多多等任何外部网站购买
            2. 推荐商品时，只能从【参考商品】列表里挑选，按商品名称引用即可，不要编造商品
            3. 如果【参考商品】为空或与用户需求无关，请告知用户当前平台暂无完全匹配的商品，建议换个关键词或浏览分类页
            4. 不要捏造价格和库存，以页面显示为准
            5. 语气友好、专业、简洁，单条回复不超过 4 句话
            """;

    private static final String[] SHOPPING_KEYWORDS = {
            "买", "购", "推荐", "想要", "需要", "选", "挑", "找", "求", "入手",
            "笔记本", "电脑", "手机", "平板", "耳机", "相机", "手表", "音响", "键盘", "鼠标",
            "显示器", "路由器", "充电", "电视", "冰箱", "洗衣机", "空调",
            "苹果", "iPhone", "MacBook", "iPad", "AirPods", "Apple",
            "华为", "小米", "OPPO", "vivo", "三星", "联想", "戴尔", "惠普",
            "性价比", "便宜", "实惠", "预算"
    };

    private final ProductService productService;
    private final EmbeddingService embeddingService;
    private ChatClient chatClient;

    public AIServiceImpl(ProductService productService,
                         EmbeddingService embeddingService,
                         @Autowired(required = false) ChatClient.Builder chatClientBuilder) {
        this.productService = productService;
        this.embeddingService = embeddingService;
        if (chatClientBuilder != null) {
            try {
                this.chatClient = chatClientBuilder
                        .defaultSystem(SYSTEM_PROMPT)
                        .build();
            } catch (Exception e) {
                log.warn("ChatClient init failed, AI features will use fallback: {}", e.getMessage());
            }
        }
    }

    @Override
    public Map<String, Object> chat(ChatRequest request) {
        Map<String, Object> result = new HashMap<>();
        String userMsg = request.getMessage() == null ? "" : request.getMessage();
        List<Product> matched = findRelatedProducts(userMsg);
        String reply;

        if (chatClient != null) {
            try {
                List<Message> messages = new ArrayList<>();

                if (!matched.isEmpty()) {
                    String catalog = matched.stream()
                            .limit(6)
                            .map(p -> "- " + p.getName()
                                    + "，价格¥" + p.getPrice()
                                    + "，评分" + p.getRating()
                                    + "，月销" + (p.getSales() == null ? 0 : p.getSales()))
                            .collect(Collectors.joining("\n"));
                    messages.add(new SystemMessage(
                            "【参考商品（本平台在售，推荐时只能从下面挑选）】：\n" + catalog
                    ));
                }

                if (request.getHistory() != null) {
                    for (Map<String, String> msg : request.getHistory()) {
                        String role = msg.get("role");
                        String content = msg.get("content");
                        if (content == null) continue;
                        if ("user".equals(role)) {
                            messages.add(new UserMessage(content));
                        } else if ("assistant".equals(role)) {
                            messages.add(new AssistantMessage(content));
                        }
                    }
                }
                messages.add(new UserMessage(userMsg));

                reply = chatClient.prompt(new Prompt(messages)).call().content();
            } catch (Exception e) {
                log.warn("AI chat failed, using fallback: {}", e.getMessage(), e);
                reply = generateFallbackReply(userMsg, matched);
            }
        } else {
            reply = generateFallbackReply(userMsg, matched);
        }

        result.put("reply", reply);
        result.put("products", matched.stream().limit(4).collect(Collectors.toList()));
        return result;
    }

    @Override
    public List<String> suggest(String keyword, int limit) {
        List<String> suggestions = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of("手机", "笔记本电脑", "耳机", "平板", "智能手表");
        }
        String kw = keyword.trim();
        suggestions.add(kw + "推荐");
        suggestions.add(kw + "排行榜");
        suggestions.add(kw + "价格");
        suggestions.add("最新款" + kw);
        suggestions.add(kw + "优惠");
        return suggestions.stream().limit(limit).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> recommend(AIRecommendRequest request) {
        Map<String, Object> result = new HashMap<>();
        String analysis;

        List<Product> products = productService.search(
                1, request.getLimit(), null, extractKeyword(request.getQuery()),
                null, request.getBudget(), "rating", "desc"
        ).getRecords();

        if (chatClient != null) {
            try {
                String productSummary = products.stream()
                        .map(p -> p.getName() + "（¥" + p.getPrice() + "，评分" + p.getRating() + "）")
                        .collect(Collectors.joining("、"));
                String prompt = String.format(
                        "用户需求：%s%s\n候选商品：%s\n请用2-3句话分析并推荐，语气友好，只能推荐上面列出的商品，不要去外部网站。",
                        request.getQuery(),
                        request.getBudget() != null ? "，预算" + request.getBudget() + "元" : "",
                        productSummary.isEmpty() ? "暂无匹配商品" : productSummary
                );
                analysis = chatClient.prompt(prompt).call().content();
            } catch (Exception e) {
                log.warn("AI recommend failed, using fallback: {}", e.getMessage(), e);
                analysis = generateRecommendFallback(request.getQuery(), products);
            }
        } else {
            analysis = generateRecommendFallback(request.getQuery(), products);
        }

        result.put("analysis", analysis);
        result.put("products", products);
        return result;
    }

    private List<Product> findRelatedProducts(String message) {
        if (message == null || message.isBlank()) return List.of();
        if (!hasShoppingIntent(message)) return List.of();

        try {
            if (embeddingService != null && embeddingService.isAvailable()) {
                List<Product> products = embeddingService.semanticSearch(message, 6);
                if (products != null && !products.isEmpty()) return products;
            }
        } catch (Exception e) {
            log.warn("Semantic search failed in chat, falling back to keyword: {}", e.getMessage());
        }

        try {
            String keyword = extractKeyword(message);
            if (keyword == null || keyword.isBlank()) return List.of();
            return productService.search(1, 6, null, keyword, null, null, "rating", "desc")
                    .getRecords();
        } catch (Exception e) {
            log.warn("Keyword search failed in chat: {}", e.getMessage());
            return List.of();
        }
    }

    private boolean hasShoppingIntent(String text) {
        if (text == null) return false;
        String lower = text.toLowerCase();
        for (String kw : SHOPPING_KEYWORDS) {
            if (lower.contains(kw.toLowerCase())) return true;
        }
        return false;
    }

    private String extractKeyword(String query) {
        if (query == null) return null;
        String[] stopWords = {"我想买", "我要买", "想买", "要买", "推荐", "一下", "合适",
                "一款", "一个", "适合", "预算", "左右", "的", "请", "帮我", "给我"};
        String result = query;
        for (String sw : stopWords) {
            result = result.replace(sw, " ");
        }
        String[] tokens = result.trim().split("\\s+");
        return tokens.length > 0 ? tokens[0] : result.trim();
    }

    private String generateFallbackReply(String message, List<Product> matched) {
        if (!matched.isEmpty()) {
            Product top = matched.get(0);
            return String.format("根据您的需求，本平台为您找到以下推荐：【%s】（¥%s，评分%s）。下方卡片可直接点击查看详情。",
                    top.getName(), top.getPrice(), top.getRating());
        }
        if (message.contains("退款") || message.contains("退货")) {
            return "您好！本平台支持7天无理由退货，可在【我的订单】中申请，1-3个工作日处理完毕，退款原路返回。";
        } else if (message.contains("配送") || message.contains("快递") || message.contains("发货")) {
            return "您好！本平台自营商品支持次日达，部分地区当日达，订单详情页可查物流。";
        } else if (message.contains("优惠") || message.contains("活动") || message.contains("打折")) {
            return "您好！本平台目前有多项优惠活动，可在结算页输入优惠码（如 WELCOME10、SAVE20）享受满减。";
        }
        return "您好！我是京小智，请问您想了解什么商品，或者有什么购物问题需要帮助？";
    }

    private String generateRecommendFallback(String query, List<Product> products) {
        if (products.isEmpty()) {
            return "根据您的需求【" + query + "】，本平台暂时未找到完全匹配的商品，建议您调整关键词或预算范围。";
        }
        Product top = products.get(0);
        return String.format("根据您的需求【%s】，本平台为您精选了%d款商品。其中【%s】最受好评，评分%s分，销量%d件，性价比出色，值得考虑！",
                query, products.size(), top.getName(), top.getRating(), top.getSales() == null ? 0 : top.getSales());
    }
}
