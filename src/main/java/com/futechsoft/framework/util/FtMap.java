package com.futechsoft.framework.util;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.CaseUtils;

import com.futechsoft.framework.common.sqlHelper.Column;

public class FtMap extends HashMap<String, Object> {

	private static final long serialVersionUID = 1L;

	public FtMap() {
		super();
	}

	private boolean isCamelCase(String s) {
		String camelCasePattern = "([a-z]+[a-zA-Z0-9]+)+";
		return s.matches(camelCasePattern);
	}

	/*
	 * private String toCamelCase(String target) { StringBuffer buffer = new
	 * StringBuffer(); for (String token : target.toLowerCase().split("_")){
	 * buffer.append(StringUtils.capitalize(token)); } return
	 * StringUtils.uncapitalize(buffer.toString()); }
	 */

	public static String getSnakeCase(String s) {
		String ret = s.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2").replaceAll("([a-z])([A-Z])", "$1_$2");
		return ret.toLowerCase();

	}

	public FtMap(Map<String, String[]> parameterMap) {

		for (String key : parameterMap.keySet()) {
			String[] values = parameterMap.get(key);

			if (key.lastIndexOf("_") != -1) {
				key = CaseUtils.toCamelCase(key, false, new char[] { '_' });
			}

			if (!isCamelCase(key)) {
				key = key.toLowerCase();
			}

			if (values.length > 1) {
				super.put(key, values);
			} else {
				super.put(key, values[0]);
			}

		}

	}

	public void setFtMap(Map<String, Object> map) {
		for (String key : map.keySet()) {
			Object values = map.get(key);
			if (key.lastIndexOf("_") != -1) {
				key = CaseUtils.toCamelCase(key, false, new char[] { '_' });
			}

			if (!isCamelCase(key)) {
				key = key.toLowerCase();
			}
			super.put(key, values);
		}
	}

	public boolean containsKey(String key) {
		if (key.lastIndexOf("_") != -1) {
			key = CaseUtils.toCamelCase(key, false, new char[] { '_' });
		}

		if (!isCamelCase(key)) {
			key = key.toLowerCase();
		}

		return super.containsKey(key);
	}

	public Object get(String key) {
		Object value = null;

		if (key.lastIndexOf("_") != -1) {
			key = CaseUtils.toCamelCase(key, false, new char[] { '_' });
		}

		if (!isCamelCase(key)) {
			key = key.toLowerCase();
		}

		if (super.containsKey(key)) {
			value = super.get(key);
		}
		return value;
	}

	public Object put(String key, Object value) {

		if (key.lastIndexOf("_") != -1) {
			key = CaseUtils.toCamelCase(key, false, new char[] { '_' });
		}

		if (!isCamelCase(key)) {
			key = key.toLowerCase();
		}

		return super.put(key, value);
	}

	public String getString(String key) {

		Object value = get(key);

		if (value instanceof java.sql.Clob) {
			StringBuffer strOut = new StringBuffer();
			String str = "";
			BufferedReader br = null;
			try {
				br = new BufferedReader(((java.sql.Clob) value).getCharacterStream());
				while ((str = br.readLine()) != null) {
					strOut.append(str);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return strOut.toString();
		} else {
			if (get(key) == null) {
				return "";
			}
			return get(key).toString();
		}

	}

	public String[] getStringArray(String key) {

		if(get(key) instanceof Object[]) {
			return (String[]) get(key);
		}else {
			String[] temp = new  String[1];
			temp[0]=getString(key);
			return temp;
		}

	}

	public int getInt(String key) {
		return Integer.parseInt(getString(key).equals("")?"0":getString(key));
	}

	public long getLong(String key) {
		return Long.parseLong(getString(key).equals("")?"0":getString(key));
	}

	public double getDouble(String key) {
		return Double.parseDouble(getString(key).equals("")?"0":getString(key));
	}

	public boolean getBoolean(String key) {
		return Boolean.parseBoolean(getString(key));
	}

	public void remove(String key) {
		if (key.lastIndexOf("_") != -1) {
			key = CaseUtils.toCamelCase(key, false, new char[] { '_' });
		}

		if (!isCamelCase(key)) {
			key = key.toLowerCase();
		}

		if (containsKey(key)) {
			super.remove(key);
		}
	}

	public Map<String, String> getTblMap(Map<String, Object> map) throws Exception {

		String userNo = SecurityUtil.getUserNo();

		map.put(Column.RGSTP_NO, userNo);
		map.put(Column.UPDTP_NO, userNo);

		Map<String, String> tblMap = new HashMap<String, String>();

		for (String key : map.keySet()) {

			String values = map.get(key).toString();
			if (isCamelCase(key)) {
				key = getSnakeCase(key);
			}

			if (!key.startsWith("sch_") && !key.startsWith("s_") && !key.startsWith("search_")
					&& !key.startsWith("page_") && !key.equals("display_page")) {
				tblMap.put(key, values);
			}

		}

		return tblMap;
	}

	public Map<String, String> getTblMap(Map<String, Object> map, List<FtMap> columnList) throws Exception {

		String userNo = SecurityUtil.getUserNo();

		map.put(Column.RGSTP_NO, userNo);
		map.put(Column.UPDTP_NO, userNo);

		Map<String, String> tblMap = new HashMap<String, String>();

		for (FtMap columnMap : columnList) {
			String tmpKey = columnMap.getString("field").toLowerCase();

			if (isCamelCase(tmpKey)) {
				tmpKey = getSnakeCase(tmpKey);
			}

			String key = columnMap.getString("field");
			key = CaseUtils.toCamelCase(key, false, new char[] { '_' });

			if (map.containsKey(key)) {

				if (!key.endsWith("dttm")) {
					if (map.get(key) != null && tmpKey != null) {
						String values = map.get(key).toString();
						tblMap.put(tmpKey, values);
					}
				}

			}

		}

		return tblMap;
	}

	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    for (Map.Entry<String, Object> entry : this.entrySet()) {
	        sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
	    }
	    return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : ""; // 마지막 "&" 제거
	}

}
