package sam.g.trackuriboh.data.db.dao

import androidx.room.Delete
import androidx.room.Update
import androidx.room.Upsert

interface BaseDao<T> {
    /*
     * The long returned here is the rowId, not the inserted item's id.
     *
     * See https://www.sqlite.org/rowidtable.html for more info. Main keypoints is
     *
     * The table's PRIMARY key is not the true primary key - the rowId is. Primary key constraints are just UNIQUE constraints.
     */
    @Upsert
    suspend fun upsert(obj: T): Long

    @Upsert
    suspend fun upsert(objList: List<T>): List<Long>

    @Update
    suspend fun update(obj: T)

    @Update
    suspend fun update(objList: List<T>)

    @Delete
    suspend fun delete(obj: T)
}