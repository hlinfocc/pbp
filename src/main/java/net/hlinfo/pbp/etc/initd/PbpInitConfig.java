package net.hlinfo.pbp.etc.initd;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.trans.Trans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import cn.hutool.core.collection.CollectionUtil;
import net.hlinfo.opt.Func;
import net.hlinfo.pbp.entity.AdminInfo;
import net.hlinfo.pbp.entity.AdminRole;
import net.hlinfo.pbp.entity.MimeTypes;
import net.hlinfo.pbp.entity.Permission;
import net.hlinfo.pbp.entity.Role;
import net.hlinfo.pbp.entity.RolePermission;
import net.hlinfo.pbp.opt.PwdUtils;
import net.hlinfo.pbp.opt.vo.PermMeta;

@Component
@DependsOn(value = {"dao"})
public class PbpInitConfig {
	@Autowired
	private Dao dao;
	
	@PostConstruct
	@Lazy
	public void init() {
		int count = dao.count(Permission.class, null);
		if(count <= 0) {
			initPermission();
		}
		
		count = dao.count(Role.class, null);
		if(count <= 0) {
			initRole();
		}
		
		count = dao.count(AdminInfo.class, null);
		if(count <= 0) {
			initAdmin();
		}
		this.initMimeTypes();
	}
	/**
	 * 初始化默认权限
	 */
	private void initPermission() {
		List<Permission> perms = new ArrayList<>();
		Permission permHome = createPermission("0", "home", "首页", 
				"adminHome", "pages/home/AdminHome", "bank", 0, null);
		perms.add(permHome);
		
		Permission permConfig = createPermission("0", "configManager", "全局配置", 
				"configManager", "", "setting", 1, null);
		perms.add(permConfig);
		perms.add(createPermission(permConfig.getId(), "configTypeManager", "数据字典分类管理", 
				"configTypeManager", "pages/config/type/list", "", 0, CollectionUtil.newArrayList("添加", "编辑", "删除", "查询")));
		perms.add(createPermission(permConfig.getId(), "configManager", "数据字典管理", 
				"configManager", "pages/config/list", "", 1, CollectionUtil.newArrayList("添加", "编辑", "删除", "查询")));
		
		Permission permSystem = createPermission("0", "systemManager", "系统管理", 
				"sysManager", "", "setting", 100, null);
		perms.add(permSystem);
		perms.add(createPermission(permSystem.getId(), "menuManager", "菜单管理", 
				"permList", "pages/sys/Menu", "", 0, CollectionUtil.newArrayList("添加", "编辑", "删除", "查询")));
		perms.add(createPermission(permSystem.getId(), "roleManager", "角色管理", 
				"roleList", "pages/sys/Role", "", 1, CollectionUtil.newArrayList("添加", "编辑", "删除", "授权", "查询")));
		perms.add(createPermission(permSystem.getId(), "adminManager", "管理员管理", 
				"adminManager",  "pages/sys/Admin", "", 2, CollectionUtil.newArrayList("添加", "编辑", "删除", "关联角色", "查询")));
		
		dao.insert(perms);
	}
	/**
	 * 初始化默认角色
	 */
	private void initRole() {
		Role role = new Role();
		role.init();
		role.setCreateId("system");
		role.setCode("root");
		role.setName("超级管理员");
		role.setPow(999);
		role.setLevel(1);
		role.setStatus(0);
		List<RolePermission> rps = new ArrayList<RolePermission>();
		List<Permission> perms = dao.query(Permission.class, null);
		for(Permission perm : perms) {
			RolePermission rp = new RolePermission();
			rp.init();
			rp.setRoleid(role.getId());
			rp.setPermid(perm.getId());
			rp.setHasBtns(perm.getBtns());
			rps.add(rp);
		}
		Trans.exec(()->{
			dao.insert(role);
			dao.insert(rps);
		});
	}
	/**
	 * 初始化默认管理员用户
	 */
	private void initAdmin() {
		AdminInfo adminInfo = new AdminInfo();
		adminInfo.init();
		adminInfo.setAccount("root");
		adminInfo.setPassword(PwdUtils.passwdEncoder("123456"));
		adminInfo.setRealName("超级管理员");
		adminInfo.setStatus(0);
		
		Role role = dao.fetch(Role.class, Cnd.where("code", "=", "root"));
		AdminRole ar = new AdminRole();
		ar.init();
		ar.setAdminId(adminInfo.getId());
		ar.setRoleId(role.getId());
		Trans.exec(()->{
			dao.insert(adminInfo);
			dao.insert(ar);
		});
	}
	
	private Permission createPermission(
			String pid, String code, String name, 
			String router, String component, 
			String icon, int sort, 
			List<String> btns
		) {
		Permission perm = new Permission();
		perm.init();
		perm.setName(name);
		perm.setCode(code);
		perm.setComponentStr(component);
		
		perm.setCreateId("system");
		perm.setHidden(false);
		perm.setLevel(1);
		
		PermMeta meta = new PermMeta();
		meta.setIcon(icon);
		meta.setBreadcrumb(true);
		meta.setNoKeepAlive(true);
		meta.setInvisible(false);
		meta.setIsdisable(false);
		perm.setMeta(meta);
		
		perm.setNeedAuth(true);
		perm.setRouter(router);
		perm.setPid(pid);
		perm.setSort(sort);
		perm.setStatus(0);
		if(Func.listIsEmpty(btns)) {
			btns = new ArrayList<String>();
			perm.setBtns(btns);
		}else {
			perm.setBtns(btns);
		}
		
		return perm;
	}
	/**
	 * 初始化mimeTypes
	 */
	private void initMimeTypes() {
		int count = dao.count(MimeTypes.class, null);
		if(count > 0) {
			return;
		}
		String[][] mimeTypeArr = {
			{"application/wps-office.xlsx", ".xlsx"},
			{"application/wps-office.xls", ".xls"},
			{"application/wps-office.docx", ".docx"},
			{"application/wps-office.doc", ".doc"},
			{"application/wps-office.pptx", ".pptx"},
			{"application/wps-office.ppt", ".ppt"},
			{"application/kswps", ".wps"},
			{"application/andrew-inset", ".ez"},
			{"application/annodex", ".anx"},
			{"application/atom+xml", ".atom"},
			{"application/atomcat+xml", ".atomcat"},
			{"application/atomserv+xml", ".atomsrv"},
			{"application/bbolin", ".lin"},
			{"application/cu-seeme", ".cu"},
			{"application/davmount+xml", ".davmount"},
			{"application/dicom", ".dcm"},
			{"application/dsptype", ".tsp"},
			{"application/ecmascript", ".es"},
			{"application/epub+zip", ".epub"},
			{"application/font-sfnt", ".otf"},
			{"application/font-tdpfr", ".pfr"},
			{"application/font-woff", ".woff"},
			{"application/futuresplash", ".spl"},
			{"application/gzip", ".gz"},
			{"application/hta", ".hta"},
			{"application/java-archive", ".jar"},
			{"application/java-serialized-object", ".ser"},
			{"application/java-vm", ".class"},
			{"application/javascript", ".js"},
			{"application/json", ".json"},
			{"application/m3g", ".m3g"},
			{"application/mac-binhex40", ".hqx"},
			{"application/mac-compactpro", ".cpt"},
			{"application/mathematica", ".nb"},
			{"application/mbox", ".mbox"},
			{"application/msaccess", ".mdb"},
			{"application/msword", ".doc"},
			{"application/mxf", ".mxf"},
			{"application/octet-stream", ".bin"},
			{"application/oda", ".oda"},
			{"application/oebps-package+xml", ".opf"},
			{"application/ogg", ".ogx"},
			{"application/onenote", ".one"},
			{"application/pdf", ".pdf"},
			{"application/pgp-encrypted", ".pgp"},
			{"application/pgp-keys", ".key"},
			{"application/pgp-signature", ".sig"},
			{"application/pics-rules", ".prf"},
			{"application/postscript", ".ps"},
			{"application/rar", ".rar"},
			{"application/rdf+xml", ".rdf"},
			{"application/rtf", ".rtf"},
			{"application/sla", ".stl"},
			{"application/smil+xml", ".smi"},
			{"application/wasm", ".wasm"},
			{"application/xhtml+xml", ".xhtml"},
			{"application/xml", ".xml"},
			{"application/xslt+xml", ".xsl"},
			{"application/xspf+xml", ".xspf"},
			{"application/zip", ".zip"},
			{"application/vnd.android.package-archive", ".apk"},
			{"application/vnd.cinderella", ".cdy"},
			{"application/vnd.debian.binary-package", ".deb"},
			{"application/vnd.font-fontforge-sfd", ".sfd"},
			{"application/vnd.google-earth.kml+xml", ".kml"},
			{"application/vnd.google-earth.kmz", ".kmz"},
			{"application/vnd.mozilla.xul+xml", ".xul"},
			{"application/vnd.ms-excel", ".xls"},
			{"application/vnd.ms-excel.addin.macroEnabled.12", ".xlam"},
			{"application/vnd.ms-excel.sheet.binary.macroEnabled.12", ".xlsb"},
			{"application/vnd.ms-excel.sheet.macroEnabled.12", ".xlsm"},
			{"application/vnd.ms-excel.template.macroEnabled.12", ".xltm"},
			{"application/vnd.ms-fontobject", ".eot"},
			{"application/vnd.ms-officetheme", ".thmx"},
			{"application/vnd.ms-pki.seccat", ".cat"},
			{"application/vnd.ms-powerpoint", ".ppt"},
			{"application/vnd.ms-powerpoint.addin.macroEnabled.12", ".ppam"},
			{"application/vnd.ms-powerpoint.presentation.macroEnabled.12", ".pptm"},
			{"application/vnd.ms-powerpoint.slide.macroEnabled.12", ".sldm"},
			{"application/vnd.ms-powerpoint.slideshow.macroEnabled.12", ".ppsm"},
			{"application/vnd.ms-powerpoint.template.macroEnabled.12", ".potm"},
			{"application/vnd.ms-word.document.macroEnabled.12", ".docm"},
			{"application/vnd.ms-word.template.macroEnabled.12", ".dotm"},
			{"application/vnd.openxmlformats-officedocument.presentationml.presentation", ".pptx"},
			{"application/vnd.openxmlformats-officedocument.presentationml.slide", ".sldx"},
			{"application/vnd.openxmlformats-officedocument.presentationml.slideshow", ".ppsx"},
			{"application/vnd.openxmlformats-officedocument.presentationml.template", ".potx"},
			{"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx"},
			{"application/vnd.openxmlformats-officedocument.spreadsheetml.template", ".xltx"},
			{"application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx"},
			{"application/vnd.openxmlformats-officedocument.wordprocessingml.template", ".dotx"},
			{"application/vnd.rim.cod", ".cod"},
			{"application/vnd.smaf", ".mmf"},
			{"application/vnd.symbian.install", ".sis"},
			{"application/vnd.tcpdump.pcap", ".cap"},
			{"application/vnd.visio", ".vsd"},
			{"application/vnd.wap.wbxml", ".wbxml"},
			{"application/vnd.wap.wmlc", ".wmlc"},
			{"application/vnd.wap.wmlscriptc", ".wmlsc"},
			{"application/vnd.wordperfect", ".wpd"},
			{"application/vnd.wordperfect5.1", ".wp5"},
			{"application/x-123", ".wk"},
			{"application/x-7z-compressed", ".7z"},
			{"application/x-abiword", ".abw"},
			{"application/x-apple-diskimage", ".dmg"},
			{"application/x-bcpio", ".bcpio"},
			{"application/x-bittorrent", ".torrent"},
			{"application/x-cab", ".cab"},
			{"application/x-cbr", ".cbr"},
			{"application/x-cbz", ".cbz"},
			{"application/x-cdf", ".cdf"},
			{"application/x-cdlink", ".vcd"},
			{"application/x-chess-pgn", ".pgn"},
			{"application/x-comsol", ".mph"},
			{"application/x-cpio", ".cpio"},
			{"application/x-csh", ".csh"},
			{"application/x-debian-package", ".deb"},
			{"application/x-director", ".dcr"},
			{"application/x-dms", ".dms"},
			{"application/x-doom", ".wad"},
			{"application/x-dvi", ".dvi"},
			{"application/x-font", ".pfa"},
			{"application/x-font-pcf", ".pcf"},
			{"application/x-freemind", ".mm"},
			{"application/x-futuresplash", ".spl"},
			{"application/x-ganttproject", ".gan"},
			{"application/x-gnumeric", ".gnumeric"},
			{"application/x-go-sgf", ".sgf"},
			{"application/x-graphing-calculator", ".gcf"},
			{"application/x-gtar", ".gtar"},
			{"application/x-gtar-compressed", ".tgz"},
			{"application/x-hdf", ".hdf"},
			{"application/x-hwp", ".hwp"},
			{"application/x-ica", ".ica"},
			{"application/x-info", ".info"},
			{"application/x-internet-signup", ".ins"},
			{"application/x-iphone", ".iii"},
			{"application/x-iso9660-image", ".iso"},
			{"application/x-jam", ".jam"},
			{"application/x-java-jnlp-file", ".jnlp"},
			{"application/x-jmol", ".jmz"},
			{"application/x-kchart", ".chrt"},
			{"application/x-killustrator", ".kil"},
			{"application/x-koan", ".skp"},
			{"application/x-kpresenter", ".kpr"},
			{"application/x-kspread", ".ksp"},
			{"application/x-kword", ".kwd"},
			{"application/x-latex", ".latex"},
			{"application/x-lha", ".lha"},
			{"application/x-lyx", ".lyx"},
			{"application/x-lzh", ".lzh"},
			{"application/x-lzx", ".lzx"},
			{"application/x-maker", ".frm"},
			{"application/x-mif", ".mif"},
			{"application/x-mpegURL", ".m3u8"},
			{"application/x-ms-application", ".application"},
			{"application/x-ms-manifest", ".manifest"},
			{"application/x-ms-wmd", ".wmd"},
			{"application/x-ms-wmz", ".wmz"},
			{"application/x-msdos-program", ".com"},
			{"application/x-msi", ".msi"},
			{"application/x-netcdf", ".nc"},
			{"application/x-ns-proxy-autoconfig", ".pac"},
			{"application/x-nwc", ".nwc"},
			{"application/x-object", ".o"},
			{"application/x-oz-application", ".oza"},
			{"application/x-pkcs7-certreqresp", ".p7r"},
			{"application/x-pkcs7-crl", ".crl"},
			{"application/x-python-code", ".pyc"},
			{"application/x-qgis", ".qgs"},
			{"application/x-quicktimeplayer", ".qtl"},
			{"application/x-rdp", ".rdp"},
			{"application/x-redhat-package-manager", ".rpm"},
			{"application/x-rss+xml", ".rss"},
			{"application/x-ruby", ".rb"},
			{"application/x-scilab", ".sci"},
			{"application/x-scilab-xcos", ".xcos"},
			{"application/x-sh", ".sh"},
			{"application/x-shar", ".shar"},
			{"application/x-shockwave-flash", ".swf"},
			{"application/x-silverlight", ".scr"},
			{"application/x-sql", ".sql"},
			{"application/x-stuffit", ".sit"},
			{"application/x-sv4cpio", ".sv4cpio"},
			{"application/x-sv4crc", ".sv4crc"},
			{"application/x-tar", ".tar"},
			{"application/x-tcl", ".tcl"},
			{"application/x-tex-gf", ".gf"},
			{"application/x-tex-pk", ".pk"},
			{"application/x-texinfo", ".texinfo"},
			{"application/x-trash", ".~"},
			{"application/x-troff", ".t"},
			{"application/x-troff-man", ".man"},
			{"application/x-troff-me", ".me"},
			{"application/x-troff-ms", ".ms"},
			{"application/x-ustar", ".ustar"},
			{"application/x-wais-source", ".src"},
			{"application/x-wingz", ".wz"},
			{"application/x-x509-ca-cert", ".crt"},
			{"application/x-xcf", ".xcf"},
			{"application/x-xfig", ".fig"},
			{"application/x-xpinstall", ".xpi"},
			{"application/x-xz", ".xz"},
			{"audio/amr", ".amr"},
			{"audio/x-hx-aac-adts", ".aac"},
			{"audio/x-m4a", ".m4a"},
			{"audio/amr-wb", ".awb"},
			{"audio/annodex", ".axa"},
			{"audio/basic", ".au"},
			{"audio/csound", ".csd"},
			{"audio/flac", ".flac"},
			{"audio/midi", ".mid"},
			{"audio/mpeg", ".mpga"},
			{"audio/mpegurl", ".m3u"},
			{"audio/ogg", ".oga"},
			{"audio/prs.sid", ".sid"},
			{"audio/x-aiff", ".aif"},
			{"audio/x-gsm", ".gsm"},
			{"audio/x-mpegurl", ".m3u"},
			{"audio/x-ms-wma", ".wma"},
			{"audio/x-ms-wax", ".wax"},
			{"audio/x-pn-realaudio", ".ra"},
			{"audio/x-realaudio", ".ra"},
			{"audio/x-scpls", ".pls"},
			{"audio/x-sd2", ".sd2"},
			{"audio/x-wav", ".wav"},
			{"chemical/x-alchemy", ".alc"},
			{"chemical/x-cache", ".cac"},
			{"chemical/x-cache-csf", ".csf"},
			{"chemical/x-cactvs-binary", ".cbin"},
			{"chemical/x-cdx", ".cdx"},
			{"chemical/x-cerius", ".cer"},
			{"chemical/x-chem3d", ".c3d"},
			{"chemical/x-chemdraw", ".chm"},
			{"chemical/x-cif", ".cif"},
			{"chemical/x-cmdf", ".cmdf"},
			{"chemical/x-cml", ".cml"},
			{"chemical/x-compass", ".cpa"},
			{"chemical/x-crossfire", ".bsd"},
			{"chemical/x-csml", ".csml"},
			{"chemical/x-ctx", ".ctx"},
			{"chemical/x-cxf", ".cxf"},
			{"chemical/x-embl-dl-nucleotide", ".emb"},
			{"chemical/x-galactic-spc", ".spc"},
			{"chemical/x-gamess-input", ".inp"},
			{"chemical/x-gaussian-checkpoint", ".fch"},
			{"chemical/x-gaussian-cube", ".cub"},
			{"chemical/x-gaussian-input", ".gau"},
			{"chemical/x-gaussian-log", ".gal"},
			{"chemical/x-gcg8-sequence", ".gcg"},
			{"chemical/x-genbank", ".gen"},
			{"chemical/x-hin", ".hin"},
			{"chemical/x-isostar", ".istr"},
			{"chemical/x-jcamp-dx", ".jdx"},
			{"chemical/x-kinemage", ".kin"},
			{"chemical/x-macmolecule", ".mcm"},
			{"chemical/x-macromodel-input", ".mmd"},
			{"chemical/x-mdl-molfile", ".mol"},
			{"chemical/x-mdl-rdfile", ".rd"},
			{"chemical/x-mdl-rxnfile", ".rxn"},
			{"chemical/x-mdl-sdfile", ".sd"},
			{"chemical/x-mdl-tgf", ".tgf"},
			{"chemical/x-mmcif", ".mcif"},
			{"chemical/x-mol2", ".mol2"},
			{"chemical/x-molconn-Z", ".b"},
			{"chemical/x-mopac-graph", ".gpt"},
			{"chemical/x-mopac-input", ".mop"},
			{"chemical/x-mopac-out", ".moo"},
			{"chemical/x-mopac-vib", ".mvb"},
			{"chemical/x-ncbi-asn1", ".asn"},
			{"chemical/x-ncbi-asn1-ascii", ".prt"},
			{"chemical/x-ncbi-asn1-binary", ".val"},
			{"chemical/x-ncbi-asn1-spec", ".asn"},
			{"chemical/x-pdb", ".pdb"},
			{"chemical/x-rosdal", ".ros"},
			{"chemical/x-swissprot", ".sw"},
			{"chemical/x-vamas-iso14976", ".vms"},
			{"chemical/x-vmd", ".vmd"},
			{"chemical/x-xtel", ".xtel"},
			{"chemical/x-xyz", ".xyz"},
			{"font/collection", ".ttc"},
			{"font/otf", ".ttf"},
			{"font/sfnt", ".ttf"},
			{"font/ttf", ".ttf"},
			{"font/woff", ".woff"},
			{"font/woff2", ".woff2"},
			{"image/gif", ".gif"},
			{"image/ief", ".ief"},
			{"image/jp2", ".jp2"},
			{"image/jpeg", ".jpeg"},
			{"image/jpm", ".jpm"},
			{"image/jpx", ".jpx"},
			{"image/pcx", ".pcx"},
			{"image/png", ".png"},
			{"image/svg+xml", ".svg"},
			{"image/tiff", ".tiff"},
			{"image/vnd.djvu", ".djvu"},
			{"image/vnd.microsoft.icon", ".ico"},
			{"image/vnd.wap.wbmp", ".wbmp"},
			{"image/x-canon-cr2", ".cr2"},
			{"image/x-canon-crw", ".crw"},
			{"image/x-cmu-raster", ".ras"},
			{"image/x-coreldraw", ".cdr"},
			{"image/x-coreldrawpattern", ".pat"},
			{"image/x-coreldrawtemplate", ".cdt"},
			{"image/x-corelphotopaint", ".cpt"},
			{"image/x-epson-erf", ".erf"},
			{"image/x-jg", ".art"},
			{"image/x-jng", ".jng"},
			{"image/x-ms-bmp", ".bmp"},
			{"image/x-nikon-nef", ".nef"},
			{"image/x-olympus-orf", ".orf"},
			{"image/x-photoshop", ".psd"},
			{"image/x-portable-anymap", ".pnm"},
			{"image/x-portable-bitmap", ".pbm"},
			{"image/x-portable-graymap", ".pgm"},
			{"image/x-portable-pixmap", ".ppm"},
			{"image/x-rgb", ".rgb"},
			{"image/x-xbitmap", ".xbm"},
			{"image/x-xpixmap", ".xpm"},
			{"image/x-xwindowdump", ".xwd"},
			{"message/rfc822", ".eml"},
			{"model/iges", ".igs"},
			{"model/mesh", ".msh"},
			{"model/vrml", ".wrl"},
			{"model/x3d+vrml", ".x3dv"},
			{"model/x3d+xml", ".x3d"},
			{"model/x3d+binary", ".x3db"},
			{"text/cache-manifest", ".appcache"},
			{"text/calendar", ".ics"},
			{"text/css", ".css"},
			{"text/csv", ".csv"},
			{"text/h323", ".323"},
			{"text/html", ".html"},
			{"text/iuls", ".uls"},
			{"text/mathml", ".mml"},
			{"text/markdown", ".md"},
			{"text/plain", ".asc"},
			{"text/richtext", ".rtx"},
			{"text/scriptlet", ".sct"},
			{"text/texmacs", ".tm"},
			{"text/tab-separated-values", ".tsv"},
			{"text/turtle", ".ttl"},
			{"text/vcard", ".vcf"},
			{"text/vnd.sun.j2me.app-descriptor", ".jad"},
			{"text/vnd.wap.wml", ".wml"},
			{"text/vnd.wap.wmlscript", ".wmls"},
			{"text/x-bibtex", ".bib"},
			{"text/x-boo", ".boo"},
			{"text/x-c++hdr", ".h++"},
			{"text/x-c++src", ".c++"},
			{"text/x-chdr", ".h"},
			{"text/x-component", ".htc"},
			{"text/x-csh", ".csh"},
			{"text/x-csrc", ".c"},
			{"text/x-dsrc", ".d"},
			{"text/x-diff", ".diff"},
			{"text/x-haskell", ".hs"},
			{"text/x-java", ".java"},
			{"text/x-lilypond", ".ly"},
			{"text/x-literate-haskell", ".lhs"},
			{"text/x-moc", ".moc"},
			{"text/x-pascal", ".p"},
			{"text/x-pcs-gcd", ".gcd"},
			{"text/x-perl", ".pl"},
			{"text/x-python", ".py"},
			{"text/x-scala", ".scala"},
			{"text/x-setext", ".etx"},
			{"text/x-sfv", ".sfv"},
			{"text/x-sh", ".sh"},
			{"text/x-tcl", ".tcl"},
			{"text/x-tex", ".tex"},
			{"text/x-vcalendar", ".vcs"},
			{"video/3gpp", ".3gp"},
			{"video/annodex", ".axv"},
			{"video/dl", ".dl"},
			{"video/dv", ".dif"},
			{"video/fli", ".fli"},
			{"video/gl", ".gl"},
			{"video/mpeg", ".mpeg"},
			{"video/MP2T", ".ts"},
			{"video/mp4", ".mp4"},
			{"video/quicktime", ".qt"},
			{"video/ogg", ".ogv"},
			{"video/webm", ".webm"},
			{"video/vnd.mpegurl", ".mxu"},
			{"video/x-flv", ".flv"},
			{"video/x-la-asf", ".lsf"},
			{"video/x-mng", ".mng"},
			{"video/x-ms-asf", ".asf"},
			{"video/x-ms-wm", ".wm"},
			{"video/x-ms-wmv", ".wmv"},
			{"video/x-ms-wmx", ".wmx"},
			{"video/x-ms-wvx", ".wvx"},
			{"video/x-msvideo", ".avi"},
			{"video/x-sgi-movie", ".movie"},
			{"video/x-matroska", ".mpv"},
			{"x-conference/x-cooltalk", ".ice"},
			{"x-epoc/x-sisx-app", ".sisx"},
			{"x-world/x-vrml", ".vrm"},
			{"application/vnd.oasis.opendocument.text", ".odt"},
			{"application/vnd.oasis.opendocument.text-flat-xml", ".fodt"},
			{"application/vnd.oasis.opendocument.text-template", ".ott"},
			{"application/vnd.oasis.opendocument.text-web", ".oth"},
			{"application/vnd.oasis.opendocument.text-master", ".odm"},
			{"application/vnd.oasis.opendocument.text-master-template", ".otm"},
			{"application/vnd.oasis.opendocument.graphics", ".odg"},
			{"application/vnd.oasis.opendocument.graphics-flat-xml", ".fodg"},
			{"application/vnd.oasis.opendocument.graphics-template", ".otg"},
			{"application/vnd.oasis.opendocument.presentation", ".odp"},
			{"application/vnd.oasis.opendocument.presentation-flat-xml", ".fodp"},
			{"application/vnd.oasis.opendocument.presentation-template", ".otp"},
			{"application/vnd.oasis.opendocument.spreadsheet", ".ods"},
			{"application/vnd.oasis.opendocument.spreadsheet-flat-xml", ".fods"},
			{"application/vnd.oasis.opendocument.spreadsheet-template", ".ots"},
			{"application/vnd.oasis.opendocument.chart", ".odc"},
			{"application/vnd.oasis.opendocument.formula", ".odf"},
			{"application/vnd.oasis.opendocument.image", ".odi"},
			{"application/vnd.sun.xml.writer", ".sxw"},
			{"application/vnd.sun.xml.writer.template", ".stw"},
			{"application/vnd.sun.xml.writer.global", ".sxg"},
			{"application/vnd.stardivision.writer", ".sdw"},
			{"application/vnd.stardivision.writer-global", ".sgl"},
			{"application/vnd.sun.xml.calc", ".sxc"},
			{"application/vnd.sun.xml.calc.template", ".stc"},
			{"application/vnd.stardivision.calc", ".sdc"},
			{"application/vnd.stardivision.chart", ".sds"},
			{"application/vnd.sun.xml.impress", ".sxi"},
			{"application/vnd.sun.xml.impress.template", ".sti"},
			{"application/vnd.stardivision.impress", ".sdd"},
			{"application/vnd.sun.xml.draw", ".sxd"},
			{"application/vnd.sun.xml.draw.template", ".std"},
			{"application/vnd.stardivision.draw", ".sda"},
			{"application/vnd.sun.xml.math", ".sxm"},
			{"application/vnd.stardivision.math", ".smf"},
			{"application/vnd.sun.xml.base", ".odb"},
			{"application/vnd.openofficeorg.extension", ".oxt"}
		};
		List<MimeTypes> list = new ArrayList<MimeTypes>();
		for(int i = 0; i < mimeTypeArr.length; i ++) {
			MimeTypes item = new MimeTypes();
			item.init();
			item.setContentType(mimeTypeArr[i][0]);
			item.setSuffix(mimeTypeArr[i][1]);
			list.add(item);
		}
		dao.insert(list);
	}
}
