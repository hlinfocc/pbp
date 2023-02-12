package net.hlinfo.pbp.opt.beetlsql;

import java.util.Collection;
import java.util.Map;

import org.beetl.core.Context;
import org.beetl.core.Function;
import org.beetl.core.misc.PrimitiveArrayUtil;

import net.hlinfo.opt.Func;

public class BlankFun implements Function {
	public Boolean call(Object[] paras, Context ctx) {
		if (paras.length == 0)
			return true;
		Object result = paras[0];
		if (result == null)
			return true;
		if (result instanceof String) {

			return Func.isBlank(result + "");

		} else if (result instanceof Collection) {
			return ((Collection) result).size() == 0;
		} else if (result instanceof Map) {
			return ((Map) result).size() == 0;
		} else if (result.getClass().isArray()) {
			Class ct = result.getClass().getComponentType();
			if (ct.isPrimitive()) {
				return PrimitiveArrayUtil.getSize(result) == 0;
			} else {
				return ((Object[]) result).length == 0;
			}
		} else {
			return false;
		}
	}
}