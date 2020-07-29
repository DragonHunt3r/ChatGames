package net.steelphoenix.chatgames.util;

import java.io.File;
import java.sql.SQLException;

public class SQLiteDatabase extends net.steelphoenix.core.database.SQLiteDatabase {
	public SQLiteDatabase(File file) throws ClassNotFoundException {
		super(file);
	}
	@Override
	public final boolean isOpen(int secs) throws SQLException {
		try {
			return super.isOpen(secs);
		} catch (AbstractMethodError exception) {
			// Old org.sqlite.JDBC driver does not have an implementation for java.sql.Connection#isValid(int timeout)
			return true;
		}
	}
}
