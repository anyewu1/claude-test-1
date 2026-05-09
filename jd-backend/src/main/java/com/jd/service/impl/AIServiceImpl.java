package com.jd.service.impl;

import com.jd.dto.AIRecommendRequest;
import com.jd.dto.ChatRequest;
import com.jd.entity.Product;
import com.jd.service.AIService;
import com.jd.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AIServiceImpl implements AIService {

    private final ProductService productService;
    private ChatClient chatClient;

    public AIServiceImpl(ProductService productService,
                         @Autowired(required = false) ChatClient.Builder chatClientBuilder) {
        this.productService = productService;
        if (chatClientBuilder != null) {
            try {
                this.chatClient = chatClientBuilder
                        .defaultSystem("""
                                你是JD（京东）电商平台的智能客服助手，名叫"京小智"。
                                你的职责是帮助用户：
                                1. 解答商品咨询问题
                                2. 根据用户需求推荐合适的商品
                                3. 解答购物、支付、配送相关问题
                                4. 处理售后问题咨询
                                请用友好、专业的语气回复，回复要简洁明了。
                                如果涉及具体价格或库存，请告知用户以实际页面为准。
                                """)
                        .build();
            } catch (Exception e) {
                log.warn("ChatClient init failed, AI features will use fallback: {}", e.getMessage());
            }
        }
    }

    @Override
    public Map<String, Object> chat(ChatRequest request) {
        Map<String, Object> result = new HashMap<>();
        String reply;

        if (chatClient != null) {
            try {
                List<Message> messages = new ArrayList<>();
                if (request.getHistory() != null) {
                    for (Map<String, String> msg : request.getHistory()) {
                        String role = msg.get("role");
                        String content = msg.get("content");
                        if ("user".equals(role)) {
                            messages.add(new UserMessage(content));
                        } else if ("assistant".equals(role)) {
                            messages.add(new AssistantMessage(content));
                        }
                    }
                }
                messages.add(new UserMessage(request.getMessage()));

                Prompt prompt = new Prompt(messages);
                reply = chatClient.prompt(prompt).call().content();
            } catch (Exception e) {
                log.warn("AI chat failed, using fallback: {}", e.getMessage());
                reply = generateFallbackReply(request.getMessage());
            }
        } else {
            reply = generateFallbackReply(request.getMessage());
        }

        result.put("reply", reply);
        result.put("products", extractProductMentions(reply));
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
                        "用户需求：%s%s\n候选商品：%s\n请用2-3句话分析并推荐，语气友好。",
                        request.getQuery(),
                        request.getBudget() != null ? "，预算" + request.getBudget() + "元" : "",
                        productSummary.isEmpty() ? "暂无匹配商品" : productSummary
                );
                analysis = chatClient.prompt(prompt).call().content();
            } catch (Exception e) {
                log.warn("AI recommend failed, using fallback: {}", e.getMessage());
                analysis = generateRecommendFallback(request.getQuery(), products);
            }
        } else {
            analysis = generateRecommendFallback(request.getQuery(), products);
        }

        result.put("analysis", analysis);
        result.put("products", products);
        return result;
    }

    private String extractKeyword(String query) {
        if (query == null) return null;
        String[] stopWords = {"我想买", "推荐", "合适", "一款", "一个", "适合", "预算", "左右", "的"};
        String result = query;
        for (String sw : stopWords) {
            result = result.replace(sw, " ");
        }
        return result.trim().split("\\s+")[0];
    }

    private String generateFallbackReply(String message) {
        if (message.contains("手机") || message.contains("iPhone") || message.contains("华为")) {
            return "您好！关于手机推荐，我们平台有丰富的选择。热门机型包括iPhone 15系列、华为Mate 60、小米14等。请问您的预算范围是多少？对拍照、游戏还是续航更看重？这样我可以为您精准推荐。";
        } else if (message.contains("退款") || message.contains("退货")) {
            return "您好！关于退款退货，京东支持7天无理由退货。您可以在【我的订单】中申请退款，一般1-3个工作日处理完毕，退款会原路返回。如需人工协助，请联系我们的客服热线。";
        } else if (message.contains("配送") || message.contains("快递") || message.contains("发货")) {
            return "您好！京东自营商品支持次日达，部分地区当日达。您可以在订单详情页查看物流信息。如有配送问题，请提供您的订单编号，我来帮您查询。";
        } else if (message.contains("优惠") || message.contains("活动") || message.contains("打折")) {
            return "您好！我们目前有多项优惠活动正在进行，包括新人专享、满减优惠、限时闪购等。建议您关注首页的活动横幅，也可以在商品页面查看当前优惠信息。";
        } else {
            return "您好！我是京东智能客服京小智，很高兴为您服务！请问您想了解什么商品，或者有什么购物问题需要帮助？";
        }
    }

    private String generateRecommendFallback(String query, List<Product> products) {
        if (products.isEmpty()) {
            return "根据您的需求【" + query + "】，暂时未找到完全匹配的商品，建议您尝试调整搜索关键词或预算范围。";
        }
        Product top = products.get(0);
        return String.format("根据您的需求【%s】，为您精选了%d款商品。其中【%s】最受好评，评分高达%s分，销量%d件，性价比出色，值得考虑！",
                query, products.size(), top.getName(), top.getRating(), top.getSales());
    }

    private List<Product> extractProductMentions(String reply) {
        return List.of();
    }
}
