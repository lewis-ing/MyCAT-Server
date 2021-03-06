/*
 * Copyright (c) 2013, OpenCloudDB/MyCAT and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software;Designed and Developed mainly by many Chinese 
 * opensource volunteers. you can redistribute it and/or modify it under the 
 * terms of the GNU General Public License version 2 only, as published by the
 * Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 * 
 * Any questions about this component can be directed to it's project Web address 
 * https://code.google.com/p/opencloudb/.
 *
 */
package org.opencloudb.route;

import org.opencloudb.server.parser.ServerParse;

/**
 * @author mycat
 */
public final class RouteResultsetNode {
	private final String name; // 数据节点名称
	private final String statement; // 执行的语句
	private final int sqlType;
	private final boolean canRunInReadDB;
	private final boolean hasBlanceFlag;

	public RouteResultsetNode(String name, int sqlType, String statement) {
		this.name = name;
		this.sqlType = sqlType;
		this.statement = statement;
		canRunInReadDB = (sqlType == ServerParse.SELECT || sqlType == ServerParse.SHOW);
		hasBlanceFlag = (statement!=null)&&statement.startsWith("/*balance*/");
	}

	public boolean canRunnINReadDB(boolean autocommit) {
		return canRunInReadDB && (autocommit || hasBlanceFlag);

	}

	public String getName() {
		return name;
	}

	public int getSqlType() {
		return sqlType;
	}

	public String getStatement() {
		return statement;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj instanceof RouteResultsetNode) {
			RouteResultsetNode rrn = (RouteResultsetNode) obj;
			if (equals(name, rrn.getName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(name);
		s.append('{').append(statement).append('}');
		return s.toString();
	}

	private static boolean equals(String str1, String str2) {
		if (str1 == null) {
			return str2 == null;
		}
		return str1.equals(str2);
	}

	public boolean isModifySQL() {
		return !canRunInReadDB;
	}

}