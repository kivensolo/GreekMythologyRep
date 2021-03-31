## DAO的具体使用
@Insert支持设置冲突策略，默认为OnConflictStrategy.ABORT即中止并回滚。
还可以指定为其他策略。
- OnConflictStrategy.REPLACE 冲突时替换为新记录
- OnConflictStrategy.IGNORE 忽略冲突(不推荐)
- OnConflictStrategy.ROLLBACK 已废弃，使用ABORT替代
- OnConflictStrategy.FAIL 同上

其声明的方法返回值可为空，也可为插入行的ID或列表。
- fun insertWithOutId(movie: Movie?)
- fun insert(movie: Movie?): Long?
- fun insert(vararg movies: Movie?): LongArray?

**@Delete**

和@Insert一样支持不返回删除结果或返回删除的函数，不再赘述。

@Update

和@Insert一样支持设置冲突策略和定制返回更新结果。此外需要注意的是@Update操作将匹配参数的主键id去更新字段。
fun update(vararg movies: Movie?): Int

@Query

查询操作主要依赖@Update的value，指定不同的SQL语句即可获得相应的查询结果。
在编译阶段就将验证语句是否正确，避免错误的查询语句影响到运行阶段。

- 查询所有字段
@get:Query(“SELECT * FROM movie”)

- 查询指定字段
@get:Query(“SELECT id, movie_name, actor_name, post_year, review_score FROM movie”)

- 排序查询
@get:Query(“SELECT * FROM movie ORDER BY post_year DESC”) 比如查询最近发行的电影列表

- 匹配查询
@Query(“SELECT * FROM movie WHERE id = :id”)

- 多字段匹配查询
@Query(“SELECT * FROM movie WHERE movie_name LIKE :keyWord " + " OR actor_name LIKE :keyWord”) 比如查询名称和演员中匹配关键字的电影

- 模糊查询
@Query(“SELECT * FROM movie WHERE movie_name LIKE ‘%’ || :keyWord || ‘%’ " + " OR actor_name LIKE ‘%’ || :keyWord || ‘%’”) 比如查询名称和演员中包含关键字的电影

- 限制行数查询
@Query(“SELECT * FROM movie WHERE movie_name LIKE :keyWord LIMIT 3”) 比如查询名称匹配关键字的前三部电影

- 参数引用查询
@Query(“SELECT * FROM movie WHERE review_score >= :minScore”) 比如查询评分大于指定分数的电影

- 多参数查询
@Query(“SELECT * FROM movie WHERE post_year BETWEEN :minYear AND :maxYear”) 比如查询介于发行年份区间的电影

- 不定参数查询
@Query(“SELECT * FROM movie WHERE movie_name IN (:keyWords)”)

- Cursor查询
@Query(“SELECT * FROM movie WHERE movie_name LIKE ‘%’ || :keyWord || ‘%’ LIMIT :limit”)

fun searchMoveCursorByLimit(keyWord: String?, limit: Int): Cursor?
注意：Cursor需要保证查询到的字段和取值一一对应，所以不推荐使用

- 响应式查询
demo采用的LiveData进行的观察式查询，还可以配合RxJava2，Kotlin的Flow进行响应式查询。

## 数据库升级降级
在数据库表对应类里增加了新字段后，重新运行已创建过DB的demo会发生崩溃。
Room cannot verify the data integrity.
Looks like you've changed schema but forgot to update the version number.

单独升级版本号，也会发生崩溃，也可以使用fallbackToDestructiveMigration()以允许许升级失败时破坏性地删除DB。
并且onDestructiveMigration()将被回调。在这个回调里可以试着重新初始化DB。

但是这种操作对用户体验很难受，一般是进行数据库迁移操作.

# 事务处理
当我们的DB操作需要保持一致性，或者查询关联性结果的时候需要保证事务处理。
Room提供了@Transaction注解帮助我们快速实现这个需求，它将确保注解内的方法运行在同一个事务模式。
@Dao
public interface MovieDao {
    @Transaction
    default void insetNewAndDeleteOld(Movie newMovie, Movie oldMovie) {
        insert(newMovie);
        delete(oldMovie);
    }
}
需要注意的是，事务处理比较占用性能，避免在事务处理的方法内执行耗时逻辑。
另外，@Inset、@Delete和@Update的处理自动在事务模式进行处理，无需增加@Transaction注解。
public long[] insert(final Movie... movies) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
        long[] _result = __insertionAdapterOfMovie.insertAndReturnIdsArray(movies);
        __db.setTransactionSuccessful();
        return _result;
    } finally {
        __db.endTransaction();
    }
}
RoomDatabase的beginTransaction()和endTransaction()不推荐外部使用了，
可以采用封装好的runInTransaction()实现。
db.runInTransaction(Runnable {
    val database = db.getOpenHelper().getWritableDatabase();

    val contentValues = ContentValues()
    contentValues.put("movie_name", newMovie.getName())
    contentValues.put("actor_name", newMovie.getActor())
    contentValues.put("post_year", newMovie.getYear())
    contentValues.put("review_score", newMovie.getScore())

    database.insert("movie", SQLiteDatabase.CONFLICT_ABORT, contentValues)
    database.delete("movie", "id = " + oldMovie.getId(), null)
})
