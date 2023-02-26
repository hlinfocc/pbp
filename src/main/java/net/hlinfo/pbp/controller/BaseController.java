package net.hlinfo.pbp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dev33.satoken.stp.StpUtil;
import net.hlinfo.opt.Func;

public class BaseController {
	public final Logger log = LoggerFactory.getLogger(BaseController.class);
	
	public String getLoginId() {
		if(StpUtil.isLogin()) {			
			String[] ids = StpUtil.getLoginIdAsString().split("-");
			if(ids != null && ids.length >= 2) {
				return ids[1];
			}
		}
		return "";
	}
	public int getLoginType() {
		if(StpUtil.isLogin()) {			
			String[] ids = StpUtil.getLoginIdAsString().split("-");
			if(ids != null && ids.length >= 2) {
				return Func.string2int(ids[0]);
			}
		}
		return -1;
	}
	
	protected String outHtml(String title, String msg,String error,String url) {
		String tpl = "<!doctype html>\n"
				+ "<html lang=\"zh-CN\">\n"
				+ " <head>\n"
				+ "  <meta charset=\"UTF-8\">\n"
				+ "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n"
				+ "  <title>${title}</title>\n"
				+ " </head>\n"
				+ " <body>\n"
				+ "    <p align=\"center\"><span style=\"font-size:16pt;font-weight:bold\">${msg}</span></p>\n"
				+ "    <p align=\"center\"><span style=\"font-size:13pt;${display}\">${error}</span></p>\n"
				+ "    <p align=\"center\"><a href=\"${url}\" style=\"height: 38px;line-height: 38px;border: 1px solid transparent;padding: 10px 18px;background-color: #1E9FFF;color: #fff;white-space: nowrap;text-align: center;font-size: 14px;border-radius: 2px;cursor: pointer;text-decoration: none;\">返回首页</a></p>\n"
				+ " </body>\n"
				+ "</html>";
		tpl = tpl.replace("${title}", title);
		tpl = tpl.replace("${msg}", msg);
		tpl = tpl.replace("${url}", url);
		if(Func.isNotBlank(error)) {
			tpl = tpl.replace("${display}", "");
			tpl = tpl.replace("${error}", error);
		}else {
			tpl = tpl.replace("${display}", "display:none;");
			tpl = tpl.replace("${error}", "");
		}
		return tpl;
	}
	
	protected void outHtml(HttpServletResponse response, String html) {
		response.setContentType("text/html;charset=utf-8");
		try {
			PrintWriter out = response.getWriter();
			out.print(html);
			out.flush();
			out.close();
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}
	}
	
	protected void outHtml(HttpServletResponse response,
			String title, 
			String msg, 
			String error, 
			String url) {
		outHtml(response, outHtml(title,msg, error, url));
	}
}

