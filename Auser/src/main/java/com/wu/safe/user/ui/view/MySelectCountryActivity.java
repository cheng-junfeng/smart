package com.wu.safe.user.ui.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.EmptyUtils;
import com.google.gson.reflect.TypeToken;
import com.wu.safe.base.app.event.RxBusHelper;
import com.wu.safe.base.app.listener.OnPositionSelectListener;
import com.wu.safe.base.ui.widget.WaveSideBarView;
import com.wu.safe.base.utils.GsonUtil;
import com.wu.safe.base.utils.LogUtil;
import com.wu.safe.base.utils.ToolbarUtil;
import com.wu.safe.user.R;
import com.wu.safe.user.R2;
import com.wu.safe.user.app.acitvity.UserBaseCompatActivity;
import com.wu.safe.user.ui.adapter.CountryAdapter;
import com.wu.safe.user.ui.bean.CountryBean;
import com.wu.safe.user.ui.event.CountryEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MySelectCountryActivity extends UserBaseCompatActivity {

    private final static String TAG = "MySelectCountryActivity";

    private String countryListJson = "[{\"letter\":\"#\",\"name\":\"#\",\"py\":\"#\",\"type\":1},{\"code\":\"+86\",\"letter\":\"#\",\"name\":\"中国\",\"py\":\"ZHONGGUO\",\"type\":0},{\"code\":\"+852\",\"letter\":\"#\",\"name\":\"香港\",\"py\":\"XIANGGANG\",\"type\":0},{\"code\":\"+853\",\"letter\":\"#\",\"name\":\"澳门\",\"py\":\"AOMEN\",\"type\":0},{\"code\":\"+886\",\"letter\":\"#\",\"name\":\"台湾\",\"py\":\"TAIWAN\",\"type\":0},{\"code\":\"+81\",\"letter\":\"#\",\"name\":\"日本\",\"py\":\"RIBEN\",\"type\":0},{\"code\":\"+1\",\"letter\":\"#\",\"name\":\"美国\",\"py\":\"MEIGUO\",\"type\":0},{\"code\":\"+44\",\"letter\":\"#\",\"name\":\"英国\",\"py\":\"YINGGUO\",\"type\":0},{\"code\":\"+61\",\"letter\":\"#\",\"name\":\"澳大利亚\",\"py\":\"AODALIYA\",\"type\":0},{\"code\":\"+1\",\"letter\":\"#\",\"name\":\"加拿大\",\"py\":\"JIANADA\",\"type\":0},{\"letter\":\"A\",\"name\":\"A\",\"py\":\"A\",\"type\":1},{\"code\":\"+376\",\"letter\":\"A\",\"name\":\"安道尔共和国\",\"py\":\"ANDAOERGONGHEGUO\",\"type\":0},{\"code\":\"+43\",\"letter\":\"A\",\"name\":\"奥地利\",\"py\":\"AODELI\",\"type\":0},{\"code\":\"+213\",\"letter\":\"A\",\"name\":\"阿尔及利亚\",\"py\":\"AERJILIYA\",\"type\":0},{\"code\":\"+353\",\"letter\":\"A\",\"name\":\"爱尔兰\",\"py\":\"AIERLAN\",\"type\":0},{\"code\":\"+93\",\"letter\":\"A\",\"name\":\"阿富汗\",\"py\":\"AFUHAN\",\"type\":0},{\"code\":\"+244\",\"letter\":\"A\",\"name\":\"安哥拉\",\"py\":\"ANGELA\",\"type\":0},{\"code\":\"+1-264\",\"letter\":\"A\",\"name\":\"安圭拉岛\",\"py\":\"ANGUILADAO\",\"type\":0},{\"code\":\"+54\",\"letter\":\"A\",\"name\":\"阿根廷\",\"py\":\"AGENTING\",\"type\":0},{\"code\":\"+20\",\"letter\":\"A\",\"name\":\"埃及\",\"py\":\"AIJI\",\"type\":0},{\"code\":\"+297\",\"letter\":\"A\",\"name\":\"阿鲁巴岛\",\"py\":\"ALUBADAO\",\"type\":0},{\"code\":\"+971\",\"letter\":\"A\",\"name\":\"阿拉伯联合酋长国\",\"py\":\"ALABOLIANHEQIUCHANGGUO\",\"type\":0},{\"code\":\"+994\",\"letter\":\"A\",\"name\":\"阿塞拜疆\",\"py\":\"ASAIBAIJIANG\",\"type\":0},{\"code\":\"+251\",\"letter\":\"A\",\"name\":\"埃塞俄比亚\",\"py\":\"AISAIEBIYA\",\"type\":0},{\"code\":\"+372\",\"letter\":\"A\",\"name\":\"爱沙尼亚\",\"py\":\"AISHANIYA\",\"type\":0},{\"code\":\"+247\",\"letter\":\"A\",\"name\":\"阿森松\",\"py\":\"ASENSONG\",\"type\":0},{\"code\":\"+1268\",\"letter\":\"A\",\"name\":\"安提瓜和巴布达\",\"py\":\"ANTIGUAHEBABUDA\",\"type\":0},{\"letter\":\"B\",\"name\":\"B\",\"py\":\"B\",\"type\":1},{\"code\":\"+1246\",\"letter\":\"B\",\"name\":\"巴巴多斯\",\"py\":\"BABADUOSI\",\"type\":0},{\"code\":\"+675\",\"letter\":\"B\",\"name\":\"巴布亚新几内亚\",\"py\":\"BABUYAXINJINEIYA\",\"type\":0},{\"code\":\"+267\",\"letter\":\"B\",\"name\":\"博茨瓦纳\",\"py\":\"BOCIWANA\",\"type\":0},{\"code\":\"+354\",\"letter\":\"B\",\"name\":\"冰岛\",\"py\":\"BINGDAO\",\"type\":0},{\"code\":\"+975\",\"letter\":\"B\",\"name\":\"不丹\",\"py\":\"BUDAN\",\"type\":0},{\"code\":\"+1-787,+1-939\",\"letter\":\"B\",\"name\":\"波多黎各\",\"py\":\"BODUOLIGE\",\"type\":0},{\"code\":\"+375\",\"letter\":\"B\",\"name\":\"白俄罗斯\",\"py\":\"BAIELUOSI\",\"type\":0},{\"code\":\"+1242\",\"letter\":\"B\",\"name\":\"巴哈马\",\"py\":\"BAHAMA\",\"type\":0},{\"code\":\"+359\",\"letter\":\"B\",\"name\":\"保加利亚\",\"py\":\"BAOJIALIYA\",\"type\":0},{\"code\":\"+226\",\"letter\":\"B\",\"name\":\"布基纳法索\",\"py\":\"BUJINAFASUO\",\"type\":0},{\"code\":\"+92\",\"letter\":\"B\",\"name\":\"巴基斯坦\",\"py\":\"BAJISITAN\",\"type\":0},{\"code\":\"+973\",\"letter\":\"B\",\"name\":\"巴林\",\"py\":\"BALIN\",\"type\":0},{\"code\":\"+48\",\"letter\":\"B\",\"name\":\"波兰\",\"py\":\"BOLAN\",\"type\":0},{\"code\":\"+257\",\"letter\":\"B\",\"name\":\"布隆迪\",\"py\":\"BULONGDI\",\"type\":0},{\"code\":\"+32\",\"letter\":\"B\",\"name\":\"比利时\",\"py\":\"BILISHI\",\"type\":0},{\"code\":\"+591\",\"letter\":\"B\",\"name\":\"玻利维亚\",\"py\":\"BOLIWEIYA\",\"type\":0},{\"code\":\"+501\",\"letter\":\"B\",\"name\":\"伯利兹\",\"py\":\"BOLIZI\",\"type\":0},{\"code\":\"+1441\",\"letter\":\"B\",\"name\":\"百慕大群岛\",\"py\":\"BAIMUDAQUNDAO\",\"type\":0},{\"code\":\"+229\",\"letter\":\"B\",\"name\":\"贝宁\",\"py\":\"BEINING\",\"type\":0},{\"code\":\"+507\",\"letter\":\"B\",\"name\":\"巴拿马\",\"py\":\"BANAMA\",\"type\":0},{\"code\":\"+55\",\"letter\":\"B\",\"name\":\"巴西\",\"py\":\"BAXI\",\"type\":0},{\"letter\":\"D\",\"name\":\"D\",\"py\":\"D\",\"type\":1},{\"code\":\"+49\",\"letter\":\"D\",\"name\":\"德国\",\"py\":\"DEGUO\",\"type\":0},{\"code\":\"+228\",\"letter\":\"D\",\"name\":\"多哥\",\"py\":\"DUOGE\",\"type\":0},{\"code\":\"+45\",\"letter\":\"D\",\"name\":\"丹麦\",\"py\":\"DANMAI\",\"type\":0},{\"code\":\"+1890\",\"letter\":\"D\",\"name\":\"多米尼加共和国\",\"py\":\"DUOMINIJIAGONGHEGUO\",\"type\":0},{\"code\":\"+1684\",\"letter\":\"D\",\"name\":\"东萨摩亚(美)\",\"py\":\"DONGSAMOYA(MEI)\",\"type\":0},{\"letter\":\"E\",\"name\":\"E\",\"py\":\"E\",\"type\":1},{\"code\":\"+593\",\"letter\":\"E\",\"name\":\"厄瓜多尔\",\"py\":\"EGUADUOER\",\"type\":0},{\"code\":\"+7\",\"letter\":\"E\",\"name\":\"俄罗斯\",\"py\":\"ELUOSI\",\"type\":0},{\"letter\":\"F\",\"name\":\"F\",\"py\":\"F\",\"type\":1},{\"code\":\"+33\",\"letter\":\"F\",\"name\":\"法国\",\"py\":\"FAGUO\",\"type\":0},{\"code\":\"+679\",\"letter\":\"F\",\"name\":\"斐济\",\"py\":\"FEIJI\",\"type\":0},{\"code\":\"+358\",\"letter\":\"F\",\"name\":\"芬兰\",\"py\":\"FENLAN\",\"type\":0},{\"code\":\"+63\",\"letter\":\"F\",\"name\":\"菲律宾\",\"py\":\"FEILUBIN\",\"type\":0},{\"code\":\"+689\",\"letter\":\"F\",\"name\":\"法属波利尼西亚\",\"py\":\"FASHUBOLINIXIYA\",\"type\":0},{\"code\":\"+594\",\"letter\":\"F\",\"name\":\"法属圭亚那\",\"py\":\"FASHUGUIYANA\",\"type\":0},{\"letter\":\"G\",\"name\":\"G\",\"py\":\"G\",\"type\":1},{\"code\":\"+53\",\"letter\":\"G\",\"name\":\"古巴\",\"py\":\"GUBA\",\"type\":0},{\"code\":\"+220\",\"letter\":\"G\",\"name\":\"冈比亚\",\"py\":\"GANGBIYA\",\"type\":0},{\"code\":\"+1671\",\"letter\":\"G\",\"name\":\"关岛\",\"py\":\"GUANDAO\",\"type\":0},{\"code\":\"+242\",\"letter\":\"G\",\"name\":\"刚果\",\"py\":\"GANGGUO\",\"type\":0},{\"code\":\"+57\",\"letter\":\"G\",\"name\":\"哥伦比亚\",\"py\":\"GELUNBIYA\",\"type\":0},{\"code\":\"+995\",\"letter\":\"G\",\"name\":\"格鲁吉亚\",\"py\":\"GELUJIYA\",\"type\":0},{\"code\":\"+1473\",\"letter\":\"G\",\"name\":\"格林纳达\",\"py\":\"GELINNADA\",\"type\":0},{\"code\":\"+506\",\"letter\":\"G\",\"name\":\"哥斯达黎加\",\"py\":\"GESIDALIJIA\",\"type\":0},{\"code\":\"+592\",\"letter\":\"G\",\"name\":\"圭亚那\",\"py\":\"GUIYANA\",\"type\":0},{\"letter\":\"H\",\"name\":\"H\",\"py\":\"H\",\"type\":1},{\"code\":\"+509\",\"letter\":\"H\",\"name\":\"海地\",\"py\":\"HAIDE\",\"type\":0},{\"code\":\"+504\",\"letter\":\"H\",\"name\":\"洪都拉斯\",\"py\":\"HONGDOULASI\",\"type\":0},{\"code\":\"+082\",\"letter\":\"H\",\"name\":\"韩国\",\"py\":\"HANGUO\",\"type\":0},{\"code\":\"+7\",\"letter\":\"H\",\"name\":\"哈萨克斯坦\",\"py\":\"HASAKESITAN\",\"type\":0},{\"letter\":\"J\",\"name\":\"J\",\"py\":\"J\",\"type\":1},{\"code\":\"+263\",\"letter\":\"J\",\"name\":\"津巴布韦\",\"py\":\"JINBABUWEI\",\"type\":0},{\"code\":\"+253\",\"letter\":\"J\",\"name\":\"吉布提\",\"py\":\"JIBUTI\",\"type\":0},{\"code\":\"+996\",\"letter\":\"J\",\"name\":\"吉尔吉斯坦\",\"py\":\"JIERJISITAN\",\"type\":0},{\"code\":\"+420\",\"letter\":\"J\",\"name\":\"捷克\",\"py\":\"JIEKE\",\"type\":0},{\"code\":\"+233\",\"letter\":\"J\",\"name\":\"加纳\",\"py\":\"JIANA\",\"type\":0},{\"code\":\"+224\",\"letter\":\"J\",\"name\":\"几内亚\",\"py\":\"JINEIYA\",\"type\":0},{\"code\":\"+241\",\"letter\":\"J\",\"name\":\"加蓬\",\"py\":\"JIAPENG\",\"type\":0},{\"code\":\"+855\",\"letter\":\"J\",\"name\":\"柬埔寨\",\"py\":\"JIANBUZHAI\",\"type\":0},{\"letter\":\"K\",\"name\":\"K\",\"py\":\"K\",\"type\":1},{\"code\":\"+682\",\"letter\":\"K\",\"name\":\"库克群岛\",\"py\":\"KUKEQUNDAO\",\"type\":0},{\"code\":\"+237\",\"letter\":\"K\",\"name\":\"喀麦隆\",\"py\":\"KAMAILONG\",\"type\":0},{\"code\":\"+1345\",\"letter\":\"K\",\"name\":\"开曼群岛\",\"py\":\"KAIMANQUNDAO\",\"type\":0},{\"code\":\"+254\",\"letter\":\"K\",\"name\":\"肯尼亚\",\"py\":\"KENNIYA\",\"type\":0},{\"code\":\"+225\",\"letter\":\"K\",\"name\":\"科特迪瓦\",\"py\":\"KETEDIWA\",\"type\":0},{\"code\":\"+974\",\"letter\":\"K\",\"name\":\"卡塔尔\",\"py\":\"KATAER\",\"type\":0},{\"code\":\"+965\",\"letter\":\"K\",\"name\":\"科威特\",\"py\":\"KEWEITE\",\"type\":0},{\"letter\":\"L\",\"name\":\"L\",\"py\":\"L\",\"type\":1},{\"code\":\"+231\",\"letter\":\"L\",\"name\":\"利比里亚\",\"py\":\"LIBILIYA\",\"type\":0},{\"code\":\"+961\",\"letter\":\"L\",\"name\":\"黎巴嫩\",\"py\":\"LIBANEN\",\"type\":0},{\"code\":\"+218\",\"letter\":\"L\",\"name\":\"利比亚\",\"py\":\"LIBIYA\",\"type\":0},{\"code\":\"+40\",\"letter\":\"L\",\"name\":\"罗马尼亚\",\"py\":\"LUOMANIYA\",\"type\":0},{\"code\":\"+262\",\"letter\":\"L\",\"name\":\"留尼汪\",\"py\":\"LIUNIWANG\",\"type\":0},{\"code\":\"+352\",\"letter\":\"L\",\"name\":\"卢森堡\",\"py\":\"LUSENBAO\",\"type\":0},{\"code\":\"+266\",\"letter\":\"L\",\"name\":\"莱索托\",\"py\":\"LAISUOTUO\",\"type\":0},{\"code\":\"+370\",\"letter\":\"L\",\"name\":\"立陶宛\",\"py\":\"LITAOWAN\",\"type\":0},{\"code\":\"+371\",\"letter\":\"L\",\"name\":\"拉脱维亚\",\"py\":\"LATUOWEIYA\",\"type\":0},{\"code\":\"+856\",\"letter\":\"L\",\"name\":\"老挝\",\"py\":\"LAOWO\",\"type\":0},{\"code\":\"+423\",\"letter\":\"L\",\"name\":\"列支敦士登\",\"py\":\"LIEZHIDUNSHIDENG\",\"type\":0},{\"letter\":\"M\",\"name\":\"M\",\"py\":\"M\",\"type\":1},{\"code\":\"+95\",\"letter\":\"M\",\"name\":\"缅甸\",\"py\":\"MIANDIAN\",\"type\":0},{\"code\":\"+261\",\"letter\":\"M\",\"name\":\"马达加斯加\",\"py\":\"MADAJIASIJIA\",\"type\":0},{\"code\":\"+960\",\"letter\":\"M\",\"name\":\"马尔代夫\",\"py\":\"MAERDAIFOU\",\"type\":0},{\"code\":\"+373\",\"letter\":\"M\",\"name\":\"摩尔多瓦\",\"py\":\"MOERDUOWA\",\"type\":0},{\"code\":\"+356\",\"letter\":\"M\",\"name\":\"马耳他\",\"py\":\"MAERTA\",\"type\":0},{\"code\":\"+880\",\"letter\":\"M\",\"name\":\"孟加拉国\",\"py\":\"MENGJIALAGUO\",\"type\":0},{\"code\":\"+223\",\"letter\":\"M\",\"name\":\"马里\",\"py\":\"MALI\",\"type\":0},{\"code\":\"+230\",\"letter\":\"M\",\"name\":\"毛里求斯\",\"py\":\"MAOLIQIUSI\",\"type\":0},{\"code\":\"+265\",\"letter\":\"M\",\"name\":\"马拉维\",\"py\":\"MALAWEI\",\"type\":0},{\"code\":\"+60\",\"letter\":\"M\",\"name\":\"马来西亚\",\"py\":\"MALAIXIYA\",\"type\":0},{\"code\":\"+1670\",\"letter\":\"M\",\"name\":\"马里亚纳群岛\",\"py\":\"MALIYANAQUNDAO\",\"type\":0},{\"code\":\"+377\",\"letter\":\"M\",\"name\":\"摩纳哥\",\"py\":\"MONAGE\",\"type\":0},{\"code\":\"+258\",\"letter\":\"M\",\"name\":\"莫桑比克\",\"py\":\"MOSANGBIKE\",\"type\":0},{\"code\":\"+684\",\"letter\":\"M\",\"name\":\"美属萨摩亚\",\"py\":\"MEISHUSAMOYA\",\"type\":0},{\"code\":\"+596\",\"letter\":\"M\",\"name\":\"马提尼克\",\"py\":\"MATINIKE\",\"type\":0},{\"code\":\"+52\",\"letter\":\"M\",\"name\":\"墨西哥\",\"py\":\"MOXIGE\",\"type\":0},{\"letter\":\"N\",\"name\":\"N\",\"py\":\"N\",\"type\":1},{\"code\":\"+27\",\"letter\":\"N\",\"name\":\"南非\",\"py\":\"NANFEI\",\"type\":0},{\"code\":\"+674\",\"letter\":\"N\",\"name\":\"瑙鲁\",\"py\":\"NAOLU\",\"type\":0},{\"code\":\"+227\",\"letter\":\"N\",\"name\":\"尼日尔\",\"py\":\"NIRIER\",\"type\":0},{\"code\":\"+234\",\"letter\":\"N\",\"name\":\"尼日利亚\",\"py\":\"NIRILIYA\",\"type\":0},{\"code\":\"+47\",\"letter\":\"N\",\"name\":\"挪威\",\"py\":\"NUOWEI\",\"type\":0},{\"letter\":\"P\",\"name\":\"P\",\"py\":\"P\",\"type\":1},{\"code\":\"+351\",\"letter\":\"P\",\"name\":\"葡萄牙\",\"py\":\"PUTAOYA\",\"type\":0},{\"letter\":\"R\",\"name\":\"R\",\"py\":\"R\",\"type\":1},{\"code\":\"+46\",\"letter\":\"R\",\"name\":\"瑞典\",\"py\":\"RUIDIAN\",\"type\":0},{\"code\":\"+41\",\"letter\":\"R\",\"name\":\"瑞士\",\"py\":\"RUISHI\",\"type\":0},{\"letter\":\"S\",\"name\":\"S\",\"py\":\"S\",\"type\":1},{\"code\":\"+249\",\"letter\":\"S\",\"name\":\"苏丹\",\"py\":\"SUDAN\",\"type\":0},{\"code\":\"+239\",\"letter\":\"S\",\"name\":\"圣多美和普林西比\",\"py\":\"SHENGDUOMEIHEPULINXIBI\",\"type\":0},{\"code\":\"+503\",\"letter\":\"S\",\"name\":\"萨尔瓦多\",\"py\":\"SAERWADUO\",\"type\":0},{\"code\":\"+421\",\"letter\":\"S\",\"name\":\"斯洛伐克\",\"py\":\"SILUOFAKE\",\"type\":0},{\"code\":\"+232\",\"letter\":\"S\",\"name\":\"塞拉利昂\",\"py\":\"SAILALIANG\",\"type\":0},{\"code\":\"+94\",\"letter\":\"S\",\"name\":\"斯里兰卡\",\"py\":\"SILILANKA\",\"type\":0},{\"code\":\"+677\",\"letter\":\"S\",\"name\":\"所罗门群岛\",\"py\":\"SUOLUOMENQUNDAO\",\"type\":0},{\"code\":\"+597\",\"letter\":\"S\",\"name\":\"苏里南\",\"py\":\"SULINAN\",\"type\":0},{\"code\":\"+386\",\"letter\":\"S\",\"name\":\"斯洛文尼亚\",\"py\":\"SILUOWENNIYA\",\"type\":0},{\"code\":\"+1758\",\"letter\":\"S\",\"name\":\"圣卢西亚\",\"py\":\"SHENGLUXIYA\",\"type\":0},{\"code\":\"+252\",\"letter\":\"S\",\"name\":\"索马里\",\"py\":\"SUOMALI\",\"type\":0},{\"code\":\"+378\",\"letter\":\"S\",\"name\":\"圣马力诺\",\"py\":\"SHENGMALINUO\",\"type\":0},{\"code\":\"+221\",\"letter\":\"S\",\"name\":\"塞内加尔\",\"py\":\"SAINEIJIAER\",\"type\":0},{\"code\":\"+357\",\"letter\":\"S\",\"name\":\"塞浦路斯\",\"py\":\"SAIPULUSI\",\"type\":0},{\"code\":\"+248\",\"letter\":\"S\",\"name\":\"塞舌尔\",\"py\":\"SAISHEER\",\"type\":0},{\"code\":\"+966\",\"letter\":\"S\",\"name\":\"沙特阿拉伯\",\"py\":\"SHATEALABO\",\"type\":0},{\"code\":\"+268\",\"letter\":\"S\",\"name\":\"斯威士兰\",\"py\":\"SIWEISHILAN\",\"type\":0},{\"code\":\"+784\",\"letter\":\"S\",\"name\":\"圣文森特\",\"py\":\"SHENGWENSENTE\",\"type\":0},{\"letter\":\"T\",\"name\":\"T\",\"py\":\"T\",\"type\":1},{\"code\":\"+90\",\"letter\":\"T\",\"name\":\"土耳其\",\"py\":\"TUERQI\",\"type\":0},{\"code\":\"+66\",\"letter\":\"T\",\"name\":\"泰国\",\"py\":\"TAIGUO\",\"type\":0},{\"code\":\"+676\",\"letter\":\"T\",\"name\":\"汤加\",\"py\":\"TANGJIA\",\"type\":0},{\"code\":\"+992\",\"letter\":\"T\",\"name\":\"塔吉克斯坦\",\"py\":\"TAJIKESITAN\",\"type\":0},{\"code\":\"+993\",\"letter\":\"T\",\"name\":\"土库曼斯坦\",\"py\":\"TUKUMANSITAN\",\"type\":0},{\"code\":\"+1868\",\"letter\":\"T\",\"name\":\"特立尼达和多巴哥\",\"py\":\"TELINIDAHEDUOBAGE\",\"type\":0},{\"code\":\"+216\",\"letter\":\"T\",\"name\":\"突尼斯\",\"py\":\"TUNISI\",\"type\":0},{\"code\":\"+255\",\"letter\":\"T\",\"name\":\"坦桑尼亚\",\"py\":\"TANSANGNIYA\",\"type\":0},{\"letter\":\"W\",\"name\":\"W\",\"py\":\"W\",\"type\":1},{\"code\":\"+502\",\"letter\":\"W\",\"name\":\"危地马拉\",\"py\":\"WEIDEMALA\",\"type\":0},{\"code\":\"+256\",\"letter\":\"W\",\"name\":\"乌干达\",\"py\":\"WUGANDA\",\"type\":0},{\"code\":\"+380\",\"letter\":\"W\",\"name\":\"乌克兰\",\"py\":\"WUKELAN\",\"type\":0},{\"code\":\"+673\",\"letter\":\"W\",\"name\":\"文莱\",\"py\":\"WENLAI\",\"type\":0},{\"code\":\"+598\",\"letter\":\"W\",\"name\":\"乌拉圭\",\"py\":\"WULAGUI\",\"type\":0},{\"code\":\"+58\",\"letter\":\"W\",\"name\":\"委内瑞拉\",\"py\":\"WEINEIRUILA\",\"type\":0},{\"code\":\"+998\",\"letter\":\"W\",\"name\":\"乌兹别克斯坦\",\"py\":\"WUZIBIEKESITAN\",\"type\":0},{\"letter\":\"X\",\"name\":\"X\",\"py\":\"X\",\"type\":1},{\"code\":\"+34\",\"letter\":\"X\",\"name\":\"西班牙\",\"py\":\"XIBANYA\",\"type\":0},{\"code\":\"+65\",\"letter\":\"X\",\"name\":\"新加坡\",\"py\":\"XINJIAPO\",\"type\":0},{\"code\":\"+30\",\"letter\":\"X\",\"name\":\"希腊\",\"py\":\"XILA\",\"type\":0},{\"code\":\"+963\",\"letter\":\"X\",\"name\":\"叙利亚\",\"py\":\"XULIYA\",\"type\":0},{\"code\":\"+685\",\"letter\":\"X\",\"name\":\"西萨摩亚\",\"py\":\"XISAMOYA\",\"type\":0},{\"code\":\"+36\",\"letter\":\"X\",\"name\":\"匈牙利\",\"py\":\"XIONGYALI\",\"type\":0},{\"letter\":\"Y\",\"name\":\"Y\",\"py\":\"Y\",\"type\":1},{\"code\":\"+91\",\"letter\":\"Y\",\"name\":\"印度\",\"py\":\"YINDU\",\"type\":0},{\"code\":\"+962\",\"letter\":\"Y\",\"name\":\"约旦\",\"py\":\"YUEDAN\",\"type\":0},{\"code\":\"+39\",\"letter\":\"Y\",\"name\":\"意大利\",\"py\":\"YIDALI\",\"type\":0},{\"code\":\"+98\",\"letter\":\"Y\",\"name\":\"伊朗\",\"py\":\"YILANG\",\"type\":0},{\"code\":\"+964\",\"letter\":\"Y\",\"name\":\"伊拉克\",\"py\":\"YILAKE\",\"type\":0},{\"code\":\"+967\",\"letter\":\"Y\",\"name\":\"也门\",\"py\":\"YEMEN\",\"type\":0},{\"code\":\"+1876\",\"letter\":\"Y\",\"name\":\"牙买加\",\"py\":\"YAMAIJIA\",\"type\":0},{\"code\":\"+374\",\"letter\":\"Y\",\"name\":\"亚美尼亚\",\"py\":\"YAMEINIYA\",\"type\":0},{\"code\":\"+62\",\"letter\":\"Y\",\"name\":\"印尼\",\"py\":\"YINNI\",\"type\":0},{\"code\":\"+84\",\"letter\":\"Y\",\"name\":\"越南\",\"py\":\"YUENAN\",\"type\":0},{\"code\":\"+972\",\"letter\":\"Y\",\"name\":\"以色列\",\"py\":\"YISELIE\",\"type\":0},{\"letter\":\"Z\",\"name\":\"Z\",\"py\":\"Z\",\"type\":1},{\"code\":\"+350\",\"letter\":\"Z\",\"name\":\"直布罗陀\",\"py\":\"ZHIBULUOTUO\",\"type\":0},{\"code\":\"+260\",\"letter\":\"Z\",\"name\":\"赞比亚\",\"py\":\"ZANBIYA\",\"type\":0},{\"code\":\"+235\",\"letter\":\"Z\",\"name\":\"乍得\",\"py\":\"ZHADE\",\"type\":0},{\"code\":\"+236\",\"letter\":\"Z\",\"name\":\"中非共和国\",\"py\":\"ZHONGFEIGONGHEGUO\",\"type\":0},{\"code\":\"+56\",\"letter\":\"Z\",\"name\":\"智利\",\"py\":\"ZHILI\",\"type\":0},{\"code\":\"+243\",\"letter\":\"Z\",\"name\":\"扎伊尔\",\"py\":\"ZHAYIER\",\"type\":0}]";

    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R2.id.side_view)
    WaveSideBarView mSideView;

    private CountryAdapter countryAdapter;

    @Override
    protected int setContentView() {
        return R.layout.activity_me_select_country;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarUtil.setToolbarLeft(toolbar, "选择国家/区域", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        init();
    }

    protected void init() {
        ArrayList<CountryBean> countryList = GsonUtil.fromJson(countryListJson, new TypeToken<ArrayList<CountryBean>>() {
        }.getType());
        List<String> letterList = new ArrayList<>();
        for (CountryBean countryBean : countryList) {
            if (!letterList.contains(countryBean.letter)) {
                letterList.add(countryBean.letter);
            }
        }
        mSideView.setLetters(letterList);
        setRecyclerData(countryList);
        mSideView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = countryAdapter.getLetterPosition(letter);

                if (pos != -1) {
                    mRecyclerView.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager =
                            (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });
    }

    private void setRecyclerData(List<CountryBean> data) {
        if (countryAdapter == null) {
            countryAdapter = new CountryAdapter(data);
            countryAdapter.setOnItemClickListener(new OnPositionSelectListener() {
                @Override
                public void onPositiveSelect(int position) {
                    CountryBean bean = countryAdapter.getItem(position);
                    if (EmptyUtils.isEmpty(bean.code)) {
                        LogUtil.d(TAG, "do nothing");
                        return;
                    }
                    LogUtil.d(TAG, "position:" + position + " " + bean.code + " " + bean.name);
                    CountryEvent event = new CountryEvent.Builder()
                            .code(bean.code)
                            .name(bean.name).build();
                    RxBusHelper.post(event);
                    finish();
                }
            });

            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            //每个item大小如果是确定的,设置可以提高性能
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(countryAdapter);
        } else {
            countryAdapter.setData(data);
            countryAdapter.notifyDataSetChanged();
        }
    }
}
