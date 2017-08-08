package com.example.asus.downloaddemo.common.bean;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DOWN_LOAD_INFO".
*/
public class DownLoadInfoDao extends AbstractDao<DownLoadInfo, Long> {

    public static final String TABLENAME = "DOWN_LOAD_INFO";

    /**
     * Properties of entity DownLoadInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Url = new Property(1, String.class, "url", false, "URL");
        public final static Property TaskId = new Property(2, int.class, "taskId", false, "TASK_ID");
        public final static Property Finished = new Property(3, long.class, "finished", false, "FINISHED");
        public final static Property IsFinish = new Property(4, boolean.class, "isFinish", false, "IS_FINISH");
    }


    public DownLoadInfoDao(DaoConfig config) {
        super(config);
    }
    
    public DownLoadInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DOWN_LOAD_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"URL\" TEXT," + // 1: url
                "\"TASK_ID\" INTEGER NOT NULL ," + // 2: taskId
                "\"FINISHED\" INTEGER NOT NULL ," + // 3: finished
                "\"IS_FINISH\" INTEGER NOT NULL );"); // 4: isFinish
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DOWN_LOAD_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DownLoadInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(2, url);
        }
        stmt.bindLong(3, entity.getTaskId());
        stmt.bindLong(4, entity.getFinished());
        stmt.bindLong(5, entity.getIsFinish() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DownLoadInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(2, url);
        }
        stmt.bindLong(3, entity.getTaskId());
        stmt.bindLong(4, entity.getFinished());
        stmt.bindLong(5, entity.getIsFinish() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public DownLoadInfo readEntity(Cursor cursor, int offset) {
        DownLoadInfo entity = new DownLoadInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // url
            cursor.getInt(offset + 2), // taskId
            cursor.getLong(offset + 3), // finished
            cursor.getShort(offset + 4) != 0 // isFinish
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DownLoadInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUrl(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setTaskId(cursor.getInt(offset + 2));
        entity.setFinished(cursor.getLong(offset + 3));
        entity.setIsFinish(cursor.getShort(offset + 4) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DownLoadInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DownLoadInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DownLoadInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
