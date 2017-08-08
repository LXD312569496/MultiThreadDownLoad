package com.example.asus.downloaddemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "THREAD_INFO".
*/
public class ThreadInfoDao extends AbstractDao<ThreadInfo, Long> {

    public static final String TABLENAME = "THREAD_INFO";

    /**
     * Properties of entity ThreadInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Url = new Property(1, String.class, "url", false, "URL");
        public final static Property Start = new Property(2, int.class, "start", false, "START");
        public final static Property End = new Property(3, int.class, "end", false, "END");
        public final static Property Finished = new Property(4, int.class, "finished", false, "FINISHED");
    }


    public ThreadInfoDao(DaoConfig config) {
        super(config);
    }
    
    public ThreadInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"THREAD_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"URL\" TEXT," + // 1: url
                "\"START\" INTEGER NOT NULL ," + // 2: start
                "\"END\" INTEGER NOT NULL ," + // 3: end
                "\"FINISHED\" INTEGER NOT NULL );"); // 4: finished
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"THREAD_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ThreadInfo entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(2, url);
        }
        stmt.bindLong(3, entity.getStart());
        stmt.bindLong(4, entity.getEnd());
        stmt.bindLong(5, entity.getFinished());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ThreadInfo entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(2, url);
        }
        stmt.bindLong(3, entity.getStart());
        stmt.bindLong(4, entity.getEnd());
        stmt.bindLong(5, entity.getFinished());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public ThreadInfo readEntity(Cursor cursor, int offset) {
        ThreadInfo entity = new ThreadInfo( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // url
            cursor.getInt(offset + 2), // start
            cursor.getInt(offset + 3), // end
            cursor.getInt(offset + 4) // finished
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ThreadInfo entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setUrl(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setStart(cursor.getInt(offset + 2));
        entity.setEnd(cursor.getInt(offset + 3));
        entity.setFinished(cursor.getInt(offset + 4));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ThreadInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ThreadInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ThreadInfo entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}