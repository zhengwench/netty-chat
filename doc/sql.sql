CREATE TABLE `sys_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '权限级别',
  `back_status` int(11) DEFAULT NULL,
  `name` varchar(100) NOT NULL COMMENT '权限名',
  `url` varchar(100) DEFAULT NULL COMMENT '路由',
  `authority` varchar(100) NOT NULL COMMENT '鉴权',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `domain_id` bigint(20) DEFAULT NULL,
  `role_name` varchar(100) NOT NULL,
  `role_icon` varchar(255) DEFAULT NULL,
  `perm_ids` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `domain_id` bigint(20) NOT NULL,
  `user_name` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(50) NOT NULL,
  `room_id` bigint(20) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `role_id` bigint(20) DEFAULT NULL,
  `login_ip` varchar(100) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `sys_token` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `domain_user` (`domain_id`,`user_name`),
  KEY `domain_token` (`domain_id`,`token`),
  KEY `domain_sys_token` (`domain_id`,`sys_token`)
) ENGINE=InnoDB AUTO_INCREMENT=10000000 DEFAULT CHARSET=utf8;

CREATE TABLE `tb_black_ip` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `domain_id` bigint(20) DEFAULT NULL,
  `ip` varchar(100) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_domain_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL,
  `domain_name` varchar(100) NOT NULL COMMENT '域名',
  `websocket_url` varchar(255) DEFAULT NULL,
  `http_url` varchar(255) DEFAULT NULL,
  `public_key` varchar(255) DEFAULT NULL,
  `private_key` varchar(1000) DEFAULT NULL,
  `start_time` datetime NOT NULL COMMENT '服务开始时间',
  `end_time` datetime NOT NULL COMMENT '服务结束时间',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_receive_redbag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `domain_id` bigint(20) DEFAULT NULL,
  `redbag_id` bigint(20) DEFAULT NULL,
  `receive_user_id` bigint(11) DEFAULT NULL,
  `receive_user_name` varchar(255) DEFAULT NULL,
  `send_user_id` bigint(20) DEFAULT NULL,
  `send_user_name` varchar(255) DEFAULT NULL,
  `send_user_icon` varchar(255) DEFAULT NULL,
  `amount` decimal(14,2) NOT NULL,
  `status` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_receive_redbag` (`domain_id`,`redbag_id`,`receive_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=200000000 DEFAULT CHARSET=utf8;

CREATE TABLE `tb_redbag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `domain_id` bigint(20) DEFAULT NULL,
  `room_id` bigint(20) DEFAULT NULL,
  `send_user_id` bigint(20) DEFAULT NULL,
  `send_user_name` varchar(255) DEFAULT NULL,
  `send_user_icon` varchar(255) DEFAULT NULL,
  `amount` decimal(14,2) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_room` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `domain_id` bigint(20) NOT NULL,
  `room_name` varchar(50) DEFAULT NULL,
  `room_type` int(11) DEFAULT NULL,
  `room_logo` varchar(255) DEFAULT NULL,
  `room_pc_bg` varchar(255) DEFAULT NULL,
  `room_mobile_bg` varchar(255) DEFAULT NULL,
  `forbid_status` int(11) DEFAULT NULL,
  `open_room` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `websocket_url` varchar(255) DEFAULT NULL,
  `http_url` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8;

CREATE TABLE `tb_server` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `server_ip` varchar(100) DEFAULT NULL,
  `server_domain` varchar(100) DEFAULT NULL,
  `inner_ip` varchar(100) DEFAULT NULL,
  `http_port` int(11) DEFAULT NULL,
  `websocket_port` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_system_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `domain_id` bigint(20) NOT NULL,
  `sys_group` varchar(100) NOT NULL COMMENT '组名',
  `sys_key` varchar(100) NOT NULL,
  `sys_value` varchar(255) DEFAULT NULL,
  `sys_type` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `del_flag` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tb_user_robot` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `domain_id` bigint(20) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `room_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;