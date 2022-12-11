package sam.g.trackuriboh.data.db.dao

import androidx.room.Delete
import androidx.room.Insert
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
    @Insert
    suspend fun insert(obj: T): Long

    @Insert
    suspend fun insert(objList: List<T>): List<Long>

    @Update
    suspend fun update(obj: T)

    @Update
    suspend fun update(objList: List<T>)

    /*
     * Upserting consists of a query + insert. If the table is big, the query can take long and
     * be a bottleneck. The query can be sped up by using Index.
     */

    @Upsert
    suspend fun upsert(obj: T): Long

    @Upsert
    suspend fun upsert(objList: List<T>): List<Long>

    @Delete
    suspend fun delete(obj: T)

    @Delete
    suspend fun delete(vararg objs: T)
}