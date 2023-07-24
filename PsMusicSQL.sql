/*
SQLyog Ultimate v13.1.1 (64 bit)
MySQL - 8.0.21 : Database - psmusic
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`psmusic` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `psmusic`;

/*Table structure for table `admin` */

DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='管理员';

/*Data for the table `admin` */

insert  into `admin`(`id`,`name`,`password`) values 
(1,'admin','201941030204');

/*Table structure for table `collect` */

DROP TABLE IF EXISTS `collect`;

CREATE TABLE `collect` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int DEFAULT NULL COMMENT '用户id',
  `type` tinyint(1) DEFAULT NULL COMMENT '收藏类型（0表示歌曲，1表示歌单）',
  `song_id` int DEFAULT NULL COMMENT '歌曲id',
  `song_list_id` int DEFAULT NULL COMMENT '歌单id',
  `create_time` datetime DEFAULT NULL COMMENT '收藏时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='收藏';

/*Data for the table `collect` */

/*Table structure for table `comment` */

DROP TABLE IF EXISTS `comment`;

CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int DEFAULT NULL COMMENT '用户id',
  `type` tinyint(1) DEFAULT NULL COMMENT '评论类型（0表示歌曲，1表示歌单）',
  `song_id` int DEFAULT NULL COMMENT '歌曲id',
  `song_list_id` int DEFAULT NULL COMMENT '歌单id',
  `content` varchar(255) DEFAULT NULL COMMENT '评论内容',
  `create_time` datetime DEFAULT NULL COMMENT '评论时间',
  `up` int DEFAULT '0' COMMENT '评论点赞数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COMMENT='评论';

/*Data for the table `comment` */

insert  into `comment`(`id`,`user_id`,`type`,`song_id`,`song_list_id`,`content`,`create_time`,`up`) values 
(1,9,1,NULL,1,'测试','2023-07-18 22:25:19',14),
(8,9,1,NULL,1,'呵呵','2023-07-19 14:01:38',5),
(9,1,1,NULL,1,'评论自动刷新','2023-07-19 16:37:29',4),
(10,1,1,NULL,1,'再来一次','2023-07-19 16:40:34',4),
(11,1,1,NULL,1,'再来','2023-07-19 16:42:20',1),
(12,6,1,NULL,1,'哈哈哈，成功','2023-07-19 16:53:25',1),
(13,6,1,NULL,1,'再来试试','2023-07-19 16:55:00',1),
(14,6,1,NULL,1,'实时','2023-07-19 16:56:44',1),
(15,6,1,NULL,1,'好了吗','2023-07-19 16:57:47',1),
(16,6,1,NULL,1,'有点假','2023-07-19 16:57:56',1),
(17,6,1,NULL,1,'好了吗','2023-07-19 16:58:22',1),
(18,6,1,NULL,1,'好了','2023-07-19 16:58:27',1),
(19,6,1,NULL,1,'还是不行吗','2023-07-19 16:58:34',1),
(20,6,1,NULL,1,'测试','2023-07-19 17:06:21',0),
(21,6,1,NULL,1,'测试9999','2023-07-19 17:06:48',0),
(22,6,1,NULL,1,'测试000000000000','2023-07-19 17:07:29',1),
(23,6,1,NULL,1,'测试102012012','2023-07-19 17:09:05',0),
(24,6,1,NULL,1,'真的好了吗','2023-07-19 17:09:13',0),
(25,6,1,NULL,1,'是真的','2023-07-19 17:09:18',0),
(26,6,1,NULL,1,'才怪','2023-07-19 17:09:29',0),
(27,6,1,NULL,2,'不想再试了','2023-07-19 17:15:38',0),
(28,6,1,NULL,2,'真的吗','2023-07-19 17:15:43',0),
(29,6,1,NULL,2,'假的','2023-07-19 17:15:50',0),
(30,6,1,NULL,2,'谁信','2023-07-19 17:15:55',0),
(31,6,1,NULL,2,'我信','2023-07-19 17:16:03',0),
(32,6,1,NULL,2,'那好','2023-07-19 17:16:11',0),
(33,6,1,NULL,7,'呵呵呵了','2023-07-19 18:56:16',1);

/*Table structure for table `comment_up` */

DROP TABLE IF EXISTS `comment_up`;

CREATE TABLE `comment_up` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NOT NULL COMMENT '点赞用户id',
  `comment_id` int NOT NULL COMMENT '评论id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `USER_COMMENT` (`user_id`,`comment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8;

/*Data for the table `comment_up` */

insert  into `comment_up`(`id`,`user_id`,`comment_id`) values 
(1,1,1),
(28,1,8),
(80,6,1),
(85,6,8),
(90,6,9),
(97,6,10),
(100,6,33),
(70,9,8),
(61,9,9),
(40,9,10),
(41,9,11),
(101,9,22);

/*Table structure for table `consumer` */

DROP TABLE IF EXISTS `consumer`;

CREATE TABLE `consumer` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(255) DEFAULT NULL COMMENT '账号',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `sex` tinyint(1) DEFAULT NULL COMMENT '性别（1男，0女）',
  `phone_num` char(15) DEFAULT NULL COMMENT '电话',
  `email` char(30) DEFAULT NULL COMMENT '电子邮箱',
  `birth` datetime DEFAULT NULL COMMENT '生日',
  `introduction` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '签名',
  `location` varchar(255) DEFAULT NULL COMMENT '地区',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='前端用户';

/*Data for the table `consumer` */

insert  into `consumer`(`id`,`username`,`password`,`sex`,`phone_num`,`email`,`birth`,`introduction`,`location`,`avatar`,`create_time`,`update_time`) values 
(1,'Pluto','123456',1,'11111111111','1111111111@qq.com','2023-07-13 00:00:00','我命油我不油天！','深圳','/img/consumerAvatar/1689347253265head.jpg','2023-07-14 23:06:00','2023-07-14 23:07:33'),
(10,'蛋蛋','12345678',0,'18228267567','1287172@qq.com','2023-07-05 00:00:00','说些什么，又不知道说啥，又想说几句！','北京','/img/consumerAvatar/user.jpg','2023-07-20 22:24:20','2023-07-20 22:24:20');

/*Table structure for table `list_song` */

DROP TABLE IF EXISTS `list_song`;

CREATE TABLE `list_song` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `song_id` int DEFAULT NULL COMMENT '歌曲id',
  `song_list_id` int DEFAULT NULL COMMENT '歌单id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='歌单包含歌曲列表';

/*Data for the table `list_song` */

insert  into `list_song`(`id`,`song_id`,`song_list_id`) values 
(20,18,7),
(21,17,1),
(22,20,1);

/*Table structure for table `rank_score` */

DROP TABLE IF EXISTS `rank_score`;

CREATE TABLE `rank_score` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `song_list_id` int NOT NULL COMMENT '歌单id',
  `consumer_id` int NOT NULL COMMENT '用户id',
  `score` int DEFAULT NULL COMMENT '评分',
  PRIMARY KEY (`id`),
  UNIQUE KEY `consumer_id` (`song_list_id`,`consumer_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='评价';

/*Data for the table `rank_score` */

insert  into `rank_score`(`id`,`song_list_id`,`consumer_id`,`score`) values 
(2,1,9,6),
(13,7,6,5),
(15,10,9,9);

/*Table structure for table `singer` */

DROP TABLE IF EXISTS `singer`;

CREATE TABLE `singer` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `sex` tinyint(1) DEFAULT NULL COMMENT '性别（1男，0女，2组合，3不明）',
  `pic` varchar(255) DEFAULT NULL COMMENT '头像',
  `birth` datetime DEFAULT NULL COMMENT '生日',
  `location` varchar(255) DEFAULT NULL COMMENT '地区',
  `introduction` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '简介',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8 COMMENT='歌手';

/*Data for the table `singer` */

insert  into `singer`(`id`,`name`,`sex`,`pic`,`birth`,`location`,`introduction`) values 
(1,'刘德华',1,'/img/singerPic/1689173873099华仔.jpg','1961-09-27 00:00:00','中国','刘德华（Andy Lau），1961年9月出生于中国香港，中国知名演员、歌手、词作人、制片人、电影人，影视歌多栖发展的代表艺人之一。'),
(9,'哆啦A梦',1,'/img/singerPic/1689174774679avatar2.png','2023-07-18 00:00:00','日本','大山版《哆啦A梦》（ドラえもん）是根据日本漫画家藤子·F·不二雄同名作品改编的动画剧，因其主要配音阵容由大山羡代等人组成而得名。'),
(26,'周杰伦',1,'/img/singerPic/1689863799170zhoujielun.jpg','1979-01-18 00:00:00','中国','周杰伦（Jay Chou），1979年1月18日出生于台湾省新北市，祖籍福建省泉州市永春县，中国台湾流行乐男歌手、音乐人、演员、导演、编剧，毕业于淡江中学。\n2000年发行个人首张专辑《Jay》。2001年发行的专辑《范特西》奠定其融合中西方音乐的风格。2002年举行“The One”世界巡回演唱会 。2003年成为美国《时代周刊》封面人物 。2004年发行个人专辑《七里香》。2005年凭借动作片《头文字D》获得金马奖、金像奖最佳新人奖。2006年起连续三年获得世界音乐大奖中国区最畅销艺人奖 ；同年发行个人专辑《依然范特西》。2007年自编自导的文艺片《不能说的秘密》获得金马奖年度台湾杰出电影奖；同年发行个人专辑《我很忙》。2008年凭借歌曲《青花瓷》获得第19届台湾金曲奖最佳年度歌曲奖、最佳作曲人奖。\n2009年入选美国CNN评出的“25位亚洲最具影响力人物” ；同年凭借专辑《魔杰座》获得第20届台湾金曲奖最佳国语男歌手奖。2010年入选美国《Fast Company》评出的“全球百大创意人物”；同年发行个人专辑《跨时代》，凭借该专辑获得第22届台湾金曲奖最佳国语男歌手奖、最佳国语专辑奖 。2012年登上福布斯中国名人榜榜首。2014年发行个人首张数字专辑《哎呦，不错哦》。2023年凭借专辑《最伟大的作品》获得IFPI全球畅销专辑榜冠军 。\n演艺事业外，他还涉足商业、设计等领域。2007年成立杰威尔有限公司。2011年担任华硕笔电设计师。\n周杰伦热心公益慈善，多次向中国内地灾区捐款捐物。2008年捐款援建希望小学 。'),
(27,'胡歌',1,'/img/singerPic/1689864394037huge.jpg','1982-09-20 00:00:00','中国','中国内地影视男演员、流行乐歌手\n胡歌，1982年9月20日出生于上海市徐汇区，中国内地影视男演员、流行乐歌手，民盟盟员，毕业于上海戏剧学院表演系。1996年，14岁的胡歌便成为上海教育电视台的小主持人。2005年在仙侠剧《仙剑奇侠传》中塑造了“李逍遥”一角，并演唱该剧插曲《六月的雨》《逍遥叹》。2006年8月遭受严重车祸，2007年6月复出。2009年主演仙侠剧《仙剑奇侠传三》，并演唱片尾曲《忘记时间》等等。'),
(28,'王恩信Est,二胖u',2,'/img/singerPic/hhh.jpg','1972-07-21 00:00:00','中国','组合主要作品有《你走以后 1.0》');

/*Table structure for table `song` */

DROP TABLE IF EXISTS `song`;

CREATE TABLE `song` (
  `id` int NOT NULL AUTO_INCREMENT,
  `singer_id` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `introduction` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `pic` varchar(255) DEFAULT NULL,
  `lyric` text,
  `url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

/*Data for the table `song` */

insert  into `song`(`id`,`singer_id`,`name`,`introduction`,`create_time`,`update_time`,`pic`,`lyric`,`url`) values 
(1,2,'22-烟','《烟》','2023-07-13 19:51:06','2023-07-16 21:50:09','/img/songPic/1689515409979微信图片_202307152144353.jpg','[00:01.10]烟\n[00:02.45]曲：まきちゃんぐ\n[00:03.48]填词：Vagary\n[00:05.02]演唱：晃儿&慕寒\n[00:08.03]\n[00:25.37]晃儿：只是一点流连 霞彩的变幻\n[00:32.00]恍然不觉之间 流光已向晚\n[00:38.36]期待放在等待之前 是依恋是习惯\n[00:44.76]亲吻停在拥抱对面 有无言没诺言\n[00:51.26]\n[00:53.03]慕寒：指间凉烟几时烧完\n[00:56.07]芳香黯淡 我的眼看你的脸 看不见\n[01:03.56]\n[01:05.56]晃儿：月色空燃 镜里桃花明艳 响板红檀\n[01:12.00]一步千年 樱桃莲萼疲倦 深雪优昙\n[01:18.83]慕寒:若能不见 遇不见\n[01:22.60]等不见 望不见 便 释怀这场奈何禅\n[01:27.68]但将无怨 作无缘\n[01:32.31]\n[01:44.00]晃儿：鬓边芳华一线 逍遥换是不换\n[01:50.87]掌间玲珑权变 此心断是不断\n[01:56.43]慕寒:胸怀冰冷恍若无关 谁的眼谁在看\n[02:03.20]舌尖血痕痛得甘甜 谁的唇吻得干\n[02:10.32]\n[02:12.01]晃儿：半支凉烟含泪轻衔\n[02:14.92]点数这无眠 月中天白沙水岸 五更寒\n[02:24.08]\n[02:24.63]慕寒：天意人间 凭我信手打乱 浮生沙盘\n[02:31.01]晃儿：绿水青山 夜来秀色难辨 苟且偷安\n[02:37.64]合：只这一寸 小心愿 与君言\n[02:42.49]温存暂 却 一晌贪欢空缠绵\n[02:46.70]只将遗憾 作应然\n[02:50.98]----------------------------\n[03:17.00]合：月色空燃 镜里桃花明艳 响板红檀\n[03:23.81]一步千年 樱桃莲萼疲倦 深雪优昙\n[03:29.99]若能不见 遇不见 等不见\n[03:35.54]望不见 便 释怀这场奈何禅\n[03:39.19]但将无怨 作无缘\n[03:43.00]合：褪色凉烟仍在唇边 酒未完\n[03:43.44]\n[03:48.46]泪未干 笑嫣然 忆华年\n[03:53.49]错拂弦 锦瑟无端\n[03:56.45]\n[03:56.96]合：只这一寸 小心愿 与君言\n[04:01.45]温存暂 却 一晌贪欢空缠绵\n[04:05.25]修来这场奈何禅\n[04:11.28]\n[04:13.19]合：若能不见 遇不见 等不见\n[04:18.20]望不见 便 释怀这风流无限\n[04:22.54]烧尽凉烟思悄然\n[04:32.04]','/song/1689260657489王恩信Est,二胖u - 你走以后1.0.mp3'),
(2,2,'22-111','22222','2023-07-13 19:52:29','2023-07-17 18:43:57','/img/songPic/1689515397056微信图片_202307152144352.jpg','[00:00.00]2222222\n[00:01.00]3333333\n[00:02.01]4444444','/song/1689249149526月牙儿.mp3'),
(14,3,'3-假的','《假的》','2023-07-15 16:00:31','2023-07-16 21:43:08','/img/songPic/1689514988779微信图片_202307152144372.jpg','没有','/song/1689408031432王恩信Est,二胖u - 你走以后1.0.mp3'),
(15,9,'哆啦A梦-哆啦A梦','《哆啦A梦》','2023-07-16 14:59:58','2023-07-16 15:00:10','/img/songPic/1689490810481avatar2.png','[00:00.00]暂无歌词','/song/1689490798692王恩信Est,二胖u - 你走以后1.0.mp3'),
(17,26,'周杰伦-稻香','《稻香》','2023-07-20 22:37:38','2023-07-20 23:05:24','/img/songPic/1689864023927daoxiang.jpg','[ti:稻香]\n[ar:周杰伦]\n作词：周杰伦 作曲：周杰伦\n[00:30.98]对这个世界如果你有太多的抱怨\n[00:34.35]跌倒了 就不敢继续往前走\n[00:37.23]为什么 人要这么的脆弱 堕落\n[00:41.60]请你打开电视看看\n[00:43.17]多少人为生命在努力勇敢的走下去\n[00:47.23]我们是不是该知足\n[00:49.67]珍惜一切 就算没有拥有\n[00:54.11]还记得你说家是唯一的城堡\n[00:57.79]随着稻香河流继续奔跑\n[01:00.80]微微笑 小时候的梦我知道\n[01:05.86]不要哭让萤火虫带著你逃跑\n[01:09.48]乡间的歌谣永远的依靠\n[01:12.36]回家吧 回到最初的美好\n[01:41.25]不要这么容易就想放弃 就像我说的\n[01:44.87]追不到的梦想 换个梦不就得了\n[01:48.06]为自己的人生鲜艳上色\n[01:50.06]先把爱涂上喜欢的颜色\n[01:52.93]笑一个吧 功成名就不是目的\n[01:55.81]让自己快乐快乐这才叫做意义\n[01:58.75]童年的纸飞机 现在终于飞回我手里\n[02:04.37]所谓的那快乐 赤脚在田里追蜻蜓追到累了\n[02:08.81]偷摘水果被蜜蜂给叮到怕了\n[02:12.00]谁在偷笑呢\n[02:13.44]我靠着稻草人吹着风唱着歌睡着了\n[02:16.82]哦 哦 午后吉它在虫鸣中更清脆\n[02:19.69]哦 哦 阳光洒在路上就不怕心碎\n[02:23.26]珍惜一切 就算没有拥有\n[02:27.76]还记得你说家是唯一的城堡\n[02:31.51]随着稻香河流继续奔跑\n[02:34.44]微微笑 小时候的梦我知道\n[02:39.51]不要哭让萤火虫带著你逃跑\n[02:43.19]乡间的歌谣永远的依靠\n[02:46.07]回家吧 回到最初的美好\n[02:51.38]还记得你说家是唯一的城堡\n[02:54.89]随着稻香河流继续奔跑\n[02:57.76]微微笑 小时候的梦我知道\n[03:02.89]不要哭让萤火虫带著你逃跑\n[03:06.51]乡间的歌谣永远的依靠\n[03:09.46]回家吧 回到最初的美好','/song/1689863858239周杰伦-稻香.mp3'),
(18,26,'周杰伦-告白气球','《告白气球》','2023-07-20 22:42:08','2023-07-20 23:06:07','/img/songPic/1689864174811gaobaiqiqui.jpg','[ti:告白气球]\n[ar:周杰伦]\n[al:周杰伦的床边故事]\n[offset:0]\n[00:00.98]告白气球\n[00:04.19]词：方文山\n[00:06.29]曲：周杰伦\n[00:07.78]演唱：周杰伦\n[00:17.78]\n[00:23.65]塞纳河畔 左岸的咖啡\n[00:26.45]我手一杯 品尝你的美\n[00:28.94]\n[00:29.49]留下唇印的嘴\n[00:31.89]\n[00:34.42]花店玫瑰 名字写错谁\n[00:37.18]告白气球 风吹到对街\n[00:40.18]微笑在天上飞\n[00:42.59]\n[00:44.27]你说你有点难追 想让我知难而退\n[00:49.40]礼物不需挑最贵 只要香榭的落叶\n[00:54.67]喔 营造浪漫的约会 不害怕搞砸一切\n[01:00.01]拥有你就拥有 全世界\n[01:04.15]\n[01:05.13]亲爱的 爱上你 从那天起\n[01:11.48]甜蜜的很轻易\n[01:14.59]\n[01:15.70]亲爱的 别任性 你的眼睛\n[01:21.36]\n[01:22.08]在说我愿意\n[01:25.42]\n[01:49.14]塞纳河畔 左岸的咖啡\n[01:51.72]我手一杯 品尝你的美\n[01:54.20]\n[01:54.87]留下唇印的嘴\n[01:57.31]\n[01:59.81]花店玫瑰 名字写错谁\n[02:02.39]告白气球 风吹到对街\n[02:04.70]\n[02:05.44]微笑在天上飞\n[02:07.99]\n[02:09.61]你说你有点难追 想让我知难而退\n[02:14.78]礼物不需挑最贵 只要香榭的落叶\n[02:19.65]\n[02:20.18]喔 营造浪漫的约会 不害怕搞砸一切\n[02:25.40]拥有你就拥有 全世界\n[02:29.42]\n[02:30.46]亲爱的 爱上你 从那天起\n[02:36.87]甜蜜的很轻易\n[02:39.98]\n[02:41.01]亲爱的 别任性 你的眼睛\n[02:46.74]\n[02:47.33]在说我愿意\n[02:51.16]\n[02:51.85]亲爱的 爱上你 恋爱日记\n[02:57.46]\n[02:58.06]飘香水的回忆\n[03:01.49]\n[03:02.42]一整瓶 的梦境 全都有你\n[03:08.11]\n[03:08.82]搅拌在一起\n[03:12.29]\n[03:13.16]亲爱的别任性 你的眼睛\n[03:20.01]\n[03:21.37]在说我愿意','/song/1689864128640周杰伦-告白气球.mp3'),
(19,27,'胡歌-六月的雨','《六月的雨》','2023-07-20 22:48:18','2023-07-20 22:50:00','/img/songPic/1689864573837109951163240682406.jpg','[00:00.00]暂无歌词','/song/1689864498790胡歌-六月的雨.mp3'),
(20,28,'王恩信Est,二胖u-你走以后 1.0','《你走以后 1.0》','2023-07-20 22:56:52','2023-07-20 22:57:08','/img/songPic/1689865028927music_bg7.jpg','[00:00.00]暂无歌词','/song/1689865012682王恩信Est,二胖u - 你走以后1.0.mp3');

/*Table structure for table `song_list` */

DROP TABLE IF EXISTS `song_list`;

CREATE TABLE `song_list` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `pic` varchar(255) DEFAULT NULL COMMENT '歌单图片',
  `introduction` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '歌单简介',
  `style` varchar(255) DEFAULT NULL COMMENT '风格',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='歌单';

/*Data for the table `song_list` */

insert  into `song_list`(`id`,`title`,`pic`,`introduction`,`style`) values 
(1,'歌单1','/img/songListPic/1689490644772乐队.gif','经典歌曲','经典'),
(2,'歌单2','/img/songListPic/1689489535906微信图片_202307152144362.jpg','背景音乐BackGround','BGM'),
(7,'歌单3','/img/songListPic/1689489560068微信图片_202307152144372.jpg','华语','华语'),
(8,'歌单4','/img/songListPic/1689490626041music_user.jpeg','搞笑音乐','嘻哈'),
(9,'歌单5','/img/songListPic/1689490619722微信图片_202307152144353.jpg','古典歌曲','古风'),
(10,'歌单6','/img/songListPic/123-bf.jpg','日本和韩国的歌曲','日韩'),
(11,'歌单7','/img/songListPic/123-bf.jpg','欧洲和美国的流行音乐','欧美'),
(14,'歌单8','/img/songListPic/123-bf.jpg','民间歌曲','民谣'),
(15,'歌单9','/img/songListPic/123-bf.jpg','哼唱','蓝调');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
