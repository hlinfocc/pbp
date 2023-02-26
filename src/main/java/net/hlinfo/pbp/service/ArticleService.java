/**
 * 
 */
package net.hlinfo.pbp.service;

import java.util.List;
import java.util.regex.Pattern;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.hlinfo.opt.Func;
import net.hlinfo.opt.RedisUtils;
import net.hlinfo.pbp.entity.AdminInfo;
import net.hlinfo.pbp.entity.ArticleCategory;
import net.hlinfo.pbp.opt.RedisKey;

@Service
public class ArticleService {
	public static final Logger log = LoggerFactory.getLogger(ArticleService.class);
	
	@Autowired
	private Dao dao;
	@Autowired
	private RedisUtils redisUtils;
	/**
	 * 以子集方式查询分类
	 * @param pid 父级ID
	 * @param state 状态：0启用 1禁用
	 * @param isdisplay 是否在导航显示 0不显示，1显示
	 * @return 以子集返回分类数据
	 */
	public List<ArticleCategory> articleCategoryList(String pid, int status,int isdisplay) {
		Cnd cnd = Cnd.where("isdelete", "=", 0);
		cnd.and("pid", "=", pid);
		if(status == 0 || status ==1) {
			cnd.and("status", "=", status);
		}
		if(isdisplay == 0 || isdisplay == 1) {
			cnd.and("isdisplay", "=", isdisplay);
		}
		cnd.asc("sort");
		cnd.desc("createTime");
		List<ArticleCategory> list = dao.query(ArticleCategory.class, cnd);
		if(list == null || list.size() <= 0) {
			return null;
		}
		list.forEach(item -> {
			item.setChildren(articleCategoryList(item.getId(), status,isdisplay));
		});
		return list;
	}

	public String getCategoryName(String id) {
		Cnd cnd = Cnd.where("id", "=", id);
		ArticleCategory obj = dao.fetch(ArticleCategory.class, cnd);
		return obj.getName();
	}
	public ArticleCategory getCategory(String id) {
		return dao.fetch(ArticleCategory.class, id);
	}
	
	public Node typesetingNode(Node ele) {
    	if(ele instanceof Comment) {
    		Comment cele = (Comment)ele;
			cele.attributes().remove("comment");
			cele.remove();
			return null;
		}
    	if("p".equals(ele.nodeName().toLowerCase().trim())) {
    		Element eele = (Element) ele;
    		
    		String content = eele.html().replaceAll("&nbsp;", "");
    		if(Func.isBlank(content)) {
    			ele.remove();
    			return null;
    		}
    		String style = ele.attr("style") 
				+ ";text-indent:2em;";
        	ele.attr("style", style);
		}
    	if(ele instanceof TextNode) {
    		TextNode tele = (TextNode)ele;
    		String text = this.trim(tele.text());
    		if(Strings.isNotBlank(text)) {
    			text = text.replaceAll("\\n+", "\\n")
    				.replaceAll("\\s+", " ");
    			tele.text(text);
    		}
		}
    	if(!findH(ele)) {
        	String style = ele.attr("style") 
				+ ";font-family: '宋体,SimSun';"
				+ "font-size:14pt;"
				+ "line-height:1.6em;";
        	ele.attr("style", style);
    	}else {
//    		System.out.println("h =" + ele.outerHtml());
    		if(findHx(ele,1)) {
    			String style = ele.attr("style") 
    					+ ";font-family: '宋体,SimSun';"
    					+ "font-size:22pt;"
    					+ "line-height:1.6em;";
    	      ele.attr("style", style);
    		}
    		if(findHx(ele,2)) {
    			String style = ele.attr("style") 
    					+ ";font-family: '宋体,SimSun';"
    					+ "font-size:18pt;"
    					+ "line-height:1.6em;";
    	      ele.attr("style", style);
    		}
    	}
    	//处理无效的图片
    	if("img".equals(ele.nodeName().toLowerCase().trim())) {
    		Element eele = (Element) ele;
    		if(eele.attr("src").toLowerCase().startsWith("file://")) {
    			ele.remove();
    			return null;
    		}
		}
    	for(int index = 0; index < ele.childNodes().size(); index ++) {
    		Node item = ele.childNode(index);
			if(typesetingNode(item) == null) {
				index = 0;
			}
		}
    	
    	return ele;
    }
    
    public boolean findH(Node node) {
    	boolean matches = Pattern.matches("h[1-6]{1}", node.nodeName().toLowerCase().trim());
    	if(matches) {
    		return true;
    	}
    	if(node.parent() == null) {
    		return false;
    	}
    	return findH(node.parent());
    }
    public boolean findHx(Node node,int x) {
    	boolean matches = Pattern.matches("h"+x+"{1}", node.nodeName().toLowerCase().trim());
    	if(matches) {
    		return true;
    	}
    	if(node.parent() == null) {
    		return false;
    	}
    	return findH(node.parent());
    }
    /**
     * 去掉字符串前后空白字符。空白字符的定义由Character.isWhitespace来判断
     *
     * @param cs
     *            字符串
     * @return 去掉了前后空白字符的新字符串
     */
    public String trim(CharSequence cs) {
        if (null == cs)
            return null;
        int length = cs.length();
        if (length == 0)
            return cs.toString();
        int l = 0;
        int last = length - 1;
        int r = last;
        for (; l < length; l++) {
            if (!Character.isWhitespace(cs.charAt(l)))
                break;
        }
        for (; r > l; r--) {
            if (!Character.isWhitespace(cs.charAt(r)))
                break;
        }
        if (l > r)
            return "";
        else if (l == 0 && r == last)
            return cs.toString();
        return cs.subSequence(l, r + 1).toString();
    }
    
    /**
	 * 获取管理员信息
	 @param userid
	 @return
	 */
	public AdminInfo getUserInfo(String userid) {
		AdminInfo memberInfo = redisUtils.getObject(RedisKey.ADMININFO+userid);
		if(memberInfo==null) {
			memberInfo = dao.fetch(AdminInfo.class,userid);
		}
		return memberInfo;
	}
}
