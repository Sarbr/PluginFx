CREATE TABLE if not exists RSS(
                    id bigint auto_increment  NOT NULL COMMENT '主键ID',
                    order_no int(11) NULL DEFAULT NULL COMMENT '排序',
                    name VARCHAR(30) NULL DEFAULT NULL COMMENT '名称',
                    url VARCHAR(100) NULL DEFAULT NULL COMMENT '链接',
                    state VARCHAR(100) NULL DEFAULT NULL COMMENT '状态',
                    PRIMARY KEY (id)
);
CREATE TABLE if not exists SYS_PARAM(
                    id bigint auto_increment  NOT NULL COMMENT '主键ID',
                    title VARCHAR(200) NULL DEFAULT NULL COMMENT '键',
                    type VARCHAR(30) NULL DEFAULT NULL COMMENT '类型',
                    key VARCHAR(200) NULL DEFAULT NULL COMMENT '键',
                    val VARCHAR(500) NULL DEFAULT NULL COMMENT '值',
                    note VARCHAR(1000) NULL DEFAULT NULL COMMENT '说明',
                    PRIMARY KEY (id)
);