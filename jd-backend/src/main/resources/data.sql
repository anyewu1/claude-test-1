-- Demo user (password: 123456, BCrypt hashed)
INSERT INTO users (username, email, password, phone, role) VALUES
('admin', 'admin@jd.com', '$2a$10$N7NGw98pbOa1BIWYmUHylOIZ27SUqpmvzYe2tsRbOEsxhXL4wcrfK', '13800000001', 'ADMIN'),
('demo', 'demo@jd.com', '$2a$10$N7NGw98pbOa1BIWYmUHylOIZ27SUqpmvzYe2tsRbOEsxhXL4wcrfK', '13800000002', 'USER');

-- Top-level categories
INSERT INTO categories (id, name, icon, parent_id, sort_order) VALUES
(1, '手机数码', '📱', 0, 1),
(2, '电脑办公', '💻', 0, 2),
(3, '家用电器', '🏠', 0, 3),
(4, '服装服饰', '👗', 0, 4),
(5, '食品生鲜', '🥗', 0, 5),
(6, '美妆护肤', '💄', 0, 6),
(7, '运动户外', '⚽', 0, 7),
(8, '图书文具', '📚', 0, 8);

-- Sub-categories
INSERT INTO categories (id, name, icon, parent_id, sort_order) VALUES
(11, '手机', '📱', 1, 1),
(12, '平板电脑', '📟', 1, 2),
(13, '智能手表', '⌚', 1, 3),
(14, '耳机音响', '🎧', 1, 4),
(21, '笔记本电脑', '💻', 2, 1),
(22, '台式机', '🖥️', 2, 2),
(23, '显示器', '🖥️', 2, 3),
(24, '键鼠外设', '⌨️', 2, 4),
(31, '电视', '📺', 3, 1),
(32, '冰箱', '🧊', 3, 2),
(33, '洗衣机', '🫧', 3, 3),
(34, '空调', '❄️', 3, 4);

-- Products - Mobile phones
INSERT INTO products (name, description, price, original_price, stock, sales, category_id, cover_image, images, rating, rating_count, is_flash_sale) VALUES
('Apple iPhone 15 Pro 256GB 深空黑', 'A17 Pro芯片，钛金属设计，专业相机系统，支持USB 3速度，Action按钮，全天候续航。', 7999.00, 8999.00, 500, 15000, 11, 'https://picsum.photos/seed/iphone15/400/400', '["https://picsum.photos/seed/iphone15/400/400","https://picsum.photos/seed/iphone15b/400/400","https://picsum.photos/seed/iphone15c/400/400"]', 4.9, 28456, TRUE),
('Apple iPhone 15 128GB 粉色', '全新A16芯片，Dynamic Island灵动岛，4800万像素主摄，USB-C接口，超视网膜XDR显示屏。', 5999.00, 6499.00, 800, 22000, 11, 'https://picsum.photos/seed/iphone15pink/400/400', '["https://picsum.photos/seed/iphone15pink/400/400","https://picsum.photos/seed/iphone15pinkb/400/400"]', 4.8, 35200, FALSE),
('小米14 Pro 512GB 钛黑', '骁龙8 Gen3芯片，徕卡光学镜头，IP68防水，超声波指纹，全面屏设计，大电池快充。', 4999.00, 5499.00, 1000, 18000, 11, 'https://picsum.photos/seed/mi14pro/400/400', '["https://picsum.photos/seed/mi14pro/400/400","https://picsum.photos/seed/mi14prob/400/400"]', 4.8, 21000, TRUE),
('华为Mate 60 Pro 512GB 雅川青', '麒麟9000S芯片，卫星通话，超强信号，IP68防水防尘，超长续航，超级快充。', 6999.00, 7499.00, 300, 9800, 11, 'https://picsum.photos/seed/mate60/400/400', '["https://picsum.photos/seed/mate60/400/400","https://picsum.photos/seed/mate60b/400/400"]', 4.7, 12300, FALSE),
('OPPO Find X7 Ultra 512GB 海阔天空', '骁龙8 Gen3，哈苏影像，潜望式长焦，大电池，超级快充，超薄机身。', 5999.00, 6499.00, 600, 7500, 11, 'https://picsum.photos/seed/findx7/400/400', '["https://picsum.photos/seed/findx7/400/400"]', 4.7, 8900, FALSE),
('vivo X100 Pro 512GB 深海蓝', '天玑9300芯片，蔡司影像，超大底主摄，潜望式长焦，大电池快充。', 4699.00, 5199.00, 700, 11000, 11, 'https://picsum.photos/seed/vivox100/400/400', '["https://picsum.photos/seed/vivox100/400/400"]', 4.7, 9800, FALSE),
('三星Galaxy S24 Ultra 512GB 钛黑', 'Galaxy AI功能，骁龙8 Gen3，内置S Pen，2亿像素主摄，钛金属边框。', 9999.00, 10999.00, 200, 5600, 11, 'https://picsum.photos/seed/s24ultra/400/400', '["https://picsum.photos/seed/s24ultra/400/400"]', 4.8, 7200, TRUE),
('荣耀Magic6 Pro 512GB 流光青', '骁龙8 Gen3，荣耀鹰眼相机，1.5K超清曲面屏，大电池快充，IP68防水。', 4699.00, 5199.00, 550, 8900, 11, 'https://picsum.photos/seed/magic6/400/400', '["https://picsum.photos/seed/magic6/400/400"]', 4.6, 7600, FALSE);

-- Products - Tablets
INSERT INTO products (name, description, price, original_price, stock, sales, category_id, cover_image, images, rating, rating_count, is_flash_sale) VALUES
('Apple iPad Pro 12.9英寸 256GB WiFi', 'M2芯片，Liquid Retina XDR显示屏，超宽超广角摄像头，雷雳4接口，Face ID。', 8999.00, 9499.00, 300, 4500, 12, 'https://picsum.photos/seed/ipadpro/400/400', '["https://picsum.photos/seed/ipadpro/400/400"]', 4.9, 6800, FALSE),
('小米平板6 Pro 256GB', '骁龙8+ Gen1芯片，2.8K分辨率，120Hz，8600mAh电池，67W快充，金属机身。', 2999.00, 3299.00, 600, 8000, 12, 'https://picsum.photos/seed/mipad6/400/400', '["https://picsum.photos/seed/mipad6/400/400"]', 4.7, 12000, TRUE),
('华为MatePad Pro 13.2英寸', '麒麟9000S，OLED 2.8K屏，超感知触控，卫星通话，大电池，快充。', 4799.00, 5299.00, 200, 3200, 12, 'https://picsum.photos/seed/matepro/400/400', '["https://picsum.photos/seed/matepro/400/400"]', 4.8, 4500, FALSE);

-- Products - Laptops
INSERT INTO products (name, description, price, original_price, stock, sales, category_id, cover_image, images, rating, rating_count, is_flash_sale) VALUES
('Apple MacBook Pro 14英寸 M3 Pro', 'M3 Pro芯片，Liquid Retina XDR显示屏，MagSafe充电，HDMI 2.1，18小时续航。', 14999.00, 15999.00, 150, 3200, 21, 'https://picsum.photos/seed/macbookpro/400/400', '["https://picsum.photos/seed/macbookpro/400/400"]', 4.9, 5600, FALSE),
('联想ThinkPad X1 Carbon i7 32GB 1TB', '英特尔酷睿i7，超薄轻量，军规认证，优秀键盘，长续航，商务首选。', 9999.00, 11999.00, 200, 2800, 21, 'https://picsum.photos/seed/thinkpad/400/400', '["https://picsum.photos/seed/thinkpad/400/400"]', 4.8, 4200, FALSE),
('华为MateBook X Pro 2024 Ultra5 32GB', ' OLED触控屏，超薄机身，3K分辨率，全面屏，指纹解锁，长续航。', 7999.00, 8999.00, 300, 5000, 21, 'https://picsum.photos/seed/matebook/400/400', '["https://picsum.photos/seed/matebook/400/400"]', 4.8, 7800, TRUE),
('小米笔记本Pro 15 i7 16GB 1TB', '英特尔酷睿Ultra7，OLED屏2.8K分辨率，独立显卡，超大散热，长续航。', 5999.00, 6999.00, 400, 6800, 21, 'https://picsum.photos/seed/mibook/400/400', '["https://picsum.photos/seed/mibook/400/400"]', 4.7, 9500, TRUE),
('戴尔XPS 15 i9 32GB 1TB RTX4070', 'i9处理器，RTX4070独显，4K OLED屏，高性能创作本，精工制造。', 16999.00, 18999.00, 100, 1500, 21, 'https://picsum.photos/seed/dellxps/400/400', '["https://picsum.photos/seed/dellxps/400/400"]', 4.8, 2300, FALSE);

-- Products - Earphones
INSERT INTO products (name, description, price, original_price, stock, sales, category_id, cover_image, images, rating, rating_count, is_flash_sale) VALUES
('Apple AirPods Pro 2代 USB-C', '自适应音频，主动降噪，空间音频，MagSafe充电，H2芯片，通透模式。', 1899.00, 2099.00, 1000, 25000, 14, 'https://picsum.photos/seed/airpodspro/400/400', '["https://picsum.photos/seed/airpodspro/400/400"]', 4.9, 45000, TRUE),
('索尼WH-1000XM5 无线降噪耳机', '行业领先降噪，30小时续航，快速充电，多点连接，高解析度音频。', 2299.00, 2799.00, 500, 8900, 14, 'https://picsum.photos/seed/sonywh/400/400', '["https://picsum.photos/seed/sonywh/400/400"]', 4.8, 15600, FALSE),
('小米Buds 4 Pro 真无线耳机', '骨声纹识别，Hi-Res认证，自适应降噪，低延迟，大电池，舒适佩戴。', 599.00, 799.00, 2000, 32000, 14, 'https://picsum.photos/seed/mibuds/400/400', '["https://picsum.photos/seed/mibuds/400/400"]', 4.7, 28000, TRUE);

-- Products - TVs
INSERT INTO products (name, description, price, original_price, stock, sales, category_id, cover_image, images, rating, rating_count, is_flash_sale) VALUES
('小米电视 75英寸 144Hz 4K', '144Hz高刷，4K超清，杜比视界，量子点，AI画质增强，一键投屏。', 3999.00, 4999.00, 300, 5600, 31, 'https://picsum.photos/seed/mitv/400/400', '["https://picsum.photos/seed/mitv/400/400"]', 4.7, 8900, TRUE),
('海信ULED X 65英寸 MiniLED', 'MiniLED背光，4K 144Hz，杜比全景声，高亮度，超广色域，AI画质。', 5999.00, 6999.00, 150, 2300, 31, 'https://picsum.photos/seed/hisense/400/400', '["https://picsum.photos/seed/hisense/400/400"]', 4.8, 3200, FALSE),
('三星Neo QLED 8K 85英寸', '8K分辨率，量子矩阵技术，神经量子处理器8K，杜比全景声，旗舰之选。', 39999.00, 45999.00, 50, 450, 31, 'https://picsum.photos/seed/samsungtv/400/400', '["https://picsum.photos/seed/samsungtv/400/400"]', 4.9, 890, FALSE);

-- Products - Clothing
INSERT INTO products (name, description, price, original_price, stock, sales, category_id, cover_image, images, rating, rating_count, is_flash_sale) VALUES
('优衣库超轻型羽绒服 男款', '超轻薄设计，保暖不臃肿，防风防水，可折叠收纳，多色可选。', 599.00, 799.00, 2000, 15000, 41, 'https://picsum.photos/seed/uniqlo/400/400', '["https://picsum.photos/seed/uniqlo/400/400"]', 4.6, 22000, FALSE),
('NIKE Air Max 270 运动鞋', '大气垫缓震，透气网面，经典设计，轻质舒适，适合日常运动。', 999.00, 1299.00, 500, 8900, 71, 'https://picsum.photos/seed/nikeair/400/400', '["https://picsum.photos/seed/nikeair/400/400"]', 4.7, 12000, TRUE),
('adidas Ultra Boost 22 跑鞋', 'Boost中底，PRIMEKNIT鞋面，Continental橡胶外底，专业跑步鞋。', 1099.00, 1499.00, 400, 6700, 71, 'https://picsum.photos/seed/adidas/400/400', '["https://picsum.photos/seed/adidas/400/400"]', 4.8, 9800, FALSE);

-- Products - Food
INSERT INTO products (name, description, price, original_price, stock, sales, category_id, cover_image, images, rating, rating_count, is_flash_sale) VALUES
('褚橙 云南冰糖橙 精品5斤装', '产自云南哀牢山，冰糖橙品种，皮薄汁多，甜度高，新鲜直采。', 49.00, 69.00, 5000, 35000, 51, 'https://picsum.photos/seed/orange/400/400', '["https://picsum.photos/seed/orange/400/400"]', 4.8, 52000, TRUE),
('盒马有机三文鱼 挪威进口 500g', '挪威原产地直采，有机认证，新鲜冷链配送，可直接生食，肉质鲜美。', 99.00, 129.00, 1000, 18000, 51, 'https://picsum.photos/seed/salmon/400/400', '["https://picsum.photos/seed/salmon/400/400"]', 4.7, 25000, FALSE);

-- Products - Beauty
INSERT INTO products (name, description, price, original_price, stock, sales, category_id, cover_image, images, rating, rating_count, is_flash_sale) VALUES
('雅诗兰黛 小棕瓶精华 50ml', '第七代配方，ANF因子，修护细胞，改善暗沉，提亮肤色，抗老精华。', 850.00, 1050.00, 800, 12000, 61, 'https://picsum.photos/seed/estee/400/400', '["https://picsum.photos/seed/estee/400/400"]', 4.8, 18000, FALSE),
('兰蔻菁纯面霜 50ml', '玫瑰精华，深层滋养，淡化皱纹，提升紧致度，奢华护肤。', 760.00, 960.00, 600, 8900, 61, 'https://picsum.photos/seed/lancome/400/400', '["https://picsum.photos/seed/lancome/400/400"]', 4.7, 11000, TRUE);

-- Demo addresses for demo user (id=2)
INSERT INTO addresses (user_id, name, phone, province, city, district, detail, is_default) VALUES
(2, '张三', '13800138000', '广东省', '深圳市', '南山区', '科技园南区8栋101室', TRUE),
(2, '李四', '13900139000', '上海市', '上海市', '浦东新区', '陆家嘴金融城1号楼', FALSE);

-- Sample reviews
INSERT INTO reviews (product_id, user_id, rating, content) VALUES
(1, 2, 5, '商品质量非常好，物流很快，第二天就到了，包装也很完整，非常满意！'),
(1, 1, 4, '整体体验不错，性能强劲，就是价格有点贵，但京东正品有保障。'),
(2, 2, 5, '买了很久了，一直用得很好，强烈推荐！售后服务也很贴心。'),
(2, 1, 4, '粉色颜值很高，Dynamic Island用起来很顺手。'),
(3, 2, 5, '苹果Watch Ultra太棒了，续航比普通Watch强多了，很值！'),
(4, 1, 5, 'AirPods Pro降噪效果绝了，通勤必备，强烈推荐！'),
(5, 2, 4, 'MacBook Air M3性能超强，轻薄便携，就是价格贵一点。');
