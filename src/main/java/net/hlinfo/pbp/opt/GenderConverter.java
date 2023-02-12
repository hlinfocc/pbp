package net.hlinfo.pbp.opt;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;

import cn.hutool.core.util.StrUtil;

/**
 * easyexcel性别转换器
 * @author hadoop
 *
 */
public class GenderConverter implements Converter<Integer> {
	
	public Class<?> supportJavaTypeKey() {
		//对象属性类型
		return Integer.class;
	}

	public CellDataTypeEnum supportExcelTypeKey() {
		//CellData属性类型
		return CellDataTypeEnum.STRING;
	}

	public Integer convertToJavaData(ReadConverterContext<?> context) throws Exception {
		//CellData转对象属性
		String cellStr = context.getReadCellData().getStringValue();
		if (StrUtil.isEmpty(cellStr)) return null;
		if ("男".equals(cellStr)) {
			return 1;
		} else if ("女".equals(cellStr)) {
			return 0;
		} else {
			return null;
		}
	}

	public WriteCellData<?> convertToExcelData(WriteConverterContext<Integer> context) throws Exception {
		//对象属性转CellData
		Integer cellValue = context.getValue();
		if (cellValue == null) {
			return new WriteCellData<Object>("");
		}
		if (cellValue == 1) {
			return new WriteCellData<Object>("男");
		} else if (cellValue == 0) {
			return new WriteCellData<Object>("女");
		} else {
			return new WriteCellData<Object>("");
		}
	}
}
